package com.rm.rmfreedraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Riccardo Moro on 9/10/2016.
 */
public class RMFreeDrawVIew extends View implements View.OnTouchListener {

    private static final String TAG = RMFreeDrawVIew.class.getSimpleName();

    private SerializablePaint mCurrentPaint;
    private SerializablePath mCurrentPath;

    private ArrayList<Point> mPoints = new ArrayList<>();
    private ArrayList<HistoryPath> mPaths = new ArrayList<>();
    private ArrayList<HistoryPath> mCanceledPaths = new ArrayList<>();

    private int mLastDimensionW = -1;
    private int mLastDimensionH = -1;

    private boolean mFinishPath = false;

    public RMFreeDrawVIew(Context context) {
        this(context, null);
    }

    public RMFreeDrawVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RMFreeDrawVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Todo custom attributes
        setOnTouchListener(this);

        setBackgroundColor(Color.WHITE);

        initPaint();
    }

    @Override
    protected Parcelable onSaveInstanceState() {

        // Get the superclass parcelable state
        Parcelable superState = super.onSaveInstanceState();

        if (mPoints.size() > 0) {// Currently doing a line, save it's current path
            createPathFromPoints();
        }

        return new FreeDrawSavedState(superState, mPaths, mLastDimensionW, mLastDimensionH);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        // If not instance of my state, let the superclass handle it
        if (!(state instanceof FreeDrawSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        FreeDrawSavedState savedState = (FreeDrawSavedState) state;
        // Superclass restore state
        super.onRestoreInstanceState(savedState.getSuperState());

        // My state restore
        mPaths = savedState.getPaths();
        // Restore the last dimensions, so that in onSizeChanged i can calculate the
        // height and width change factor and multiply every point x or y to it, so that if the
        // View is resized, it adapt automatically it's points to the new width/height
        mLastDimensionW = savedState.getLastDimensionW();
        mLastDimensionH = savedState.getLastDimensionH();
    }

    /**
     * Set the paint color
     *
     * @param color The now color to be applied to the
     */
    public void setPaintColor(@ColorInt int color) {
        mFinishPath = true;

        invalidate();

        mCurrentPaint.setColor(color);
    }

    /**
     * Set the paint width in px
     *
     * @param widthPx The new weight in px, must be > 0
     */
    public void setPaintWidthPx(@FloatRange(from = 0) float widthPx) {
        if (widthPx > 0) {
            mFinishPath = true;

            invalidate();

            mCurrentPaint.setStrokeWidth(widthPx);
        }
    }

    /**
     * Set the paint width in dp
     *
     * @param dp The new weight in dp, must be > 0
     */
    public void setPaintWithDp(float dp) {
        setPaintWidthPx(FreeDrawHelper.convertDpToPixels(dp));
    }

    /**
     * Set the paint opacity, must be between 0 and 1
     *
     * @param alpha The alpha to apply to the paint
     */
    public void setPaintAlpha(@IntRange(from = 0, to = 255) int alpha) {

        // Finish current path and redraw, so that the new setting is applied only to the next path
        mFinishPath = true;
        invalidate();

        mCurrentPaint.setAlpha(alpha);
    }

    /**
     * Cancel the last drawn segment
     */
    public void undoLast() {

        if (mPaths.size() > 0) {
            // End current path
            mFinishPath = true;
            invalidate();

            // Cancel the last one and redraw
            mCanceledPaths.add(mPaths.get(mPaths.size() - 1));
            mPaths.remove(mPaths.size() - 1);
            invalidate();
        }
    }

    /**
     * Re-add the first removed path and redraw
     */
    public void redoLast() {

        if (mCanceledPaths.size() > 0) {
            mPaths.add(mCanceledPaths.get(mCanceledPaths.size() - 1));
            mCanceledPaths.remove(mCanceledPaths.size() - 1);
            invalidate();
        }
    }

    /**
     * Remove all the paths and redraw (can be undone with {@link #redoLast()})
     */
    public void clearAll() {

        mCanceledPaths.addAll(mPaths);
        mPaths.clear();
        invalidate();
    }

    // TODO Remove and take from custom xml attributes
    private void initPaint() {
        mCurrentPaint = new SerializablePaint(Paint.ANTI_ALIAS_FLAG);
        mCurrentPaint.setColor(Color.BLUE);
        mCurrentPaint.setStrokeJoin(Paint.Join.ROUND);
        mCurrentPaint.setStrokeCap(Paint.Cap.ROUND);
        mCurrentPaint.setPathEffect(new ComposePathEffect(
                new CornerPathEffect(100f),
                new CornerPathEffect(100f)));
        mCurrentPaint.setStyle(Paint.Style.STROKE);
        mCurrentPaint.setStrokeWidth(FreeDrawHelper.convertDpToPixels(6));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < mPaths.size(); i++) {
            HistoryPath currentPath = mPaths.get(i);

            // If the path is just a single point, draw as a point
            if (currentPath.isPoint()) {
                canvas.drawCircle(currentPath.getOriginX(), currentPath.getOriginY(),
                        currentPath.getPaint().getStrokeWidth() / 2, currentPath.getPaint());
            } else {// Else draw the complete path
                canvas.drawPath(currentPath.getPath(), currentPath.getPaint());
            }
        }


        if (mCurrentPath == null)
            mCurrentPath = new SerializablePath();
        else
            mCurrentPath.rewind();

        // If a single point, add a circle to the path
        if (mPoints.size() == 1) {
            mCurrentPath.addCircle(mPoints.get(0).x, mPoints.get(0).y,
                    mCurrentPaint.getStrokeWidth() / 2, Path.Direction.CW);
        } else {// Else draw the complete series of points

            boolean first = true;

            for (int i = 0; i < mPoints.size(); i++) {

                Point point = mPoints.get(i);

                if (first) {
                    mCurrentPath.moveTo(point.x, point.y);
                    first = false;
                } else {
                    mCurrentPath.lineTo(mPoints.get(i).x, mPoints.get(i).y);
                }
            }
        }

        // If the path is finished, add it to the history
        if (mFinishPath && mPoints.size() > 0) {
            createPathFromPoints();
        }

        canvas.drawPath(mCurrentPath, mCurrentPaint);
    }

    // Create a path from the current points
    private void createPathFromPoints() {
        mPaths.add(new HistoryPath(
                new SerializablePath(mCurrentPath), new SerializablePaint(mCurrentPaint),
                mPoints.get(0).x, mPoints.get(0).y, FreeDrawHelper.isAPoint(mPoints)));
        mPoints.clear();

        mFinishPath = false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        // Clear all the history when restarting to draw
        mCanceledPaths.clear();

        if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
            Point point;
            for (int i = 0; i < motionEvent.getHistorySize(); i++) {
                point = new Point();
                point.x = motionEvent.getHistoricalX(i);
                point.y = motionEvent.getHistoricalY(i);
                mPoints.add(point);
            }
            point = new Point();
            point.x = motionEvent.getX();
            point.y = motionEvent.getY();
            mPoints.add(point);
        } else
            mFinishPath = true;

        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float xMultiplyFactor = 1;
        float yMultiplyFactor = 1;


        if (mLastDimensionW == -1) {
            mLastDimensionW = w;
        }

        if (mLastDimensionH == -1) {
            mLastDimensionH = h;
        }

        if (w >= 0 && w != oldw && w != mLastDimensionW) {
            xMultiplyFactor = (float) w / mLastDimensionW;
            mLastDimensionW = w;
        }

        if (h >= 0 && h != oldh && h != mLastDimensionH) {
            yMultiplyFactor = (float) h / mLastDimensionH;
            mLastDimensionH = h;
        }

        multiplyPathsAndPoints(xMultiplyFactor, yMultiplyFactor);
    }

    // Translate all the paths, used every time that this view size is changed
    private void multiplyPathsAndPoints(float xMultiplyFactor, float yMultiplyFactor) {
        if ((xMultiplyFactor == 1 && yMultiplyFactor == 1)
                || (xMultiplyFactor <= 0 || yMultiplyFactor <= 0)) {
            return;
        }

        for (HistoryPath historyPath : mPaths) {

            // If it's a point, just multiply it's origins
            if (historyPath.isPoint()) {
                historyPath.setOriginX(historyPath.getOriginX() * xMultiplyFactor);
                historyPath.setOriginY(historyPath.getOriginY() * yMultiplyFactor);
            } else {

                // Doing this because of android, which has a problem with
                // multiple path transformations
                SerializablePath scaledPath = new SerializablePath();
                scaledPath.addPath(historyPath.getPath(),
                        new TranslateMatrix(xMultiplyFactor, yMultiplyFactor));
                historyPath.getPath().close();
                historyPath.setPath(scaledPath);
            }
        }

        for (Point point : mPoints) {
            point.x *= xMultiplyFactor;
            point.y *= yMultiplyFactor;
        }
    }
}

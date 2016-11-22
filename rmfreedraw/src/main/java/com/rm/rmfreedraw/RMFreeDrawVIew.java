package com.rm.rmfreedraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Riccardo Moro on 9/10/2016.
 */
public class RMFreeDrawVIew extends View implements View.OnTouchListener {
    // TODO Riccardo: let the user choose between keep aspect ratio or fit
    private static final String TAG = RMFreeDrawVIew.class.getSimpleName();

    private static final float DEFAULT_STROKE_WIDTH = 4;
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_ALPHA = 255;

    private SerializablePaint mCurrentPaint;
    private SerializablePath mCurrentPath;

    private ResizeBehaviour mResizeBehaviour = ResizeBehaviour.FIT_INSIDE;

    private ArrayList<Point> mPoints = new ArrayList<>();
    private ArrayList<HistoryPath> mPaths = new ArrayList<>();
    private ArrayList<HistoryPath> mCanceledPaths = new ArrayList<>();

    @ColorInt
    private int mPaintColor = DEFAULT_COLOR;
    @IntRange(from = 0, to = 255)
    private int mPaintAlpha = DEFAULT_ALPHA;

    private int mLastDimensionW = -1;
    private int mLastDimensionH = -1;

    private boolean mFinishPath = false;

    // Needed to draw points
    private Paint mFillPaint;

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

        initPaints();
    }

    @Override
    protected Parcelable onSaveInstanceState() {

        // Get the superclass parcelable state
        Parcelable superState = super.onSaveInstanceState();

        if (mPoints.size() > 0) {// Currently doing a line, save it's current path
            createPathFromPoints();
        }

        return new FreeDrawSavedState(superState, mPaths, mCanceledPaths,
                mCurrentPaint, mPaintColor, mPaintAlpha, mResizeBehaviour,
                mLastDimensionW, mLastDimensionH);
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
        mCanceledPaths = savedState.getCanceledPaths();
        mCurrentPaint = savedState.getCurrentPaint();

        initFillPaint();

        mResizeBehaviour = savedState.getResizeBehaviour();

        mPaintColor = savedState.getPaintColor();
        mPaintAlpha = savedState.getPaintAlpha();
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

        mPaintColor = color;

        mCurrentPaint.setColor(mPaintColor);
        mCurrentPaint.setAlpha(mPaintAlpha);// Restore the previous alpha
    }

    /**
     * Get the current paint color without it's alpha
     */
    @ColorInt
    public int getPaintColor() {
        return mPaintColor;
    }

    /**
     * Get the current color with the current alpha
     */
    @ColorInt
    public int getPaintColorWithAlpha() {
        return mCurrentPaint.getColor();
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
     * {@link #getPaintWith(boolean)}
     */
    @FloatRange(from = 0)
    public float getPaintWidth() {
        return getPaintWith(false);
    }

    /**
     * Get the current paint with in dp or pixel
     */
    @FloatRange(from = 0)
    public float getPaintWith(boolean inDp) {
        if (inDp) {
            return FreeDrawHelper.convertPixelsToDp(mCurrentPaint.getStrokeWidth());
        } else {
            return mCurrentPaint.getStrokeMiter();
        }
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

        mPaintAlpha = alpha;
        mCurrentPaint.setAlpha(mPaintAlpha);
    }

    /**
     * Get the current paint alpha
     */
    @IntRange(from = 0, to = 255)
    public int getPaintAlpha() {
        return mPaintAlpha;
    }


    /**
     * Set what to do when the view is resized (on rotation if its dimensions are not fixed)
     * {@link ResizeBehaviour}
     */
    public void setResizeBehaviour(ResizeBehaviour newBehaviour) {
        mResizeBehaviour = newBehaviour;
    }

    /**
     * Get the current behaviour on view resize
     */
    public ResizeBehaviour getResizeBehaviour() {
        return mResizeBehaviour;
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
     * Re-add all the removed paths and redraw
     */
    public void redoAll() {

        if (mCanceledPaths.size() > 0) {
            mPaths.addAll(mCanceledPaths);
            mCanceledPaths.clear();
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
        Collections.reverse(mPaths);
        mCanceledPaths.addAll(mPaths);
        mPaths.clear();
        invalidate();
    }

    /**
     * Get how many undo operations are available
     */
    public int getUndoCount() {
        return mPaths.size();
    }

    /**
     * Get how many redo operations are available
     */
    public int getRedoCount() {
        return mCanceledPaths.size();
    }

    private void initPaints() {
        mCurrentPaint = new SerializablePaint(Paint.ANTI_ALIAS_FLAG);
        mCurrentPaint.setColor(mPaintColor);
        mCurrentPaint.setAlpha(mPaintAlpha);
        mCurrentPaint.setStrokeJoin(Paint.Join.ROUND);
        mCurrentPaint.setStrokeCap(Paint.Cap.ROUND);
        mCurrentPaint.setPathEffect(new ComposePathEffect(
                new CornerPathEffect(100f),
                new CornerPathEffect(100f)));
        mCurrentPaint.setStyle(Paint.Style.STROKE);
        mCurrentPaint.setStrokeWidth(FreeDrawHelper.convertDpToPixels(DEFAULT_STROKE_WIDTH));

        initFillPaint();
    }

    private void initFillPaint() {
        mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFillPaint.setStyle(Paint.Style.FILL);
    }

    private void setupFillPaint(Paint from) {
        mFillPaint.setColor(from.getColor());
        mFillPaint.setAlpha(from.getAlpha());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Avoid concurrency errors by first setting the finished path variable to false
        final boolean finishedPath = mFinishPath;
        mFinishPath = false;

        for (int i = 0; i < mPaths.size(); i++) {
            HistoryPath currentPath = mPaths.get(i);

            // If the path is just a single point, draw as a point
            if (currentPath.isPoint()) {
                setupFillPaint(currentPath.getPaint());
                canvas.drawCircle(currentPath.getOriginX(), currentPath.getOriginY(),
                        currentPath.getPaint().getStrokeWidth() / 2, mFillPaint);
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
            setupFillPaint(mCurrentPaint);
            canvas.drawCircle(mPoints.get(0).x, mPoints.get(0).y,
                    mCurrentPaint.getStrokeWidth() / 2, mFillPaint);
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
        if (finishedPath && mPoints.size() > 0) {
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
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        // Clear all the history when restarting to draw
        mCanceledPaths.clear();

        if ((motionEvent.getAction() != MotionEvent.ACTION_UP) &&
                (motionEvent.getAction() != MotionEvent.ACTION_CANCEL)) {
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
    @SuppressWarnings("SuspiciousNameCombination")
    private void multiplyPathsAndPoints(float xMultiplyFactor, float yMultiplyFactor) {

        // If both factors == 1 or <= 0 or no paths/points to apply things, just return
        if ((xMultiplyFactor == 1 && yMultiplyFactor == 1)
                || (xMultiplyFactor <= 0 || yMultiplyFactor <= 0) ||
                (mPaths.size() == 0 && mCanceledPaths.size() == 0 && mPoints.size() == 0)) {
            return;
        }

        if (mResizeBehaviour == ResizeBehaviour.CLEAR) {// If clear, clear all and return
            mPaths.clear();
            mCanceledPaths.clear();
            mPoints.clear();
            return;
        } else if (mResizeBehaviour == ResizeBehaviour.FIT_INSIDE) {

            if (xMultiplyFactor > 1 || yMultiplyFactor <= 1) {
                // If fit inside, just use the lowest among the scale factors
                if (xMultiplyFactor < yMultiplyFactor) {
                    yMultiplyFactor = xMultiplyFactor;
                } else {
                    xMultiplyFactor = yMultiplyFactor;
                }
            } else {
                if (xMultiplyFactor > yMultiplyFactor) {
                    yMultiplyFactor = xMultiplyFactor;
                } else {
                    xMultiplyFactor = yMultiplyFactor;
                }
            }
        } else if (mResizeBehaviour == ResizeBehaviour.CROP) {
            xMultiplyFactor = yMultiplyFactor = 1;
        }

        // Adapt drawn paths
        for (HistoryPath historyPath : mPaths) {

            multiplySinglePath(historyPath, xMultiplyFactor, yMultiplyFactor);
        }

        // Adapt canceled paths
        for (HistoryPath historyPath : mCanceledPaths) {

            multiplySinglePath(historyPath, xMultiplyFactor, yMultiplyFactor);
        }

        // Adapt drawn points
        for (Point point : mPoints) {
            point.x *= xMultiplyFactor;
            point.y *= yMultiplyFactor;
        }
    }

    private void multiplySinglePath(HistoryPath historyPath,
                                    float xMultiplyFactor, float yMultiplyFactor) {

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

    public Bitmap getDrawScreenshot() {
        Bitmap screenShotBitmap = Bitmap.createBitmap(
                getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenShotBitmap);
        draw(c);
        return screenShotBitmap;
    }
}

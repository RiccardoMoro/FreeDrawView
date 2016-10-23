package com.rm.rmfreedraw;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
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

    private Paint mCurrentPaint;
    private Path mCurrentPath;

    private ArrayList<Point> mPoints = new ArrayList<>();
    private ArrayList<HistoryPath> mPaths = new ArrayList<>();
    private ArrayList<HistoryPath> mCanceledPaths = new ArrayList<>();

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
        setPaintWidthPx(convertDpToPixels(dp));
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
        mCurrentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentPaint.setColor(Color.BLUE);
        mCurrentPaint.setStrokeJoin(Paint.Join.ROUND);
        mCurrentPaint.setStrokeCap(Paint.Cap.ROUND);
        mCurrentPaint.setPathEffect(new ComposePathEffect(
                new CornerPathEffect(100f),
                new CornerPathEffect(100f)));
        mCurrentPaint.setStyle(Paint.Style.STROKE);
        mCurrentPaint.setStrokeWidth(convertDpToPixels(6));
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
            mCurrentPath = new Path();
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
                    //Point prev = mPoints.get(i - 1);
                    mCurrentPath.lineTo(mPoints.get(i).x, mPoints.get(i).y);
                }
            }
        }

        // If the path is finished, add it to the history
        if (mFinishPath && mPoints.size() > 0) {
            mPaths.add(new HistoryPath(new Path(mCurrentPath), new Paint(mCurrentPaint),
                    mPoints.get(0).x, mPoints.get(0).y, PointHelper.isAPoint(mPoints)));
            mPoints.clear();

            mFinishPath = false;
        }

        canvas.drawPath(mCurrentPath, mCurrentPaint);
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

    private float convertDpToPixels(float dp) {
        return (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

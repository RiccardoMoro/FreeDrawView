package com.rm.rmfreedraw;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Riccardo Moro on 9/10/2016.
 */
public class RMFreeDrawVIew extends View implements View.OnTouchListener {
    private static final String TAG = RMFreeDrawVIew.class.getSimpleName();

    private static Paint sPaint;

    private ArrayList<Point> mPoints = new ArrayList<>();
    private ArrayList<Path> mPaths = new ArrayList<>();

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

    private void initPaint() {
        sPaint = new Paint();
        sPaint.setAntiAlias(true);
        sPaint.setColor(Color.BLUE);
        sPaint.setStyle(Paint.Style.STROKE);
        sPaint.setStrokeWidth(convertDpToPixels(6));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < mPaths.size(); i++) {
            canvas.drawPath(mPaths.get(i), sPaint);
        }

        Path newPath = new Path();

        if (mPoints.size() > 1) {
            for (int i = mPoints.size() - 2; i < mPoints.size(); i++) {
                if (i >= 0) {
                    Point point = mPoints.get(i);

                    if (i == 0) {
                        Point next = mPoints.get(i + 1);
                        point.dx = ((next.x - point.x) / 3);
                        point.dy = ((next.y - point.y) / 3);
                    } else if (i == mPoints.size() - 1) {
                        Point prev = mPoints.get(i - 1);
                        prev.dx = ((point.x - prev.x) / 3);
                        prev.dy = ((point.y - prev.y) / 3);
                    } else {
                        Point next = mPoints.get(i + 1);
                        Point prev = mPoints.get(i - 1);
                        point.dx = ((next.x - prev.x) / 3);
                        point.dy = ((next.y - prev.y) / 3);
                    }
                }
            }
        }

        boolean first = true;

        for (int i = 0; i < mPoints.size(); i++) {
            Point point = mPoints.get(i);

            if (first) {
                newPath.moveTo(point.x, point.y);
                first = false;
            } else {
                Point prev = mPoints.get(i - 1);
                newPath.cubicTo(prev.x + prev.dx, prev.y + prev.dy, point.x - point.dx, point.y - point.dy, point.x, point.y);
            }
        }

        if (mFinishPath) {
            mPaths.add(newPath);
            mPoints.clear();

            mFinishPath = false;
        }

        canvas.drawPath(newPath, sPaint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

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

    private int convertDpToPixels(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

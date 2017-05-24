package com.rm.freedrawview;

import android.graphics.Paint;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Riccardo on 23/05/2017.
 */

public class FreeDrawSerializableState implements Serializable {

    private ArrayList<HistoryPath> mCanceledPaths;
    private ArrayList<HistoryPath> mPaths;
    private Paint mCurrentPaint;

    private int mPaintColor;
    private int mPaintAlpha;

    private ResizeBehaviour mResizeBehaviour;

    private int mLastDimensionW;
    private int mLastDimensionH;

    public FreeDrawSerializableState(ArrayList<HistoryPath> canceledPaths,
                                     ArrayList<HistoryPath> paths, Paint currentPaint,
                                     int paintColor, int paintAlpha,
                                     ResizeBehaviour resizeBehaviour, int lastW, int lastH) {

        setCanceledPaths(canceledPaths != null ? canceledPaths : new ArrayList<HistoryPath>());
        setPaths(paths != null ? paths : new ArrayList<HistoryPath>());
        if (currentPaint != null) {
            setCurrentPaint(currentPaint);
        }
        setPaintColor(paintColor);
        setPaintAlpha(paintAlpha);
        setResizeBehaviour(resizeBehaviour);
        setLastDimensionW(lastW >= 0 ? lastW : 0);
        setLastDimensionH(lastH >= 0 ? lastH : 0);
    }

    public ArrayList<HistoryPath> getCanceledPaths() {
        return mCanceledPaths;
    }

    public void setCanceledPaths(ArrayList<HistoryPath> canceledPaths) {
        this.mCanceledPaths = canceledPaths;
    }

    public ArrayList<HistoryPath> getPaths() {
        return mPaths;
    }

    public void setPaths(ArrayList<HistoryPath> paths) {
        this.mPaths = paths;
    }

    public Paint getCurrentPaint() {
        return mCurrentPaint;
    }

    public void setCurrentPaint(Paint currentPaint) {
        this.mCurrentPaint = currentPaint;
    }

    public int getPaintColor() {
        return mPaintColor;
    }

    public void setPaintColor(int paintColor) {
        this.mPaintColor = paintColor;
    }

    public int getPaintAlpha() {
        return mPaintAlpha;
    }

    public void setPaintAlpha(int paintAlpha) {
        this.mPaintAlpha = paintAlpha;
    }

    public ResizeBehaviour getResizeBehaviour() {
        return mResizeBehaviour;
    }

    public void setResizeBehaviour(ResizeBehaviour resizeBehaviour) {
        this.mResizeBehaviour = resizeBehaviour;
    }

    public int getLastDimensionW() {
        return mLastDimensionW;
    }

    public void setLastDimensionW(int lastDimensionW) {
        this.mLastDimensionW = lastDimensionW;
    }

    public int getLastDimensionH() {
        return mLastDimensionH;
    }

    public void setLastDimensionH(int lastDimensionH) {
        this.mLastDimensionH = lastDimensionH;
    }
}

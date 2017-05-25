package com.rm.freedrawview;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Riccardo on 23/05/2017.
 */

public class FreeDrawSerializableState implements Serializable {

    static final long serialVersionUID = 40L;

    private ArrayList<HistoryPath> mCanceledPaths;
    private ArrayList<HistoryPath> mPaths;

    private int mPaintColor;
    private int mPaintAlpha;
    private float mPaintWidth;

    private ResizeBehaviour mResizeBehaviour;

    private int mLastDimensionW;
    private int mLastDimensionH;

    public FreeDrawSerializableState(ArrayList<HistoryPath> canceledPaths,
                                     ArrayList<HistoryPath> paths, int paintColor, int paintAlpha,
                                     float paintWidth, ResizeBehaviour resizeBehaviour,
                                     int lastW, int lastH) {

        setCanceledPaths(canceledPaths != null ? canceledPaths : new ArrayList<HistoryPath>());
        setPaths(paths != null ? paths : new ArrayList<HistoryPath>());
        setPaintWidth(paintWidth >= 0 ? paintWidth : 0);
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

    public float getPaintWidth() {
        return mPaintWidth;
    }

    public void setPaintWidth(float paintWidth) {
        this.mPaintWidth = paintWidth;
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

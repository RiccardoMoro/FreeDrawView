package com.rm.freedrawview;

import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Riccardo Moro on 11/4/2016.
 */

class FreeDrawSavedState extends View.BaseSavedState {

    private ArrayList<HistoryPath> mPaths = new ArrayList<>();
    private ArrayList<HistoryPath> mCanceledPaths = new ArrayList<>();

    private int mPaintColor;
    private int mPaintAlpha;
    private float mPaintWidth;

    private ResizeBehaviour mResizeBehaviour;

    private int mLastDimensionW;
    private int mLastDimensionH;

    FreeDrawSavedState(Parcelable superState, ArrayList<HistoryPath> paths,
                       ArrayList<HistoryPath> canceledPaths, float paintWidth,
                       int paintColor, int paintAlpha, ResizeBehaviour resizeBehaviour,
                       int lastDimensionW, int lastDimensionH) {
        super(superState);

        mPaths = paths;
        mCanceledPaths = canceledPaths;
        mPaintWidth = paintWidth;

        mPaintColor = paintColor;
        mPaintAlpha = paintAlpha;

        mResizeBehaviour = resizeBehaviour;

        mLastDimensionW = lastDimensionW;
        mLastDimensionH = lastDimensionH;
    }

    ArrayList<HistoryPath> getPaths() {
        return mPaths;
    }

    ArrayList<HistoryPath> getCanceledPaths() {
        return mCanceledPaths;
    }

    @ColorInt
    int getPaintColor() {
        return mPaintColor;
    }

    @IntRange(from = 0, to = 255)
    int getPaintAlpha() {
        return mPaintAlpha;
    }

    float getCurrentPaintWidth() {
        return mPaintWidth;
    }

    Paint getCurrentPaint() {

        Paint paint = FreeDrawHelper.createPaint();
        FreeDrawHelper.setupStrokePaint(paint);
        FreeDrawHelper.copyFromValues(paint, mPaintColor, mPaintAlpha, mPaintWidth, true);
        return paint;
    }

    ResizeBehaviour getResizeBehaviour() {
        return mResizeBehaviour;
    }

    int getLastDimensionW() {
        return mLastDimensionW;
    }

    int getLastDimensionH() {
        return mLastDimensionH;
    }

    // Parcelable stuff
    private FreeDrawSavedState(Parcel in) {
        super(in);

        in.readTypedList(mPaths, HistoryPath.CREATOR);
        in.readTypedList(mCanceledPaths, HistoryPath.CREATOR);

        mPaintColor = in.readInt();
        mPaintAlpha = in.readInt();
        mPaintWidth = in.readFloat();

        mResizeBehaviour = (ResizeBehaviour) in.readSerializable();

        mLastDimensionW = in.readInt();
        mLastDimensionH = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);

        out.writeTypedList(mPaths);
        out.writeTypedList(mCanceledPaths);

        out.writeInt(mPaintColor);
        out.writeInt(mPaintAlpha);
        out.writeFloat(mPaintWidth);

        out.writeSerializable(mResizeBehaviour);

        out.writeInt(mLastDimensionW);
        out.writeInt(mLastDimensionH);
    }

    // Parcelable CREATOR class, needed for parcelable to work
    public static final Parcelable.Creator<FreeDrawSavedState> CREATOR =
            new Parcelable.Creator<FreeDrawSavedState>() {
                public FreeDrawSavedState createFromParcel(Parcel in) {
                    return new FreeDrawSavedState(in);
                }

                public FreeDrawSavedState[] newArray(int size) {
                    return new FreeDrawSavedState[size];
                }
            };
}

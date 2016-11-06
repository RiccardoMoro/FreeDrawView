package com.rm.rmfreedraw;

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

    private ArrayList<HistoryPath> mCanceledPaths;
    private ArrayList<HistoryPath> mPaths;
    private SerializablePaint mCurrentPaint;

    private int mPaintColor;
    private int mPaintAlpha;

    private ResizeBehaviour mResizeBehaviour;

    private int mLastDimensionW;
    private int mLastDimensionH;

    FreeDrawSavedState(Parcelable superState, ArrayList<HistoryPath> paths,
                       ArrayList<HistoryPath> canceledPaths, SerializablePaint currentPaint,
                       int paintColor, int paintAlpha, ResizeBehaviour resizeBehaviour,
                       int lastDimensionW, int lastDimensionH) {
        super(superState);

        mPaths = paths;
        mCanceledPaths = canceledPaths;
        mCurrentPaint = currentPaint;

        mPaintColor = paintColor;
        mPaintAlpha = paintAlpha;

        mResizeBehaviour = resizeBehaviour;

        mLastDimensionW = lastDimensionW;
        mLastDimensionH = lastDimensionH;
    }

    private FreeDrawSavedState(Parcel in) {
        super(in);
        try {
            mPaths = in.readArrayList(HistoryPath.class.getClassLoader());
            mCanceledPaths = in.readArrayList(HistoryPath.class.getClassLoader());

            mPaintColor = in.readInt();
            mPaintAlpha = in.readInt();

            mResizeBehaviour = (ResizeBehaviour) in.readSerializable();

            mLastDimensionW = in.readInt();
            mLastDimensionH = in.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeTypedList(mPaths);
        out.writeTypedList(mCanceledPaths);

        out.writeInt(mPaintColor);
        out.writeInt(mPaintAlpha);

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

    ArrayList<HistoryPath> getPaths() {
        return mPaths;
    }

    ArrayList<HistoryPath> getCanceledPaths() {
        return mCanceledPaths;
    }

    SerializablePaint getCurrentPaint() {
        return mCurrentPaint;
    }

    @ColorInt
    int getPaintColor() {
        return mPaintColor;
    }

    @IntRange(from = 0, to = 255)
    int getPaintAlpha() {
        return mPaintAlpha;
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
}

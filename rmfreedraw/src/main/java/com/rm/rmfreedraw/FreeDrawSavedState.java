package com.rm.rmfreedraw;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Riccardo Moro on 11/4/2016.
 */

public class FreeDrawSavedState extends View.BaseSavedState {

    private ArrayList<HistoryPath> mPaths;
    private int mLastDimensionW;
    private int mLastDimensionH;

    public FreeDrawSavedState(Parcelable superState, ArrayList<HistoryPath> paths,
                              int lastDimensionW, int lastDimensionH) {
        super(superState);

        mPaths = paths;
        mLastDimensionW = lastDimensionW;
        mLastDimensionH = lastDimensionH;
    }

    private FreeDrawSavedState(Parcel in) {
        super(in);
        try {
            mPaths = in.readArrayList(HistoryPath.class.getClassLoader());
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

    public ArrayList<HistoryPath> getPaths() {
        return mPaths;
    }

    public int getLastDimensionW() {
        return mLastDimensionW;
    }

    public int getLastDimensionH() {
        return mLastDimensionH;
    }
}

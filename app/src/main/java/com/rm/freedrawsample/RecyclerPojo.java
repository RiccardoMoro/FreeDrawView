package com.rm.freedrawsample;

import android.os.Parcelable;

/**
 * Created by Riccardo on 01/12/16.
 */

public class RecyclerPojo {
    private Parcelable drawState;


    public Parcelable getDrawState() {
        return drawState;
    }

    public void setDrawState(Parcelable drawState) {
        this.drawState = drawState;
    }
}

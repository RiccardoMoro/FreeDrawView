package com.rm.freedrawview;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Riccardo Moro on 9/25/2016.
 */

class Point implements Parcelable, Serializable {

    static final long serialVersionUID = 42L;

    float x, y;

    Point() {
        x = y = -1;
    }

    @Override
    public String toString() {
        return "" + x + " : " + y + " - ";
    }


    // Parcelable stuff
    private Point(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(x);
        dest.writeFloat(y);
    }

    // Parcelable CREATOR class
    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };
}

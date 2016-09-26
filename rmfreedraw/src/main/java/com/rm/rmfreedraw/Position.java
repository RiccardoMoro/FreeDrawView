package com.rm.rmfreedraw;

/**
 * Created by Riccardo Moro on 9/10/2016.
 */
public class Position {
    private float mX;
    private float mY;

    public Position(float x, float y) {
        this.mX = x;
        this.mY = y;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public void setY(float y) {
        this.mY = y;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }
}

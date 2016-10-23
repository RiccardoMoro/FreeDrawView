package com.rm.rmfreedraw;

import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;

/**
 * Created by Riccardo Moro on 9/27/2016.
 */

public class HistoryPath {
    private Path path;
    private Paint paint;
    private float originX, originY;
    private boolean isPoint;

    public HistoryPath(@NonNull Path path, @NonNull Paint paint, float originX, float originY,
                       boolean isPoint) {
        this.path = path;
        this.paint = paint;
        this.originX = originX;
        this.originY = originY;
        this.isPoint = isPoint;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isPoint() {
        return isPoint;
    }

    public void setPoint(boolean point) {
        isPoint = point;
    }

    public float getOriginX() {
        return originX;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public float getOriginY() {
        return originY;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }
}

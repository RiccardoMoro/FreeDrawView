package com.rm.rmfreedraw;

import android.graphics.Matrix;

/**
 * Created by Riccardo Moro on 11/5/2016.
 */

public class TranslateMatrix extends Matrix {
    public TranslateMatrix(float dx, float dy) {
        super();
        setScale(dx, dy);
    }

    public TranslateMatrix(Matrix matrix) {
        super(matrix);
    }
}

package com.rm.rmfreedraw;

import android.graphics.Matrix;

/**
 * Created by Riccardo Moro on 11/5/2016.
 */

class TranslateMatrix extends Matrix {
    TranslateMatrix(float dx, float dy) {
        super();
        setScale(dx, dy);
    }

    TranslateMatrix(Matrix matrix) {
        super(matrix);
    }
}

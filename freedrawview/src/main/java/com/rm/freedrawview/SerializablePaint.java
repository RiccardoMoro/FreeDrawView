package com.rm.freedrawview;

import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by Riccardo Moro on 11/4/2016.
 */

class SerializablePaint extends Paint implements Serializable {
    SerializablePaint(int flags) {
        super(flags);
    }

    SerializablePaint(SerializablePaint paint) {
        super(paint);
    }
}

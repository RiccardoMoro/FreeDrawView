package com.rm.rmfreedraw;

import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by Riccardo Moro on 11/4/2016.
 */

public class SerializablePaint extends Paint implements Serializable {
    public SerializablePaint(int flags) {
        super(flags);
    }

    public SerializablePaint(SerializablePaint paint) {
        super(paint);
    }
}

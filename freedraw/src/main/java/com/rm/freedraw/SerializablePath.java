package com.rm.freedraw;

import android.graphics.Path;

import java.io.Serializable;

/**
 * Created by Riccardo Moro on 11/4/2016.
 */

class SerializablePath extends Path implements Serializable {
    SerializablePath() {
        super();
    }

    SerializablePath(SerializablePath path) {
        super(path);
    }
}

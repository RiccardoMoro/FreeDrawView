package com.rm.rmfreedraw;

import android.graphics.Path;

import java.io.Serializable;

/**
 * Created by Riccardo Moro on 11/4/2016.
 */

public class SerializablePath extends Path implements Serializable {
    public SerializablePath() {
        super();
    }

    public SerializablePath(SerializablePath path) {
        super(path);
    }
}

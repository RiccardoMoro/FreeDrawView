package com.rm.freedrawsample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by Riccardo Moro on 10/23/2016.
 */

public class ColorHelper {

    @ColorInt
    public static int getRandomMaterialColor(@NonNull Context context) {
        TypedArray colors = context.getResources().obtainTypedArray(R.array.material_colors);
        int index = (int) (Math.random() * colors.length());
        int color = colors.getColor(index, Color.BLACK);
        colors.recycle();
        return color;
    }
}

package com.rm.rmfreedraw;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Riccardo Moro on 10/23/2016.
 */

public class FreeDrawHelper {

    /**
     * Function used to check whenever a list of points is a line or a path to draw
     */
    static boolean isAPoint(@NonNull List<Point> points) {
        if (points.size() == 0)
            return false;

        if (points.size() == 1)
            return true;

        for (int i = 1; i < points.size(); i++) {
            if (points.get(i - 1).x != points.get(i).x || points.get(i - 1).y != points.get(i).y)
                return false;
        }

        return true;
    }

    /**
     * Converts a given dp number to it's pixel corresponding number
     */
    public static float convertDpToPixels(float dp) {
        return (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Converts a given pixel number to it's dp corresponding number
     */
    public static float convertPixelsToDp(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }
}

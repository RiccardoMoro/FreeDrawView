package com.rm.freedrawsample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Riccardo on 30/05/2017.
 */

class IntentHelper {

    public static void openUrl(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));

            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.error_opening_url, Toast.LENGTH_LONG).show();
        }
    }
}

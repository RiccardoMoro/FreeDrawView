package com.rm.freedrawsample;

import android.content.Context;
import android.support.annotation.Nullable;

import com.rm.freedrawview.FreeDrawSerializableState;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Riccardo on 23/05/2017.
 */

public class FileHelper {

    private static final String FILE_NAME = "draw_state.ser";

    public static void saveStateIntoFile(Context context, FreeDrawSerializableState state) {

        if (context != null && state != null) {

            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                ObjectOutputStream os = new ObjectOutputStream(fos);
                os.writeObject(state);
                os.flush();
                fos.flush();
                os.close();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();

                if (fos != null) {

                    try {
                        fos.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    @Nullable
    public static FreeDrawSerializableState getSavedStoreFromFile(Context context) {

        if (context != null) {

            FileInputStream fis = null;
            try {
                fis = context.openFileInput(FILE_NAME);
                ObjectInputStream is = new ObjectInputStream(fis);
                FreeDrawSerializableState state = (FreeDrawSerializableState) is.readObject();

                fis.close();
                is.close();

                return state;
            } catch (Exception e) {
                e.printStackTrace();

                if (fis != null) {

                    try {
                        fis.close();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        return null;
    }
}

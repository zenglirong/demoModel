package com.iflytek.raw;

import static android.os.SystemClock.sleep;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyAssets {

    public static boolean copyFilesFromAssets(Context context, String file, String name) {
        sleep(1000);
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(file);
            File f = new File("system/bin"+ "/" + name);
            f.createNewFile();
            FileOutputStream fos = new FileOutputStream(f);
            byte[] temp = new byte[1024];
            int i;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }
}

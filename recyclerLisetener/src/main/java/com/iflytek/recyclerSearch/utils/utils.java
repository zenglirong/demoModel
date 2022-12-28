package com.iflytek.recyclerSearch.utils;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;

import android.widget.Toast;

import com.iflytek.recyclerSearch.MainActivity;

public class utils {

    public static void uiToast(String string) {

        runOnUiThread(() -> Toast.makeText(MainActivity.mContext, string, Toast.LENGTH_SHORT).show());
    }
}

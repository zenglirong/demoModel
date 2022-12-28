package com.iflytek.recyclerSearch.utils;

import static com.blankj.utilcode.util.ThreadUtils.runOnUiThread;
import static com.iflytek.recyclerSearch.MainActivity.getContext;

import android.widget.Toast;

public class launchApp {

    public static void uiToast(String string) {
        runOnUiThread(() -> Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show());
    }
}

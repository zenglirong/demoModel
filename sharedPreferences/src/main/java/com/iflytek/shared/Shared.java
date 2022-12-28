package com.iflytek.shared;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Shared {

    private static SharedPreferences sharedPreferences;

    @SuppressLint("WrongConstant")
    public static void initShared(Context context) {
        sharedPreferences = context.getSharedPreferences("test", MODE_PRIVATE);//第一个参数是存储文件，第二个是存储方式
    }

    public static int readSharedInt(String key) {
        //获取App打开次数
        return sharedPreferences.getInt(key, 0);
    }

    public static void writeSharedInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static String readSharedString(String key) {
        //获取App打开次数
        return sharedPreferences.getString(key, "");
    }

    public static void writeSharedString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }


    public static void writeDeleteString(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

}

package com.iflytek.raw;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Adb {
    public static String execShellStr(String cmd) {
        Log.d("TAG", "execShellStr: " + cmd);
        String[] cmdStrings = new String[]{
                "sh", "-c", cmd
        };
        String retString = "";
        try {
            Process process = Runtime.getRuntime().exec(cmdStrings);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(
                    process.getInputStream()), 7777);
            BufferedReader stderr = new BufferedReader(new InputStreamReader(
                    process.getErrorStream()), 7777);
            String line = null;
            while ((null != (line = stdout.readLine()))
                    || (null != (line = stderr.readLine()))) {
                if ("" != line) {
                    retString += line + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retString;
    }
}

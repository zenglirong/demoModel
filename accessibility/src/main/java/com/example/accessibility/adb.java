package com.example.accessibility;

import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class adb {


    public static String shell(String cmd) {
        BufferedReader reader;
        String content = "";
        try {
            //打印进程信息，不过滤任何条件
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            int read;
            char[] buffer = new char[4096];
            long i = SystemClock.elapsedRealtime();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
                if (SystemClock.elapsedRealtime() - i >= 2000) {
                    break;
                }
            }
            reader.close();
            content = output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

package com.example.accessibility;

import static com.example.accessibility.AccessibilityService.isStart;
import static com.example.accessibility.LunchApp.startApkName;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    public static Context getContext() {
        return context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        if (!isStart()) {   // 无障碍权限动态加载
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }
    }


    public void onClick(View view) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                Log.i("TAG", "start click");


//                AutoTouch autoTouch = new AutoTouch();
//
//                autoTouch.autoClickPos(MainActivity.this, 1000, 2000);
//                sleep(1);
//                autoPos(1000,2000);
//                shell1(new String[]{"input", "tap", "" + 1000, "" + 2000});
                startApkName("今日头条极速版");

            }
        }).start();
    }


    public void setMouseClick(int x, int y) {
        MotionEvent evenDownt = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_DOWN, x, y, 0);
        dispatchTouchEvent(evenDownt);
        MotionEvent eventUp = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_UP, x, y, 0);
        dispatchTouchEvent(eventUp);
        evenDownt.recycle();
        eventUp.recycle();
    }

    public void setMouseClick1(int x, int y) {
// 可以不用在 Activity 中增加任何处理，各 Activity 都可以响应
        Instrumentation inst = new Instrumentation();
        inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, x, y, 0));
        inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP, x, y, 0));
    }




}

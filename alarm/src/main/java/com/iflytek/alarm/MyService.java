package com.iflytek.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class MyService extends Service {
    private static final String TAG = "MyService";

    public MyService() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
            }
        }).start();
        AlarmManagerUtils.getInstance(getApplicationContext()).getUpAlarmManagerWorkOnOthers();
        return super.onStartCommand(intent, flags, startId);
    }
}
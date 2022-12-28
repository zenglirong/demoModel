package com.iflytek.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "指定广播: " + intent.getStringExtra("value"), Toast.LENGTH_SHORT).show();

    }
}

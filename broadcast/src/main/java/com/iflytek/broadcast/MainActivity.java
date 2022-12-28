package com.iflytek.broadcast;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("TAG", "竟然可以解锁");
                Toast.makeText(context, "竟然可以解锁", Toast.LENGTH_SHORT).show();

            }
        }, new IntentFilter(Intent.ACTION_USER_PRESENT));// 系统解锁广播

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "自定义广播: " + intent.getStringExtra("value"), Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter("自定义"));
    }
    
    public void 指定广播(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.iflytek.broadcast", "com.iflytek.broadcast.MyReceiver"));
        // 上面这一行在Android 7.0及以下版本不是必须的，但是Android 8.0或者更高版本，发送广播的条件更加严苛，必须添加这一行内容。
        // 创建的ComponentName实例化对象有两个参数，第1个参数是指接收广播类的包名，第2个参数是指接收广播类的完整类名。
        intent.putExtra("value", "" + count++);// 广播带参数
        sendBroadcast(intent);
    }


    public void 自定义广播(View view) {
        Intent intent = new Intent("自定义");
        intent.putExtra("value", "" + count++);// 广播带参数
        sendBroadcast(intent);
    }

}
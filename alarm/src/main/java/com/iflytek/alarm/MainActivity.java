package com.iflytek.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AlarmManagerUtils alarmManagerUtils;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmManagerUtils = AlarmManagerUtils.getInstance(this);
        alarmManagerUtils.createGetUpAlarmManager();

        Button button = findViewById(R.id.am);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManagerUtils.getUpAlarmManagerStartWork();
                Toast.makeText(getApplicationContext(),"设置成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
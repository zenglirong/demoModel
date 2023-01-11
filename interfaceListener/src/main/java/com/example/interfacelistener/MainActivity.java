package com.example.interfacelistener;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements Timer.OnClickItemListener {

    private static final String TAG = "MainActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer timer = new Timer();
        timer.OnClickItemListener(this);
    }

    @Override
    public void onClick(int position) {
        Log.i(TAG, "onClick count: " + position);
    }
}
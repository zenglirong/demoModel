package com.iflytek.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    View sView ;
    SeekBar seekBar1;
    SeekBar seekBar2;
    SeekBar seekBar3;
    RingView ringView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        seekBar1 = findViewById(R.id.seekBarId1);
        seekBar2 = findViewById(R.id.seekBarId2);
        seekBar3 = findViewById(R.id.seekBarId3);

        seekBar1.setOnSeekBarChangeListener(this);

        seekBar1.setProgress(20);
        seekBar2.setProgress(30);
        seekBar3.setProgress(50);

        setPercentNum(8,4,2);
    }

    public void setPercentNum(int success, int fail, int skip) {

        int allValue = success + fail + skip;
        float percent = 100f / allValue;

        float percentSuccess = success * percent;
        float percentFail = fail * percent;
        float percentSkip = skip * percent;

        // 添加的是颜色
        List<Integer> colorList = new ArrayList<>();
        colorList.add(0xff00aa00);
        colorList.add(0xFFff0000);
        colorList.add(0xFFdddd00);

        //  添加的是百分比
        List<Float> rateList = new ArrayList<>();
        rateList.add((float) ((float) (Math.round(percentSuccess * 10)) / 10));
        rateList.add((float) ((float) (Math.round(percentFail * 10)) / 10));
        rateList.add((float) ((float) (Math.round(percentSkip * 10)) / 10));

        ringView = findViewById(R.id.ringView);
        ringView.setShow(colorList, rateList, true, true);
        Log.i("TAG", "success:" + percentSuccess +  " fail:" +percentFail + " skip:" + percentSkip);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        setPercentNum(seekBar.getProgress(), 100-seekBar.getProgress(),0);
    }
}
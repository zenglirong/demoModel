package com.iflytek.audio;

import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.iflytek.audio.run.AudioRecord;
import com.iflytek.audio.run.Wave;
import com.iflytek.audio.view.WaveView;


public class MainActivity extends AppCompatActivity {

    private WaveView wave_view1;
    private WaveView wave_view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        new AudioRecord(wave_view1);
        new Wave(wave_view2, 0);
        wave_view2.setWaveWidth(1);
//        new WaveUtil(wave_view2);

    }

    void init() {
        wave_view1 = findViewById(R.id.wave_view1);
        wave_view2 = findViewById(R.id.wave_view2);
        SeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.i("TAG", "onProgressChanged:" + i);
                if (i == 0) return;
                wave_view1.setWaveWidth(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
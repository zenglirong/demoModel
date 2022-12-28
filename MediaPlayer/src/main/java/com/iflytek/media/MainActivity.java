package com.iflytek.media;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button button;

    MediaPlayer mediaPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button_start);
        mediaPlayer = new MediaPlayer();
    }

    private final String FILE_PATH = Environment.getExternalStorageDirectory() + "/sound.mp3";

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                mediaPlayer.play(this, R.raw.sound);
                break;
            case R.id.button_stop:
                mediaPlayer.stop();
                break;
        }
    }


    @Override
    protected void onStart() {
        mediaPlayer.start();
        super.onStart();
    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mediaPlayer.stop();
        super.onDestroy();
    }
}
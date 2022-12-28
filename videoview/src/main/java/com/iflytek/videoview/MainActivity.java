package com.iflytek.videoview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.play).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.replay).setOnClickListener(this);

        videoView = findViewById(R.id.video);
        videoView.setOnCompletionListener((MediaPlayer mp) -> {
            mp.start();  // 监听播放完成广播，实现自动重播
            mp.setLooping(true);
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initVideoPath();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.suspend();
        }
    }

    private void initVideoPath() {
        File file = new File(Environment.getExternalStorageDirectory(), "/DCIM/Camera/环塔.mp4");
        videoView.setVideoPath(file.getPath());                 //指定视频文件路径
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play:
                if (!videoView.isPlaying()) {
                    videoView.start();                          //开始播放
                }
                break;
            case R.id.pause:
                if (videoView.isPlaying()) {
                    videoView.pause();                          //暂停播放
                }
                break;
            case R.id.replay:
                if (videoView.isPlaying()) {
                    videoView.resume();                         //重新播放
                }
                break;
        }
    }
}
package com.iflytek.media;

import android.content.Context;
import android.media.AudioManager;

import java.io.File;

public class MediaPlayer {

    private android.media.MediaPlayer mediaPlayer;

    public void play(String filePath) {
        try {
            File file = new File(filePath);    //确认音乐文件的存在
            if (!file.exists()) return;
            mediaPlayer = new android.media.MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();     //异步的方式加载音乐文件
            //异步加载完音乐文件后会回调此
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();        //准备好之后才能start
            });
            mediaPlayer.setOnCompletionListener(mp -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(Context context, int filePath) {
        if (mediaPlayer == null) {
            mediaPlayer = new android.media.MediaPlayer();
            mediaPlayer = android.media.MediaPlayer.create(context, filePath);
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void start() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

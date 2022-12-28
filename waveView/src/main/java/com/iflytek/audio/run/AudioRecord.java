package com.iflytek.audio.run;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.util.Log;

import com.iflytek.audio.view.WaveView;

public class AudioRecord {
    private static final String TAG = "AudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 16000;
    static final int BUFFER_SIZE = android.media.AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    android.media.AudioRecord mAudioRecord;
    boolean isGetVoiceRun;
    final Object mLock;

    @SuppressLint("MissingPermission")
    public AudioRecord(WaveView sloopView) {
        mLock = new Object();

        if (isGetVoiceRun) {
            Log.e(TAG, "还在录着呢");
            return;
        }
        mAudioRecord = new android.media.AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
        isGetVoiceRun = true;

        new Thread(() -> {
            mAudioRecord.startRecording();
            short[] buffer = new short[BUFFER_SIZE];
            float[] data = new float[BUFFER_SIZE];
            while (isGetVoiceRun) {
                //r是实际读取的数据长度，一般而言r会小于buffer size
                int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                for (int i = 0; i < r; i++) data[i] = (float) buffer[i] / 1000;
                sloopView.showLines(data);
                // 大概一秒十次
                synchronized (mLock) {
                    try {
                        mLock.wait(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }).start();
    }


    void stopNoiseLevel() {
        isGetVoiceRun = false;
    }
}

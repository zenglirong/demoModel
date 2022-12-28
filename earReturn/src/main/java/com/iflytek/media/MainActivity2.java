package com.iflytek.media;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.LinkedList;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ktv";
    /**
     * 按钮
     */
    private Button bt_exit;
    /**
     * AudioRecord 写入缓冲区大小
     */
    protected int m_in_buf_size;
    /**
     * 录制音频对象
     */
    private AudioRecord m_in_rec;
    /**
     * 录入的字节数组
     */
    private byte[] m_in_bytes;
    /**
     * 存放录入字节数组的大小
     */
    private LinkedList<byte[]> m_in_q;
    /**
     * AudioTrack 播放缓冲大小
     */
    private int m_out_buf_size;
    /**
     * 播放音频对象
     */
    private AudioTrack m_out_trk;
    /**
     * 播放的字节数组
     */
    private byte[] m_out_bytes;
    /**
     * 录制音频线程
     */
    private Thread record;
    /**
     * 播放音频线程
     */
    private Thread play;
    /**
     * 让线程停止的标志
     */
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("音频回路");

        requestAllPower();
        Log.d("dfdfd", "333333333333");

        init();
        record = new Thread(new recordSound());
        play = new Thread(new playRecord());
        // 启动录制线程
        record.start();
        // 启动播放线程
        play.start();
    }

    @SuppressLint("MissingPermission")
    private void init() {
//        bt_exit = findViewById(R.id.bt_yinpinhuilu_testing_exit);
        Log.i(TAG, "bt_exit====" + bt_exit);

        bt_exit.setOnClickListener(this);

        // AudioRecord 得到录制最小缓冲区的大小
        m_in_buf_size = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
        // 实例化播放音频对象
        m_in_rec = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, m_in_buf_size);
        // 实例化一个字节数组，长度为最小缓冲区的长度
        m_in_bytes = new byte[m_in_buf_size];
        // 实例化一个链表，用来存放字节组数
        m_in_q = new LinkedList<byte[]>();

        // AudioTrack 得到播放最小缓冲区的大小
        m_out_buf_size = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
        // 实例化播放音频对象
        m_out_trk = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, m_out_buf_size,
                AudioTrack.MODE_STREAM);
        // 实例化一个长度为播放最小缓冲大小的字节数组
        m_out_bytes = new byte[m_out_buf_size];
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
//        if (v.getId() == R.id.bt_yinpinhuilu_testing_exit) {
//            flag = false;
//            m_in_rec.stop();
//            m_in_rec = null;
//            m_out_trk.stop();
//            m_out_trk = null;
//            this.finish();
//        }
    }

    /**
     * 录音线程
     */
    class recordSound implements Runnable {
        @Override
        public void run() {
            Log.i(TAG, "........recordSound run()......");
            byte[] bytes_pkg;
            // 开始录音
            m_in_rec.startRecording();

            while (flag) {
                m_in_rec.read(m_in_bytes, 0, m_in_buf_size);
                bytes_pkg = m_in_bytes.clone();
                Log.i(TAG, "........recordSound bytes_pkg==" + bytes_pkg.length);
                if (m_in_q.size() >= 2) {
                    m_in_q.removeFirst();
                }
                m_in_q.add(bytes_pkg);
            }
        }

    }

    /**
     * 播放线程
     */
    class playRecord implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.i(TAG, "........playRecord run()......");
            byte[] bytes_pkg = null;
            // 开始播放
            m_out_trk.play();

            while (flag) {
                try {
                    m_out_bytes = m_in_q.getFirst();
                    bytes_pkg = m_out_bytes.clone();
                    m_out_trk.write(bytes_pkg, 0, bytes_pkg.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 录音哦动态权限
     */
    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (String permission : permissions) {
                Toast.makeText(this, "" + "权限" + permission + "申请成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
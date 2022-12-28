package com.iflytek.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textViewId);

        new Thread(() -> {
            try {
                while (true) {
                    Log.i("lrzeng", "Server开始~~~监听~~~");
                    // accept方法会阻塞，直到有客户端与之建立连接
                    Socket socket = new ServerSocket(10000).accept();
//                    while (true) {
                        InputStream is = socket.getInputStream();
                        byte[] bytes = new byte[1024];
                        int len = is.read(bytes);
                        String out = new String(bytes, 0, len);
                        Log.i("lrzeng", "utf-8 编码：" + out + " len = " + len);
//                    }
                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
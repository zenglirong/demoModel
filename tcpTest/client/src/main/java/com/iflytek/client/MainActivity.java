package com.iflytek.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TCPClient();
    }

    protected void TCPClient() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("192.168.1.105", 10000);
                OutputStream os = socket.getOutputStream();

                os.write("你好服务器\n".getBytes());
//                while (true) {
                    InputStream is = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len = is.read(bytes);
                    String out = new String(bytes, 0, len);

                    Log.i("TAG", "utf-8 编码：" + out + " len = " + len);
                    socket.close();
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
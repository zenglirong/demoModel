package com.iflytek.ch340;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

@SuppressLint({"StaticFieldLeak", "SetTextI18n", "HandlerLeak"})
public class MainActivity extends Activity {
    UsbToUart usbToUart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (usbToUart == null) usbToUart = new UsbToUart(this);

        usbToUart.sendData("1234567890123");
        usbToUart.setOnReceiveListener(data -> {
            EditText readText = findViewById(R.id.ReadValues);
            readText.append(data);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        usbToUart.closeDevice();
        usbToUart = null;
        super.onDestroy();
    }
}
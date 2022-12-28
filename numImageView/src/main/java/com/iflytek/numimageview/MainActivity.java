package com.iflytek.numimageview;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NumImageView numImageView = (NumImageView) findViewById(R.id.num_iv_id);
//        numImageView.setColor(0xff00ff00);
//        numImageView.setNum(10);

    }
}
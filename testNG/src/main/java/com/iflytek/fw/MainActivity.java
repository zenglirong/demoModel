package com.iflytek.fw;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.iflytek.agaig.R;
import com.iflytek.fw.testCase.FrameworkAuto;
import com.iflytek.fw.testng.TestNg;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TestNg test = new TestNg();
        test.setClass(FrameworkAuto.class);
        test.setName(Arrays.asList("123", "789"));
        test.run();
        Log.i("zeng", "a=" + test.getResult().toString());
    }
}
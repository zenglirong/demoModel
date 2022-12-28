package com.iflytek.raw;

import static android.os.SystemClock.sleep;
import static com.iflytek.raw.CopyAssets.copyFilesFromAssets;
import static com.iflytek.raw.CopyRaw.copyFilesFromRaw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sleep(1000);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        copyFilesFromRaw(this, R.raw.gt, "GT7382_test_sensor_9.tporder", path);
        copyFilesFromRaw(this, R.raw.gt_mp_test, "gt_mp_test", "system/bin");
        copyFilesFromAssets(this, "gt_mp_test", "gt_mp_test");
    }
}
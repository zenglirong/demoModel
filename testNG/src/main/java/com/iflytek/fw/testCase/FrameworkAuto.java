package com.iflytek.fw.testCase;

import static android.os.SystemClock.sleep;

import android.util.Log;

import com.iflytek.fw.testng.AfterClass;
import com.iflytek.fw.testng.BeforeClass;
import com.iflytek.fw.testng.Test;

public class FrameworkAuto {

    @Test(description = "this", testName = "123", priority = 8, enabled = false)
    public void implA() {

        sleep(1000);
        Log.i("zeng", "Test A");
    }

    @Test(testName = "456", priority = 3, groups = "test")
    public String implB() {
        sleep(1000);
        Log.i("zeng", "Test B");
        return "B";
    }

    @Test(testName = "789", priority = 7, groups = {"test", "123"})
    public String implC() {
        sleep(1000);
        Log.i("zeng", "Test C");
        return "C";
    }

    @AfterClass
    public void AfterClass() {
        Log.i("zeng", "AfterClass");
    }

    @BeforeClass
    public void BeforeClass() {
        Log.i("zeng", "BeforeClass");
    }

}


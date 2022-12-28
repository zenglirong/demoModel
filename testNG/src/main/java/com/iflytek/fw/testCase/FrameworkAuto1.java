package com.iflytek.fw.testCase;

import android.util.Log;

import com.iflytek.fw.testng.AfterClass;
import com.iflytek.fw.testng.BeforeClass;
import com.iflytek.fw.testng.Test;

public class FrameworkAuto1 {

    @Test(testName = "123", priority = 8)
    public String implA() {
        return "A";
    }

    @Test(testName = "456", priority = 3)
    public String implB() {
        return "B";
    }

    @Test(testName = "789", priority = 7)
    public String implC() {
        return "C";
    }

    @AfterClass
    public void AfterClass() {
        Log.i("zeng", "AfterClass");
    }

    @BeforeClass
    public void BeforeClass(){
        Log.i("zeng", "BeforeClass");
    }

}


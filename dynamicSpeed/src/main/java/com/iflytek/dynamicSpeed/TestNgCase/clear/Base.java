package com.iflytek.dynamicSpeed.TestNgCase.clear;

import static com.iflytek.dynamicSpeed.utils.launchApp.uiToast;

import android.util.Log;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

//@Listeners(MyTestNgReporter.class)

public class Base {

    static  int count = 0;
    // 音量加减键
    @Test(description = "清理后台应用 Base")
    public void test_demo2() throws InterruptedException, IOException {

        uiToast("看看是否进入1 + " + (++count));
        Assert.assertTrue(false);
    }

    @BeforeClass
    public void BeforeClass() throws IOException, InterruptedException {
        Log.d("testNg", "BeforeClass");
    }

    @AfterClass
    public void AfterClass() {
        Log.d("testNg", "AfterClass");
    }

    @BeforeMethod
    public void BeforeMethod() {
//        执行每个测试方法之前都会执行该方法
        Log.d("testNg", "BeforeMethod");
    }

    @AfterMethod
    public void AfterMethod() {
//        执行每个测试方法之后都会执行该方法
        Log.d("testNg", "AfterMethod");
    }

}

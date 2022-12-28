package com.iflytek.dynamicSpeed.TestNgCase.web;

import static com.iflytek.dynamicSpeed.utils.launchApp.uiToast;

import android.util.Log;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

//@Listeners(MyTestNgReporter.class)

public class Base {

    // 音量加减键
    @Test(description = "网址黑名单 Base")
    public void test_demo2() throws InterruptedException, IOException {
        uiToast("看看是否进入4");
        assert "清理后台应用".equals("清理后台应用") : "Expected name Beust, for" + "清理后台应用";

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

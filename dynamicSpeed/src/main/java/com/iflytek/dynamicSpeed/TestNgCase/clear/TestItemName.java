package com.iflytek.dynamicSpeed.TestNgCase.clear;

import static com.iflytek.dynamicSpeed.utils.launchApp.uiToast;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestItemName {
    @Test(description = "清理后台应用")
    public void test_demo()  {
        uiToast("看看是否进入11");
        Assert.assertTrue(false);

    }
}

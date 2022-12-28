package com.iflytek.dynamicSpeed.TestNgCase.web;

import static com.iflytek.dynamicSpeed.utils.launchApp.uiToast;

import org.testng.annotations.Test;

public class TestItemName {
    @Test(description = "网址黑名单")
    public void test_demo()  {
        uiToast("看看是否进入44");
        assert "清理后台应用".equals("清理后台应用") : "Expected name Beust, for" + "清理后台应用";

    }
}

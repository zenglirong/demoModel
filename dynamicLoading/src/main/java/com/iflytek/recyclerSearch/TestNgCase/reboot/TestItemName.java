package com.iflytek.recyclerSearch.TestNgCase.reboot;

import static com.iflytek.recyclerSearch.utils.launchApp.uiToast;

import org.testng.annotations.Test;

public class TestItemName {
    @Test(description = "自启白名单")
    public void test_demo()  {
        uiToast("看看是否进入33");
        assert "清理后台应用".equals("清理后台应用") : "Expected name Beust, for" + "清理后台应用";

    }
}

package com.iflytek.automator2;

import android.os.Environment;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        String text = null;
        String id = null;
        String desc = null;
        String time = null;

        String cmd = readFile("/命令.txt");
        try {
            JSONObject obj = new JSONObject(cmd);
            if (obj.toString().contains("time")) time = obj.getString("time");
            if (obj.toString().contains("text")) text = obj.getString("text");
            if (obj.toString().contains("id")) id = obj.getString("id");
            if (obj.toString().contains("desc")) desc = obj.getString("desc");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("ExampleInstrumentedTest", "time" + time + "   text:" + text + "   id:" + id + "   desc:" + desc);

        if (text != null) {
            UiObject2 uiObject = mDevice.findObject(By.text(text));
            if (uiObject != null) {
                text = uiObject.getText();
                uiObject.click();
                saveFile("结果.txt", JsonDataGroup(time, text, id, desc).toString());
            }
        }
        if (id != null) {
            UiObject2 uiObject = mDevice.findObject(By.res(id));
            if (uiObject != null) {
                text = uiObject.getText();
                id = uiObject.getResourceName();
                uiObject.click();
                saveFile("结果.txt", JsonDataGroup(time, text, id, desc).toString());
            }
        }
    }

    public static JSONObject JsonDataGroup(String time, String text, String id, String desc) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (time != null) jsonObject.put("time", time);
            if (text != null) jsonObject.put("text", text);
            if (id != null) jsonObject.put("id", id);
            if (desc != null) jsonObject.put("desc", desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    void saveFile(String name, String text) {
        try {
            // 新建文件
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/", name);
            if (file.exists()) {
                if (!file.delete()) return;
                if (!file.createNewFile()) return;
            }
            // 写文件
            FileWriter fw = new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + name);
            fw.write(text);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String readFile(String name) {
        try {
            FileReader fr = new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + name);
            BufferedReader r = new BufferedReader(fr);
            return r.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}



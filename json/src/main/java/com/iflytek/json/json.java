package com.iflytek.json;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class json {

    public static void jsonTest() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("test", "禁止应用启动");
            jsonObject.put("result", "pass");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray();

        jsonArray.put(jsonObject);
        jsonArray.put(jsonObject);
        jsonArray.put(jsonObject);

        JSONArray jsonArrayAdd = new JSONArray();

        jsonArrayAdd.put(jsonArray);
        jsonArrayAdd.put(jsonArray);

        Log.i("TAG", "json = " + jsonArrayAdd.toString());

        try {
            JSONArray jsonArr = new JSONArray();
            jsonArr = jsonArrayAdd.getJSONArray(1);

            JSONObject jsonAar2 = jsonArr.getJSONObject(1);

            String value = jsonAar2.getString("test");

            Log.i("TAG", "jsonAar2 = " + jsonAar2);
            Log.i("TAG", "value = " + value);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

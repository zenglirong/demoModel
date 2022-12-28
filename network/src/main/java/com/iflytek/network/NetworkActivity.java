package com.iflytek.network;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 *
 * @author Conquer
 * @version 1.0
 */
public class NetworkActivity extends Activity implements View.OnClickListener {

    private TextView mTextView;
    private Button mButton;
    private static final String TAG = "NetworkActivity";
    private String mResult;
    private Button mParseDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        findViews();
        setListeners();

    }

    private void findViews() {
        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.getButton);
        mParseDataButton = findViewById(R.id.parseDataButton);
    }

    private void setListeners() {
        mButton.setOnClickListener(this);
        mParseDataButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getButton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String urlString = "http://www.imooc.com/api/teacher?type=2&page=1";
                        mResult = requestDataByGet(urlString);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mResult = decode(mResult);
                                mTextView.setText(mResult);
                            }
                        });
                    }
                }).start();
                break;

            case R.id.parseDataButton:
                if (!TextUtils.isEmpty(mResult)) {
                    handleJSONData(mResult);
                }
                break;
        }
    }

    private void handleJSONData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            LessonResult lessonResult = new LessonResult();
            List<LessonResult.Lesson> lessonList = new ArrayList<>();
            int status = jsonObject.getInt("status");
            JSONArray lessons = jsonObject.getJSONArray("data");
            if (lessons != null && lessons.length() > 0) {
                for (int index = 0; index < lessons.length(); index++) {
                    JSONObject item = (JSONObject) lessons.get(0);
                    int id = item.getInt("id");
                    String name = item.getString("name");
                    String smallPic = item.getString("picSmall");
                    String bigPic = item.getString("picBig");
                    String description = item.getString("description");
                    int learner = item.getInt("learner");

                    LessonResult.Lesson lesson = new LessonResult.Lesson();
                    lesson.setID(id);
                    lesson.setName(name);
                    lesson.setSmallPictureUrl(smallPic);
                    lesson.setBigPictureUrl(bigPic);
                    lesson.setDescription(description);
                    lesson.setLearnerNumber(learner);
                    lessonList.add(lesson);
                }
                lessonResult.setStatus(status);
                lessonResult.setLessons(lessonList);
                mTextView.setText("data is : " + lessonResult.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String requestDataByGet(String urlString) {
        String result = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("GET");  // GET POST
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                result = streamToString(inputStream);
            } else {
                String responseMessage = connection.getResponseMessage();
                Log.e(TAG, "requestDataByPost: " + responseMessage);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return 字符串
     */
    public String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    /**
     * 将Unicode字符转换为UTF-8类型字符串
     */
    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuilder retBuf = new StringBuilder();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5)
                        && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr
                        .charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else {
                    retBuf.append(unicodeStr.charAt(i));
                }
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }

    private String requestDataByPost(String urlString) {
        String result = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setRequestMethod("POST");

            // 设置运行输入,输出:
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // Post方式不能缓存,需手动设置为false
            connection.setUseCaches(false);
            connection.connect();

            // 我们请求的数据:
            String data = "username=" + URLEncoder.encode("imooc", "UTF-8")
                    + "&number=" + URLEncoder.encode("15088886666", "UTF-8");
            // 获取输出流
            OutputStream out = connection.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                Reader reader = new InputStreamReader(inputStream, "UTF-8");
                char[] buffer = new char[1024];
                reader.read(buffer);
                result = new String(buffer);
                reader.close();
            } else {
                String responseMessage = connection.getResponseMessage();
                Log.e(TAG, "requestDataByPost: " + responseMessage);
            }

            final String finalResult = result;
            runOnUiThread(() -> mTextView.setText(finalResult));

            connection.disconnect();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

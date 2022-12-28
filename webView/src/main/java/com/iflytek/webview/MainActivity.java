package com.iflytek.webview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iflytek.test_web.R;


public class MainActivity extends AppCompatActivity {

    private WebView mwebview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mwebview = (WebView) findViewById(R.id.id_webview);

        WebSettings webSettings = mwebview.getSettings();
        webSettings.setDomStorageEnabled(true);

        mwebview.getSettings().setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞

        mwebview.loadUrl("https://www.baidu.com");              //加载需要显示的网页
        mwebview.setWebViewClient(new HelloWebViewClient());    //设置Web视图
    }

    void SendBroadcastData(String data) {

        Intent intent = new Intent("web");
        intent.putExtra("state", data);// 广播带参数
        sendBroadcast(intent);
    }

    private BroadcastReceiver loginrevcer = new BroadcastReceiver() {// 接受B apk发过来的广播
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String https = intent.getStringExtra("https");// B APK 发送过来的数据

            mwebview.loadUrl("https://" + https);              //加载需要显示的网页
            mwebview.setWebViewClient(new HelloWebViewClient());    //设置Web视图
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mwebview.canGoBack()) {
            mwebview.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return false;
    }

    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        //作用：打开网页时不调用系统浏览器， 而是在本WebView中显示；
        // 在网页上的所有加载都经过这个方法,这个函数我们可以做很多操作。
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(String.valueOf(request.getUrl()));
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //Toast.makeText(getApplicationContext(), "网页打开正确", Toast.LENGTH_SHORT).show();
            SendBroadcastData("网页打开正确");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //super.onReceivedError(view, request, error);
            Toast.makeText(getApplicationContext(), "网页打开失败", Toast.LENGTH_SHORT).show();
        }
    }

}
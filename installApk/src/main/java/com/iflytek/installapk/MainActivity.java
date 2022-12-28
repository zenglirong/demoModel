package com.iflytek.installapk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    final String installPath = "/sdcard/install/weixin.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StoragePermissions();
    }

    public void install(View view) {
        install(installPath);
    }

    public void copy(View view) {
        copyCheckSdcardFile("weixin.apk");
    }

    public void delete(View view) {
        String string = shell("rm -r /sdcard/install/");
        uiToast("install:" + string);
    }

    void StoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限则申请权限
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                //有权限直接执行,docode()不用做处理
            }
        } else {
            //小于6.0，不用申请权限，直接执行
        }
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //执行代码,这里是已经申请权限成功了,可以不用做处理
                } else {
                    Log.i("TAG", "权限申请失败");
                }
                break;
        }
    }

    private void install(String filePath) {
        Log.i("TAG", "开始执行安装: " + filePath);
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.w("TAG", "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.yunshuxie.main", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Log.w("TAG", "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }

    public void copyCheckSdcardFile(String apkName) {

        new Thread(() -> {
            String string = shell("ls /sdcard/install/");
            if (!string.contains(apkName))
            {
                uiToast( "复制 " + apkName + " 文件到 install 中");
                shell("mkdir /sdcard/install");
                if (copyApkFromAssets(getApplicationContext(), apkName,
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/install/" + apkName)) {
                    uiToast( "文件复制完成");
                }
            }
        }).start();
    }
    public void uiToast(String string) {
        runOnUiThread(() -> Toast.makeText(this, string, Toast.LENGTH_SHORT).show());
    }
    public static boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    public static String shell(String cmd) {
        BufferedReader reader = null;
        String content = "";
        try {
            //打印进程信息，不过滤任何条件
            Process process = Runtime.getRuntime().exec(cmd);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuffer output = new StringBuffer();
            int read;
            char[] buffer = new char[4096];
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            content = output.toString();
            Log.d("AdbRunner", content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }



}
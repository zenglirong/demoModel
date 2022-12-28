package com.iflytek.open;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        boolean haveInstallPermission = getPackageManager().canRequestPackageInstalls();
//        if (!haveInstallPermission) {
//            Uri packageURI = Uri.parse("package:" + getPackageName());
//            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
//            startActivityForResult(intent, 100);
//        }
    }

    private void installApk(String path) {
        File apk = new File(path);
        Uri uri = Uri.fromFile(apk);
        Intent intent = new Intent();
//        intent.setClassName("com.android.packageinstaller", "com.android.packageinstaller.PackageInstallerActivity");
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @SuppressLint("InlinedApi")
    public void onClick(View view) {
        installApk("sdcard/install/yunshuxie.apk");
    }
}
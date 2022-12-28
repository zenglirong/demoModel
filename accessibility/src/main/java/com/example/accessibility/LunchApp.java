package com.example.accessibility;

import static android.os.SystemClock.sleep;
import static com.example.accessibility.MainActivity.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class LunchApp {
    /**
     * @param name 启动应用 package
     */
    public static void startPackageName(String name) {
        sleep(1000);
        getContext().startActivity(getContext().getPackageManager().getLaunchIntentForPackage(name));
    }
    public static void startApkName(String name) {
        Context context = getContext();
        PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        @SuppressLint("QueryPermissionsNeeded")
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        if (packages != null) {
            ApplicationInfo applicationInfo;
            for (int i = 0; i < packages.size(); i++) {
                try {
                    String packName = packages.get(i).packageName;
                    applicationInfo = packageManager.getApplicationInfo(packName, PackageManager.GET_META_DATA);
                    if (packageManager.getApplicationLabel(applicationInfo).toString().equals(name)) { // 获取app name
                        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packName));
                        return;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

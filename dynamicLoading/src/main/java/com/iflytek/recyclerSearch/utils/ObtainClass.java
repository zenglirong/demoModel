package com.iflytek.recyclerSearch.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.DexFile;

public class ObtainClass {
    private final static String TAG = "TAG";

    /**
     * 判断指定字符串是否为空（null、空串、长度不大于0 均为空），为空则返回true，否则返回false
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (null == str || str.length() < 1) {
            return true;
        } else {
            return false;
        }
    }

    public static String getPackage(String str) {
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == '.') {
                return str.substring(0, i);
            }
        }
        return str;
    }

    /**
     * android扫描某个包下的所有子包
     *
     * @param context
     * @param entityPackage 包名（如：com.cmcc.test中的test）
     * @return
     */
    public static List<String> getChildPackage(Context context, String entityPackage) {

        Set<String> filePathList = new HashSet<>();
        try {
            DexFile dex = new DexFile(context.getPackageResourcePath());
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement();

                if (entryName.contains(entityPackage)) {
                    String packageName = getPackage(entryName);
                    filePathList.add(packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> res = new ArrayList<>(filePathList);
        Collections.sort(res);
        return res;
    }

    /**
     * android扫描某个包下的所有class文件
     *
     * @param context
     * @param entityPackage 包名（如：com.cmcc.test）
     * @return
     */
    public static List<Class<?>> getTestClass(Context context, String entityPackage) {

        List<Class<?>> classes = new ArrayList<>();
        try {
            DexFile dex = new DexFile(context.getPackageResourcePath());
            Enumeration<String> entries = dex.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement();
                if (entryName.contains(entityPackage)) {
                    Class<?> entryClass = Class.forName(entryName);
                    classes.add(entryClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }
}
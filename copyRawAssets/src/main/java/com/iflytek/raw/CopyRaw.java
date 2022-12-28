package com.iflytek.raw;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyRaw {

    /**
     * 复制res/raw中的文件到指定目录
     *
     * @param context     上下文
     * @param id          资源ID
     * @param fileName    文件名
     * @param storagePath 目标文件夹的路径
     */
    public static void copyFilesFromRaw(Context context, int id, String fileName, String storagePath) {
        InputStream inputStream = context.getResources().openRawResource(id);
        File file = new File(storagePath);
        if (!file.exists()) {//如果文件夹不存在，则创建新的文件夹
            file.mkdirs();
        }
        final String SEPARATOR = File.separator;//路径分隔符

        // 读取输入流中的数据写入输出流
        file = new File(storagePath + SEPARATOR + fileName);
        try {
            if (file.exists()) return;
            // 1.建立通道对象
            FileOutputStream fos = new FileOutputStream(file);
            // 2.定义存储空间
            byte[] buffer = new byte[inputStream.available()];
            // 3.开始读文件
            int length;
            while ((length = inputStream.read(buffer)) != -1) {// 循环从输入流读取buffer字节
                // 将Buffer中的数据写到outputStream对象中
                fos.write(buffer, 0, length);
            }
            fos.flush();// 刷新缓冲区
            // 4.关闭流
            fos.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

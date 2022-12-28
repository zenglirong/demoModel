package com.iflytek.filestore;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileSave {
    //写入文件,使用输出流
    public static void WriteFile(Context context, String fileName, String fileText) {
        // 文件输出流
        FileOutputStream fileOutputStream = null;
        try {
            // 私有模式创建文件
            fileOutputStream = context.openFileOutput(fileName, MODE_PRIVATE);
            //写入数据
            fileOutputStream.write(fileText.getBytes());
            fileOutputStream.flush();
            //关闭文件输出流
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //在finally中关闭流！因为如果找不到数据就会异常，我们也需要对其进行关闭
            try {
                if (fileOutputStream != null) {
                    //这里判断的目的是因为如果没有找到数据的时候，两种流就不会实例化，既然没有实例化则如果调用就会提示null
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //读取文件，使用输入流
    public static String ReadFile(Context context, String fileName) {
        // 文件输入流
        FileInputStream fileInputStream = null;
        // 字节输出流
        ByteArrayOutputStream stream = null;
        try {
            //获取指定文件的输入流
            fileInputStream = context.openFileInput(fileName);
            //实例化字节输出流
            stream = new ByteArrayOutputStream();
            //输入输出缓存
            byte[] buffer = new byte[1024];
            int length = -1;
            //读取文件输入流的内容到缓存中
            while ((length = fileInputStream.read(buffer)) != -1) {
                // 将缓存写入输入流
                stream.write(buffer, 0, length);
            }
            //关闭输出流
            stream.close();
            //关闭文件输入流
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }//无论是否异常都要关闭输入输出流
        finally {
            try {
                //输出流不为空，则关闭输出流
                if (stream != null) {
                    stream.close();
                    //输入流不为空，则关闭输入流
                } else if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (stream == null) return "";
        return Objects.requireNonNull(stream).toString();
    }
}

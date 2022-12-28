package com.iflytek.camera;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.VideoView;

import com.iflytek.camtest.R;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private SurfaceView surface;
    private SurfaceHolder holder;
    private Camera camera;//声明相机
    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮
        //设置手机屏幕朝向，一共有7种
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //SCREEN_ORIENTATION_BEHIND： 继承Activity堆栈中当前Activity下面的那个Activity的方向
        //SCREEN_ORIENTATION_LANDSCAPE： 横屏(风景照) ，显示时宽度大于高度
        //SCREEN_ORIENTATION_PORTRAIT： 竖屏 (肖像照) ， 显示时高度大于宽度
        //SCREEN_ORIENTATION_SENSOR  由重力感应器来决定屏幕的朝向,它取决于用户如何持有设备,当设备被旋转时方向会随之在横屏与竖屏之间变化
        //SCREEN_ORIENTATION_NOSENSOR： 忽略物理感应器——即显示方向与物理感应器无关，不管用户如何旋转设备显示方向都不会随着改变("unspecified"设置除外)
        //SCREEN_ORIENTATION_UNSPECIFIED： 未指定，此为默认值，由Android系统自己选择适当的方向，选择策略视具体设备的配置情况而定，因此不同的设备会有不同的方向选择
        //SCREEN_ORIENTATION_USER： 用户当前的首选方向
        setContentView(R.layout.activity_main);
        Button back = findViewById(R.id.camera_back);
        Button position = findViewById(R.id.camera_position);
        Button shutter = findViewById(R.id.camera_shutter);
        surface = findViewById(R.id.camera_surface);
        holder = surface.getHolder();//获得句柄
        holder.addCallback(this);//添加回调
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//surfaceview不维护自己的缓冲区，等待屏幕渲染引擎将内容推送到用户面前
        //设置监听
        back.setOnClickListener(listener);
        position.setOnClickListener(listener);
        shutter.setOnClickListener(listener);
    }
    VideoView videoView;
    //响应点击事件
    final OnClickListener listener = new OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.camera_back:
                    //返回
                    MainActivity.this.finish();
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mPlayer) {
                            // TODO Auto-generated method stub
                            mPlayer.start();
                            mPlayer.setLooping(true);
                        }
                    });

                    break;
                case R.id.camera_position:
                    //切换前后摄像头
                    CameraInfo cameraInfo = new CameraInfo();
                    int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
                    for (int i = 0; i < cameraCount; i++) {
                        Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
                        if (cameraPosition == 1) {
                            //现在是后置，变更为前置
                            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                                camera.stopPreview();//停掉原来摄像头的预览
                                camera.release();//释放资源
                                camera = null;//取消原来摄像头
                                camera = Camera.open(i);//打开当前选中的摄像头
                                try {
                                    camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                camera.startPreview();//开始预览
                                cameraPosition = 0;
                                break;
                            }
                        } else {
                            //现在是前置， 变更为后置
                            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                                camera.stopPreview();//停掉原来摄像头的预览
                                camera.release();//释放资源
                                camera = null;//取消原来摄像头
                                camera = Camera.open(i);//打开当前选中的摄像头
                                try {
                                    camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                camera.startPreview();//开始预览
                                cameraPosition = 1;
                                break;
                            }
                        }
                    }
                    break;
                case R.id.camera_shutter:
                    //快门
                    //自动对焦
                    camera.autoFocus((success, camera) -> {
                        // TODO Auto-generated method stub
                        if (success) {
                            //设置参数，并拍照
                            Parameters params = camera.getParameters();
                            params.setPictureFormat(PixelFormat.JPEG);//图片格式
//                                params.setPreviewSize(800, 480);//图片大小
                            camera.setParameters(params);//将参数设置到我的camera
                            camera.takePicture(null, null, jpeg);//将拍摄到的照片给自定义的对象
                        }
                    });

                    break;
            }
        }
    };


    /*surfaceHolder他是系统提供的一个用来设置surfaceView的一个对象，而它通过surfaceView.getHolder()这个方法来获得。
     Camera提供一个setPreviewDisplay(SurfaceHolder)的方法来连接*/
    //SurfaceHolder.Callback,这是个holder用来显示surfaceView 数据的接口,他必须实现以下3个方法
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //当surfaceview创建时开启相机
        if (camera == null) {
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
                camera.startPreview();//开始预览
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //当surfaceview关闭时，关闭预览并释放资源
        camera.stopPreview();
        camera.release();
        camera = null;
        surface = null;
    }

    //创建jpeg图片回调数据对象
    PictureCallback jpeg = (data, camera) -> {
        // TODO Auto-generated method stub
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //自定义文件保存路径  以拍摄时间区分命名
            //照片保存路径
            @SuppressLint({"SimpleDateFormat", "SdCardPath"})
            String filepath = "/sdcard/DCIM/Camera/" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
            File file = new File(filepath);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
            bos.flush();// 刷新此缓冲区的输出流
            bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
            camera.stopPreview();//关闭预览 处理数据
            camera.startPreview();//数据处理完后继续开始预览
            bitmap.recycle();//回收bitmap空间
            updateGallery(getApplicationContext(), filepath);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    };

    /**
     * 更新图片到图库中
     *
     * @param context 上下文
     * @param path    更新图片的路径
     */
    public static void updateGallery(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}
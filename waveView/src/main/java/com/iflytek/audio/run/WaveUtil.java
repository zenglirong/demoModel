package com.iflytek.audio.run;

import com.iflytek.audio.view.WaveView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description: 曲线绘制工具类
 * @Author: GiftedCat
 * @Date: 2021-01-01
 */
public class WaveUtil {

    private Timer timer;
    private TimerTask timerTask;

    float data = 0f;
    float[] datas;

    /**
     * 模拟一次注入多条数据
     *
     * @param length       需要一次性注入数据的数量
     * @param waveShowView 控件
     */
    public WaveUtil(final WaveView waveShowView, int length) {
        datas = new float[length];
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {/** 随机生成5条数据*/
                for (int i = 0; i < datas.length; i++) {
//                    datas[i] = i%40 - 19;
                    datas[i] = new Random().nextFloat() * (20f) - 10f;
                }
                waveShowView.showLines(datas);
            }
        };
        //500表示调用schedule方法后等待500ms后调用run方法，50表示以后调用run方法的时间间隔
        timer.schedule(timerTask, 1000, 1000);
    }

    /**
     * 模拟数据
     */
    public WaveUtil(final WaveView waveShowView) {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                data = new Random().nextFloat() * (20f) - 10f;
                waveShowView.showLine(data);//取得是-10到10间的浮点数
            }
        };
        //500表示调用schedule方法后等待500ms后调用run方法，50表示以后调用run方法的时间间隔
        timer.schedule(timerTask, 1000, 1);
    }

    /**
     * 停止绘制
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}

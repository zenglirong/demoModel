package com.iflytek.audio.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * * @Description: 自定义View-波形图绘制
 * * @Date: 2022-11-03
 */
public class WaveView extends View {
    private float mWidth;           // 屏幕宽
    private float mHeight;          // 屏幕高
    private boolean isRefresh;      // 修改了线条的长度，需要刷新页面

    private Paint mLinePaint;       // 网格画笔
    private Paint mWavePaint;       // 数据线画笔
    private Path mPath;             // 线条的路径
    private float[] dataArray;      // 保存已绘制的数据坐标
    private int row;                // 数据点的数量
    private int draw_index;         // 画笔偏移位置

    private float max_value;        // 数据最大值，默认-20~20之间
    private int drawMode;           // 绘制模式: 0.常规绘制模式 不断往后推的方式,1.LOOP_MODE循环绘制模式
    private int cover_gap;          // 覆盖旧线条的间隙
    private int draw_point_length;  // 常规模式下，需要一次绘制的点的数量

    private int wave_line_width;    // 线条的长度，可用于控制横坐标写入的长度
    private float wave_stroke_width;// 波形线条粗细
    private int grid_line_width;    // 网格的宽高间隙
    private float grid_stroke_width;// 网格线条的粗细

    private boolean gridVisible;    // 网格是否可见
    private int gridHorizontalNum;  // 网格横线的数量
    private int gridVerticalNum;    // 网格竖线的数量

    private int waveLineColor = Color.parseColor("#ffff00");    // 波形颜色
    private int gridLineColor = Color.parseColor("#222222");    // 网格颜色

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) init(attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        mWidth = w;
        mHeight = h;
        super.onSizeChanged(w, h, oldW, oldH);
    }

    private void init(AttributeSet attrs) {
        String NAMESPACE = "http://schemas.android.com/apk/res-auto";
        setDrawMode(attrs.getAttributeIntValue(NAMESPACE, "draw_mode", 0));
        setMaxValue(attrs.getAttributeIntValue(NAMESPACE, "max_value", 20));
        setCoverGap(attrs.getAttributeIntValue(NAMESPACE, "cover_gap", 0));
        setWaveLineWidth(attrs.getAttributeIntValue(NAMESPACE, "wave_line_width", 10));
        setWaveStrokeWidth(attrs.getAttributeIntValue(NAMESPACE, "wave_line_stroke_width", 3));
        setGridVisible(attrs.getAttributeBooleanValue(NAMESPACE, "grid_visible", true));
        setGridStrokeWidth(attrs.getAttributeIntValue(NAMESPACE, "grid_line_stroke_width", 1));
        setGridWidth(attrs.getAttributeIntValue(NAMESPACE, "grid_line_width", 100));

        String wave_line_color = attrs.getAttributeValue(NAMESPACE, "wave_line_color");
        if (wave_line_color != null && !wave_line_color.isEmpty()) {
            setWaveLineColor(wave_line_color);
        }

        String grid_line_color = attrs.getAttributeValue(NAMESPACE, "grid_line_color");
        if (grid_line_color != null && !grid_line_color.isEmpty()) {
            setGridLineColor(grid_line_color);
        }

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(grid_stroke_width);
        mLinePaint.setAntiAlias(true);  // 抗锯齿效果

        mWavePaint = new Paint();
        mWavePaint.setStyle(Paint.Style.STROKE);
        mWavePaint.setColor(waveLineColor);
        mWavePaint.setStrokeWidth(wave_stroke_width);
        mWavePaint.setAntiAlias(true);  // 抗锯齿效果

        mPath = new Path();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();    // 获取控件的宽高
        mHeight = getMeasuredHeight();

        // 根据网格的单位长宽，获取能绘制网格横线和竖线的数量
        gridHorizontalNum = (int) (mHeight / grid_line_width);
        gridVerticalNum = (int) (mWidth / grid_line_width);

        // 根据线条长度，最多能绘制多少个数据点
        row = (int) (mWidth / wave_line_width);
        dataArray = new float[row];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gridVisible) drawGrid(canvas); // 绘制网格
        if (drawMode == 0) drawWaveLineNormal(canvas);// 绘制折线 normal
        if (drawMode == 1) drawWaveLineLoop(canvas);  // 绘制折线 loop
        if (++draw_index >= row) draw_index = 0;
    }

    /**
     * @param canvas 常规模式绘制折线
     */
    private void drawWaveLineNormal(Canvas canvas) {
        drawPathFromData(canvas, 0, row);
        if (row - draw_point_length >= 0) {
            System.arraycopy(dataArray, draw_point_length, dataArray, 0, row - draw_point_length);
        }
    }

    /**
     * @param canvas 循环模式绘制折线, Math.min找到最小的起始值，防止数组下标溢出
     */
    private void drawWaveLineLoop(Canvas canvas) {
        drawPathFromData(canvas, 0, draw_index);
        drawPathFromData(canvas, Math.min(row - 1, draw_index+ cover_gap), row);
    }

    /**
     * 取数组中的指定一段数据来绘制折线
     *
     * @param start 起始数据位
     * @param end   结束数据位
     */
    private void drawPathFromData(Canvas canvas, int start, int end) {
        float nowX, nowY;       // 当前的x，y坐标
        mPath.reset();
        float startY = mHeight / 2 - dataArray[start] * (mHeight / (max_value * 2));
        mPath.moveTo((start) * wave_line_width, startY);
        for (int i = start; i < end; i++) {
            if (isRefresh) {
                isRefresh = false;
                return;
            }
            nowX = i * wave_line_width;
            float dataValue = dataArray[i];
            // 判断数据为正数还是负数  超过最大值的数据按最大值来绘制
            if (dataValue > 0) {
                if (dataValue > max_value) {
                    dataValue = max_value;
                }
            } else {
                if (dataValue < -max_value) {
                    dataValue = -max_value;
                }
            }
            nowY = mHeight / 2 - dataValue * (mHeight / (max_value * 2));
            mPath.lineTo(nowX, nowY);
        }
        canvas.drawPath(mPath, mWavePaint);
    }

    /**
     * @param canvas 绘制网格
     */
    private void drawGrid(Canvas canvas) {
        mLinePaint.setColor(gridLineColor);     // 设置颜色
        for (int i = 0; i < gridHorizontalNum + 1; i++) {   // 绘制横线
            canvas.drawLine(0, i * grid_line_width, mWidth, i * grid_line_width, mLinePaint);
        }
        for (int i = 0; i < gridVerticalNum + 1; i++) {     // 绘制竖线
            canvas.drawLine(i * grid_line_width, 0, i * grid_line_width, mHeight, mLinePaint);
        }
    }

    /**
     * @param line 添加新的数据 line = 的高度
     */
    public void showLine(float line) {
        switch (drawMode) {
            case 0: // 常规模式数据添加至最后一位
                draw_point_length = 1;
                dataArray[row - 1] = line;
                break;
            case 1: // 循环模式数据添加至当前绘制的位
                dataArray[draw_index] = line;
                break;
        }
        postInvalidate();
    }

    /**
     * @param lines 添加多个数据点的高度
     */
    public void showLines(float[] lines) {
        switch (drawMode) {
            case 0: // 常规模式绘制多个点
                draw_point_length = lines.length;
                showLinesNormal(lines);
                break;
            case 1: // 轮询方式添加多个点
                showLinesLoop(lines);
                break;
        }
        postInvalidate();
    }

    /**
     * 常规的方式添加多个点
     */
    public void showLinesNormal(float[] lines) {
        int length = lines.length;
        if (length > row) length = row; // 防止传入数据太大，导致dataArray溢出范围
        for (int i = 0; i < length; i++) {  // 由于是从最后面传入数据，所以一切数据从后面开始拷贝
            dataArray[row - (length - i)] = lines[i];
        }
    }

    /**
     * 轮询方式添加多个点
     */
    public void showLinesLoop(float[] lines) {
        for (float line : lines) {
            dataArray[draw_index] = line;
            if (++draw_index > row - 1) draw_index = 0;
        }
        if (--draw_index == -1) draw_index = row - 1;
    }

    /**
     * @param max_value 设置波形最大值
     * @return 支持链式设置
     */
    public WaveView setMaxValue(int max_value) {
        this.max_value = max_value;
        return this;
    }

    /**
     * @param draw_mode 0.常规绘制模式 不断往后推的方式,1.LOOP_MODE循环绘制模式
     * @return 支持链式设置
     */
    public WaveView setDrawMode(int draw_mode) {
        this.drawMode = draw_mode;
        return this;
    }

    /**
     * @param cover 设置覆盖旧线条的间隙
     * @return 支持链式设置
     */
    public WaveView setCoverGap(int cover) {
        this.cover_gap = cover;
        return this;
    }

    /**
     * @param colorString 设置网格颜色
     * @return 支持链式设置
     */
    public WaveView setGridLineColor(String colorString) {
        this.gridLineColor = Color.parseColor(colorString);
        return this;
    }

    /**
     * @param width 设置网格线宽
     * @return 支持链式设置
     */
    public WaveView setGridStrokeWidth(float width) {
        this.grid_stroke_width = width;
        return this;
    }

    /**
     * @param width 设置网格高度和宽度
     * @return 支持链式设置
     */
    public WaveView setGridWidth(int width) {
        this.grid_line_width = width;
        return this;
    }

    /**
     * @param visible true: 显示网格
     * @return 支持链式设置
     */
    public WaveView setGridVisible(boolean visible) {
        this.gridVisible = visible;
        return this;
    }

    /**
     * @param width 设置波形宽度，外部设置更新
     * @return 支持链式设置
     */
    public WaveView setWaveWidth(int width) {
        draw_index = 0;
        this.wave_line_width = width;
        row = (int) (mWidth / wave_line_width);
        isRefresh = true;
        dataArray = new float[row];
        return this;
    }

    /**
     * @param width 设置波形宽度
     * @return 支持链式设置
     */
    public WaveView setWaveLineWidth(int width) {
        this.wave_line_width = width;
        return this;
    }

    /**
     * @param width 设置波形折线粗细
     * @return 支持链式设置
     */
    public WaveView setWaveStrokeWidth(float width) {
        this.wave_stroke_width = width;
        return this;
    }

    /**
     * @param colorString 设置波形颜色
     * @return 支持链式设置
     */
    public WaveView setWaveLineColor(String colorString) {
        this.waveLineColor = Color.parseColor(colorString);
        return this;
    }
}

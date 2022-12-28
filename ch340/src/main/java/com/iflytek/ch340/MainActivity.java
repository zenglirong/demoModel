package com.iflytek.ch340;

import static android.os.SystemClock.sleep;
import static com.iflytek.ch340.StringChangeHex.toByteArray;
import static com.iflytek.ch340.StringChangeHex.toHexString;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

public class MainActivity extends Activity {
    //    private static final String TAG = "MainActivity";

    private boolean isOpen;
    private Handler handler;
    private MainActivity activity;

    public byte[] writeBuffer;
    public byte[] readBuffer;

    public int baudRate;
    public byte stopBit;
    public byte dataBit;
    public byte parity;
    public byte flowControl;

    @SuppressLint({"SetTextI18n", "HandlerLeak"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApp.driver = new CH34xUARTDriver((UsbManager) getSystemService(Context.USB_SERVICE), this, "cn.wch.wchusbdriver.USB_PERMISSION");

        initConfig();
        initButton();
    }

    @SuppressLint("SetTextI18n")
    void initButton() {
        Button configButton = findViewById(R.id.configButton);
        Button writeButton = findViewById(R.id.WriteButton);
        Button clearButton = findViewById(R.id.clearButton);
        Button openButton = (Button) findViewById(R.id.open_device);
        EditText readText = findViewById(R.id.ReadValues);
        EditText writeText = findViewById(R.id.WriteValues);


        if (!MyApp.driver.UsbFeatureSupported()) {// 判断系统是否支持USB HOST
            Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                    .setPositiveButton("确认", (arg0, arg1) -> System.exit(0)).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持常亮的屏幕的状态
        writeBuffer = new byte[512];
        readBuffer = new byte[512];
        isOpen = false;
        configButton.setEnabled(false);
        writeButton.setEnabled(false);
        activity = this;

        //打开流程主要步骤为ResumeUsbList，UartInit
        openButton.setOnClickListener(arg0 -> {
            if (!isOpen) {
                int retVal = MyApp.driver.ResumeUsbPermission();
                if (retVal == 0) {  //Resume usb device list
                    retVal = MyApp.driver.ResumeUsbList();
                    if (retVal == 0) {
                        if (MyApp.driver.mDeviceConnection != null) {
                            if (!MyApp.driver.UartInit()) {//对串口设备进行初始化操作
                                uiToast("Initialization failed!");
                            } else {
                                uiToast("Device opened");
                                isOpen = true;
                                openButton.setText("Close");
                                configButton.setEnabled(true);
                                writeButton.setEnabled(true);
                                new readThread().start();//开启读线程读取串口接收的数据
                            }
                        } else uiToast("Open failed!");
                    } else if (retVal == -1) {// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
                        uiToast("Open failed!");
                        MyApp.driver.CloseDevice();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setIcon(R.drawable.icon);
                        builder.setTitle("未授权限");
                        builder.setMessage("确认退出吗？");
                        builder.setPositiveButton("确定", (dialog, which) -> System.exit(0));
                        builder.show();
                    }
                }
            } else {
                openButton.setText("Open");
                configButton.setEnabled(false);
                writeButton.setEnabled(false);
                isOpen = false;
                sleep(200);
                MyApp.driver.CloseDevice();
            }
        });

        configButton.setOnClickListener(arg0 -> {
            if (MyApp.driver.SetConfig(baudRate, dataBit, stopBit, parity, flowControl)) {//配置串口波特率，函数说明可参照编程手册
                uiToast("Config successfully");
            } else uiToast("Config failed!");
        });

        writeButton.setOnClickListener(arg0 -> {
            byte[] to_send = toByteArray(writeText.getText().toString());       //以16进制发送
            // byte[] to_send = toByteArray2(writeText.getText().toString());   //以字符串方式发送
            // 写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
            if (MyApp.driver.WriteData(to_send, to_send.length) < 0) uiToast("Write failed!");
        });

        clearButton.setOnClickListener(arg0 -> readText.setText(""));

        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                readText.append((String) msg.obj);
            }
        };
    }

    //处理界面
    private void initConfig() {
        /* by default it is 9600 */
        baudRate = 115200;
        Spinner baudSpinner = findViewById(R.id.baudRateValue);
        ArrayAdapter<CharSequence> baudAdapter = ArrayAdapter.createFromResource(this, R.array.baud_rate, R.layout.my_spinner_textview);
        baudAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        baudSpinner.setAdapter(baudAdapter);
        baudSpinner.setGravity(0x10);
        baudSpinner.setSelection(9);
        /* set the adapter listeners for baud */
        baudSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                baudRate = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* stop bits default is stop bit 1 */
        stopBit = 1;
        Spinner stopSpinner = (Spinner) findViewById(R.id.stopBitValue);
        ArrayAdapter<CharSequence> stopAdapter = ArrayAdapter.createFromResource(this, R.array.stop_bits, R.layout.my_spinner_textview);
        stopAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        stopSpinner.setAdapter(stopAdapter);
        stopSpinner.setGravity(0x01);
        /* set the adapter listeners for stop bits */
        stopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stopBit = (byte) Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* data bits default data bit is 8 bit */
        dataBit = 8;
        Spinner dataSpinner = (Spinner) findViewById(R.id.dataBitValue);
        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this, R.array.data_bits, R.layout.my_spinner_textview);
        dataAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        dataSpinner.setAdapter(dataAdapter);
        dataSpinner.setGravity(0x11);
        dataSpinner.setSelection(3);
        /* set the adapter listeners for data bits */
        dataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataBit = (byte) Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* parity default is none */
        parity = 0;
        Spinner paritySpinner = (Spinner) findViewById(R.id.parityValue);
        ArrayAdapter<CharSequence> parityAdapter = ArrayAdapter.createFromResource(this, R.array.parity, R.layout.my_spinner_textview);
        parityAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        paritySpinner.setAdapter(parityAdapter);
        paritySpinner.setGravity(0x11);
        /* set the adapter listeners for parity */
        paritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String parityString = parent.getItemAtPosition(position).toString();
                if (parityString.compareTo("None") == 0) parity = 0;
                if (parityString.compareTo("Odd") == 0) parity = 1;
                if (parityString.compareTo("Even") == 0) parity = 2;
                if (parityString.compareTo("Mark") == 0) parity = 3;
                if (parityString.compareTo("Space") == 0) parity = 4;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* flow control default flow control is is none */
        flowControl = 0;
        Spinner flowSpinner = (Spinner) findViewById(R.id.flowControlValue);
        ArrayAdapter<CharSequence> flowAdapter = ArrayAdapter.createFromResource(this, R.array.flow_control, R.layout.my_spinner_textview);
        flowAdapter.setDropDownViewResource(R.layout.my_spinner_textview);
        flowSpinner.setAdapter(flowAdapter);
        flowSpinner.setGravity(0x11);
        /* set the adapter listeners for flow control */
        flowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String flowString = parent.getItemAtPosition(position).toString();
                if (flowString.compareTo("None") == 0) flowControl = 0;
                if (flowString.compareTo("CTS/RTS") == 0) flowControl = 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class readThread extends Thread {
        public void run() {
            byte[] buffer = new byte[4096];
            while (true) {
                Message msg = Message.obtain();
                if (!isOpen) break;
                int length = MyApp.driver.ReadData(buffer, 4096);
                if (length > 0) {
//                    String rec = new String(buffer, 0, length);        //以字符串形式输出
                    msg.obj = toHexString(buffer, length);
                    handler.sendMessage(msg);
                }
            }
        }
    }

    public void uiToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        isOpen = false;
        MyApp.driver.CloseDevice();
        super.onDestroy();
    }
}
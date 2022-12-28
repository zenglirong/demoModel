package com.iflytek.ch340;

import static android.os.SystemClock.sleep;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;

public class UsbToUart {
    public CH34xUARTDriver driver;
    private final Context mContext;
    private static final String TAG = "UsbToUart";
    private boolean isOpen = false;
    private boolean isHex = true;

    /**
     * 定义监听接口
     */
    OnReceiveListener listener;

    public interface OnReceiveListener {
        void receive(String data);
    }

    public void setOnReceiveListener(OnReceiveListener onTestListener) {
        listener = onTestListener;
    }

    UsbToUart(Context context) {
        mContext = context;
        driver = new CH34xUARTDriver((UsbManager) context.getSystemService(Context.USB_SERVICE), context, "cn.wch.wchusbdriver.USB_PERMISSION");
        if (!driver.UsbFeatureSupported()) {// 判断系统是否支持USB HOST
            Log.i(TAG, "您的手机不支持USB HOST，请更换其他手机再试！");
        }
        openDevice();
    }

    public boolean openDevice() {
        if (!isOpen) {
            int retVal = driver.ResumeUsbPermission();
            if (retVal == 0) {  //Resume usb device list
                retVal = driver.ResumeUsbList();
                if (retVal == 0) {
                    if (driver.mDeviceConnection != null) {
                        if (!driver.UartInit()) {//对串口设备进行初始化操作
                            uiToast("Initialization failed!");
                        } else {
                            uiToast("Device opened");
                            isOpen = true;
                            if (driver.SetConfig(115200, (byte) 8, (byte) 1, (byte) 0, (byte) 0)) {//配置串口波特率，函数说明可参照编程手册
                                uiToast("Config successfully");
                            } else uiToast("Config failed!");

                            new Thread(() -> {//开启读线程读取串口接收的数据
                                byte[] buffer = new byte[4096];
                                while (isOpen) {
                                    int length = driver.ReadData(buffer, 4096);
                                    if (length > 0) {
                                        String msg;
                                        if (isHex) msg = toHexString(buffer, length);
                                        else msg = new String(buffer, 0, length); //以字符串形式输出
                                        listener.receive(msg);
                                    }
                                }
                            }).start();
                        }
                    } else uiToast("Open failed!");
                } else if (retVal == -1) {// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
                    uiToast("Open failed!");
                    driver.CloseDevice();
                } else uiToast("未授权限");
            }
        } else {
            isOpen = false;
            sleep(200);
            driver.CloseDevice();
        }
        return isOpen;
    }

    public void closeDevice() {
        isOpen = false;
        driver.CloseDevice();
    }

    public void setHex(boolean hex) {
        isHex = hex;
    }

    public void sendData(String data) {
        byte[] to_send;
        if (isHex) to_send = toByteArray(data);       //以16进制发送
        else to_send = toByteArray2(data);   //以字符串方式发送
        // 写数据，第一个参数为需要发送的字节数组，第二个参数为需要发送的字节长度，返回实际发送的字节长度
        if (driver.WriteData(to_send, to_send.length) < 0) uiToast("Write failed!");
    }

    private void uiToast(String string) {
        Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * 将byte[]数组转化为String类型
     *
     * @param arg    需要转换的byte[]数组
     * @param length 需要转换的数组长度
     * @return 转换后的String队形
     */
    private String toHexString(byte[] arg, int length) {
        StringBuilder result = new StringBuilder();
        if (arg != null) {
            for (int i = 0; i < length; i++) {
                result.append(Integer.toHexString(arg[i] < 0 ? arg[i] + 256 : arg[i]).length() == 1 ? "0"
                        + Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])
                        : Integer.toHexString(arg[i] < 0 ? arg[i] + 256
                        : arg[i])).append(" ");
            }
            return result.toString();
        }
        return "";
    }

    /**
     * 将String转化为byte[]数组
     *
     * @param arg 需要转换的String对象
     * @return 转换后的byte[]数组
     */
    private byte[] toByteArray(String arg) {
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (char c : array) {
                if (c != ' ') {
                    NewArray[length] = c;
                    length++;
                }
            }
            /* 将char数组中的值转成一个实际的十进制数组 */
            int EvenLength = (length % 2 == 0) ? length : length + 1;
            if (EvenLength != 0) {
                int[] data = new int[EvenLength];
                data[EvenLength - 1] = 0;
                for (int i = 0; i < length; i++) {
                    if (NewArray[i] >= '0' && NewArray[i] <= '9') {
                        data[i] = NewArray[i] - '0';
                    } else if (NewArray[i] >= 'a' && NewArray[i] <= 'f') {
                        data[i] = NewArray[i] - 'a' + 10;
                    } else if (NewArray[i] >= 'A' && NewArray[i] <= 'F') {
                        data[i] = NewArray[i] - 'A' + 10;
                    }
                }
                /* 将 每个char的值每两个组成一个16进制数据 */
                byte[] byteArray = new byte[EvenLength / 2];
                for (int i = 0; i < EvenLength / 2; i++) {
                    byteArray[i] = (byte) (data[i * 2] * 16 + data[i * 2 + 1]);
                }
                return byteArray;
            }
        }
        return new byte[]{};
    }

    /**
     * 将String转化为byte[]数组
     *
     * @param arg 需要转换的String对象
     * @return 转换后的byte[]数组
     */
    private byte[] toByteArray2(String arg) {
        if (arg != null) {
            /* 1.先去除String中的' '，然后将String转换为char数组 */
            char[] NewArray = new char[1000];
            char[] array = arg.toCharArray();
            int length = 0;
            for (char c : array) {
                if (c != ' ') {
                    NewArray[length] = c;
                    length++;
                }
            }
            byte[] byteArray = new byte[length];
            for (int i = 0; i < length; i++) {
                byteArray[i] = (byte) NewArray[i];
            }
            return byteArray;

        }
        return new byte[]{};
    }
}

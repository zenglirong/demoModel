package com.example.interfacelistener;

import static android.os.SystemClock.sleep;

public class Timer {
    static int count = 0;

    public Timer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    sleep(1000);
                    listener.onClick(count++);
                }
            }
        }).start();
    }

    private OnClickItemListener listener;

    public void OnClickItemListener(OnClickItemListener onClickListener) {
        this.listener = onClickListener;
    }

    public interface OnClickItemListener {
        void onClick(int position);
    }
}

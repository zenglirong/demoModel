package com.iflytek.shared;

import static com.iflytek.shared.Shared.initShared;
import static com.iflytek.shared.Shared.readSharedInt;
import static com.iflytek.shared.Shared.readSharedString;
import static com.iflytek.shared.Shared.writeSharedInt;
import static com.iflytek.shared.Shared.writeSharedString;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //数据存储(偏好设置存储)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initShared(this);
        textView = findViewById(R.id.textViewId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void add(View view) {

        writeSharedString("120", readSharedString( "120")+1);
        writeSharedInt("121", readSharedInt( "121")+1);

        textView.setText(readSharedString( "120") + "   " + readSharedInt( "121"));
    }
}
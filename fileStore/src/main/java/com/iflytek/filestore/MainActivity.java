package com.iflytek.filestore;

import static com.iflytek.filestore.FileSave.ReadFile;
import static com.iflytek.filestore.FileSave.WriteFile;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    // 定义文件夹名
    private String fileName = "save.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.button_savetext).setOnClickListener(v -> {
            //获取输入框输入的内容
            EditText inputFileText = findViewById(R.id.edit_textinput);
            WriteFile(this, fileName, inputFileText.getText().toString());

        });
        findViewById(R.id.button_readtext).setOnClickListener(v -> {
            //读取文本内容
            TextView outFileText = findViewById(R.id.text_textoutput);
            outFileText.setText(ReadFile(this, fileName));
        });
    }
}
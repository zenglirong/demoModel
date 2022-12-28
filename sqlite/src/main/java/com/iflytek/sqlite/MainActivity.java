package com.iflytek.sqlite;


import static com.iflytek.sqlite.Sqlite.deleteByName;
import static com.iflytek.sqlite.Sqlite.initSqlite;
import static com.iflytek.sqlite.Sqlite.insertData;
import static com.iflytek.sqlite.Sqlite.queryAll;
import static com.iflytek.sqlite.Sqlite.updateById;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSqlite(this);

        insertData("唐浩",14, "tanghao@qq.com");
        insertData("藤山",54, "tengshan@qq.com");
        insertData("大师",38, "dashi@qq.com");
        queryAll();
        updateById(2, "唐山", 18, "tangshan@qq.com");
        queryAll();
        deleteByName("唐浩");
        queryAll();

    }
}
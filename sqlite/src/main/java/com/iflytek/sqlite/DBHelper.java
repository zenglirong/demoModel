package com.iflytek.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        // context
        // name ： 数据库文件名
        // version： 数据库版本号
        super(context, "tedu.db", null, 1);
        //
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 日志
        //System.out.println("DBOpenHelper.onCreate()");
        // 当创建数据库时，应该直接创建并设计数据表
        String sql = "CREATE TABLE users ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name VARCHAR(16) NOT NULL UNIQUE, "
                + "age INTEGER, "
                + "email VARCHAR(50)"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 当升级版本时， 此次无视
        // 1.
        // 2.
        // 3.
        // 4.
        // 5.
    }
}

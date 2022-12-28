package com.iflytek.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Sqlite {
    static DBHelper dbHelper;

    public static void initSqlite(Context context) {
        // 1.获取DBOpenHelper对象
        dbHelper = new DBHelper(context);
    }

    // 测试增加数据
    public static void insertData(String name, int age, String email) {
        // 日志
        Log.i("SQLite test", "测试增加数据");

        // 2.获取SqliteDatabase 对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 3.执行：增加
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        values.put("email", email);
        long id = db.insert("users", null, values);
        // 4.释放数据
        db.close();
        // 日志
        Log.i("SQLite test", "增加数据完成 id = " + id);
    }

    // 测试查询数据
    public static void queryAll() {
        // 日志
        Log.i("SQLite test", "测试查询数据");
        // 2.获取SqliteDatabase 对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 3.1执行：查询
        String[] columns = null;// {"name", "age"}; // 需要查询的字段列表
        String selection = null; // whereClause
        String[] selectionArgs = null; // whereArgs
        String groupBy = null; // GROUP BY子句,是使用了聚合函数后的分组
        String having = null; // HAVING 子句
        String orderBy = null;
        Cursor c = db.query("users", columns, selection, selectionArgs, groupBy, having, orderBy);
        // 3.2 读取Cursor
        if (c.moveToFirst()) {
            for (; !c.isAfterLast(); c.moveToNext()) {
                long id = c.getLong(0);
                String name = c.getString(1);
                int age = c.getInt(2);
                String email = c.getString(3);
                Log.i("SQLite test", "id=" + id + ", name=" + name + ", age=" + age + ", email=" + email);
            }
        }
        // 4.释放资源
        db.close();
        // 日志
        Log.i("SQLite test", "查询数据完成 id = " + c);
    }

    // 测试修改数据
    public static void updateById(long id, String name, int age, String email) {
        // 日志
        Log.i("SQLite test", "测试修改数据");
        // 2.获取SqliteDatabase 对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 3.执行：修改
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("age", age);
        values.put("email", email);
        int affectedRows = db.update("users", values, "_id=?", new String[]{("" + id)});
        // 4.释放资源
        db.close();
        // 日志
        Log.i("SQLite test", "修改数据完成 id = " + affectedRows);
    }

    // 测试删除数据
    public static void deleteByName(String neme) {
        // 日志
        Log.i("SQLite test", "测试删除数据");
        // 2.获取SqliteDatabase 对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 3.执行：删除
        int affectedRows = db.delete("users", "name=?", new String[]{neme});
        // 4.释放资源
        db.close();
        // 日志
        Log.i("SQLite test", "删除数据完成 affectedRows = " + affectedRows);
    }
}

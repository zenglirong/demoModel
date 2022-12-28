
package com.iflytek.application;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> data = new ArrayList<>();
        data.add("2121212");
        data.add("32313");
        data.add("13131331");
        data.add("3131313");


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        RecyclerView mRecyclerView = findViewById(R.id.rv_right);
        RightAdapter mAdapter = new RightAdapter(data);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));  //设置间隔（可选水平或者垂直）
        mAdapter.setOnMyItemClickListener((v, position) -> {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            Toast.makeText(v.getContext(), "position:" + position, Toast.LENGTH_SHORT).show();
        });
    }
}
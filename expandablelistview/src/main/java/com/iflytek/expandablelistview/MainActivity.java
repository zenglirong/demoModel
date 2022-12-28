package com.iflytek.expandablelistview;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<ArrayList<Item>> iData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExpandableListView expandableListView = findViewById(R.id.exlist_lol);

        //数据准备
        ArrayList<Group> gData = new ArrayList<>();
        gData.add(new Group("分组1"));
        gData.add(new Group("分组2"));
        gData.add(new Group("分组3"));

        ArrayList<Item> lData = new ArrayList<>();
        //分组1
        lData.add(new Item(R.mipmap.iv_lol_icon3, "剑圣"));
        lData.add(new Item(R.mipmap.iv_lol_icon4, "德莱文"));
        lData.add(new Item(R.mipmap.iv_lol_icon13, "男枪"));
        lData.add(new Item(R.mipmap.iv_lol_icon14, "韦鲁斯"));
        iData.add(lData);
        //分组2
        lData = new ArrayList<>();
        lData.add(new Item(R.mipmap.iv_lol_icon1, "提莫"));
        lData.add(new Item(R.mipmap.iv_lol_icon7, "安妮"));
        lData.add(new Item(R.mipmap.iv_lol_icon8, "天使"));
        lData.add(new Item(R.mipmap.iv_lol_icon9, "泽拉斯"));
        lData.add(new Item(R.mipmap.iv_lol_icon11, "狐狸"));
        iData.add(lData);
        //分组3
        lData = new ArrayList<>();
        lData.add(new Item(R.mipmap.iv_lol_icon2, "诺手"));
        lData.add(new Item(R.mipmap.iv_lol_icon5, "德邦"));
        lData.add(new Item(R.mipmap.iv_lol_icon6, "奥拉夫"));
        lData.add(new Item(R.mipmap.iv_lol_icon10, "龙女"));
        lData.add(new Item(R.mipmap.iv_lol_icon12, "狗熊"));
        iData.add(lData);

        ExpListAdapter myAdapter = new ExpListAdapter(gData, iData, this);
        expandableListView.setAdapter(myAdapter);

        //为列表设置点击事件
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Toast.makeText(this, "你点击了：" + iData.get(groupPosition).get(childPosition).getName(), Toast.LENGTH_SHORT).show();
            return true;
        });
    }
}

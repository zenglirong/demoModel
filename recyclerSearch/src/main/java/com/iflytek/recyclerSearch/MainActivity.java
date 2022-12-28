package com.iflytek.recyclerSearch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.recyclerSearch.adapter.ResultMyAdapter;
import com.iflytek.recyclerSearch.data.TestItemModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mbtn_selectall;
    private Button mbtn_start;
    private EditText met_search;
    private ResultMyAdapter mAdapter;
    private RecyclerView recyclerview_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview_list = findViewById(R.id.con_smoke_viewpager);
        mbtn_selectall = findViewById(R.id.btn_selectall);
        mbtn_start = findViewById(R.id.btn_start);
        met_search = findViewById(R.id.et_search);
        mbtn_selectall.setOnClickListener(this);
        mbtn_start.setOnClickListener(this);

        initView();
        searchText();
    }

    private void initView() {

        List<TestItemModel> data = new ArrayList<>();

        data.add(new TestItemModel("开机自启白名单"));
        data.add(new TestItemModel("禁用网址黑名单"));

        mAdapter = new ResultMyAdapter(this, data);
        recyclerview_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview_list.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_selectall:

                break;
            case R.id.btn_start:

                break;
            default:
                break;
        }
    }

    void searchText(){
        met_search.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {

            }

            @Override
            public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
                mAdapter.getFilter().filter(p1.toString());
            }

            @Override
            public void afterTextChanged(Editable p1) {

            }
        });
    }
}
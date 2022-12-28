package com.iflytek.recyclerSearch;

import static com.iflytek.recyclerSearch.utils.utils.uiToast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iflytek.recyclerSearch.adapter.ResultMyAdapter;
import com.iflytek.recyclerSearch.data.TestItemModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    private EditText met_search;
    private ResultMyAdapter mAdapter;
    private RecyclerView recyclerview_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        recyclerview_list = findViewById(R.id.con_smoke_viewpager);
        findViewById(R.id.btn_selectall).setOnClickListener(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
        met_search = findViewById(R.id.et_search);

        initView();
        searchText();
    }

    @SuppressLint("NonConstantResourceId")
    private void initView() {

        List<TestItemModel> data = new ArrayList<>();

        data.add(new TestItemModel("开机自启白名单"));
        data.add(new TestItemModel("禁用网址黑名单"));

        mAdapter = new ResultMyAdapter(data);
        recyclerview_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview_list.setAdapter(mAdapter);

        mAdapter.OnClickItemListener((view, position) -> {
            switch (view.getId()) {
                case R.id.test_result_id:
                    uiToast("点击测试: " + position);
                    break;
                case R.id.test_time_id:
                    uiToast("点击时间: " + position);
                    break;
            }
        });
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

    void searchText() {
        met_search.addTextChangedListener(new TextWatcher() {
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
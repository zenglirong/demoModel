package com.iflytek.recyclerSearch;

import static com.iflytek.recyclerSearch.utils.ObtainClass.getChildPackage;
import static com.iflytek.recyclerSearch.utils.ObtainClass.getTestClass;

import android.content.Context;
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
import com.iflytek.recyclerSearch.utils.TestFrameworkClassModel;

import org.testng.TestNG;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static Context context = null;
    private Button mbtn_selectall;
    private Button mbtn_start;
    private EditText met_search;
    private ResultMyAdapter mAdapter;
    private RecyclerView recyclerview_list;


    public static ArrayList<TestItemModel> BaseInfoGroup;
    public static ArrayList<TestItemModel> FunctionInfoGroup;
    public static ArrayList<TestItemModel> StabilityInfoGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        recyclerview_list = findViewById(R.id.con_smoke_viewpager);
        mbtn_selectall = findViewById(R.id.btn_selectall);
        mbtn_start = findViewById(R.id.btn_start);
        met_search = findViewById(R.id.et_search);
        mbtn_selectall.setOnClickListener(this);
        mbtn_start.setOnClickListener(this);

        getInfoByPackage(getChildPackage(this, "TestNgCase"));
        initView();
        searchText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_selectall:
                mAdapter.selectAll(true);
                break;
            case R.id.btn_start:

                new Thread(() -> {
                    List<TestItemModel> mStartListData = mAdapter.getSelectItem();
                    mAdapter.clearAll();
                    for (int i = 0; mStartListData.size() > i; i++) {
                        TestNG testNG = new TestNG();
                        testNG.setTestClasses(new Class[]{mStartListData.get(i).getItemClass()});
                        testNG.run();
                    }
                }).start();
                break;
            default:
                break;
        }
    }

    public void getInfoByPackage(List<String> test_packages) {
        ArrayList<TestFrameworkClassModel> InfoGroup = new ArrayList<>();
        BaseInfoGroup = new ArrayList<>();
        FunctionInfoGroup = new ArrayList<>();
        StabilityInfoGroup = new ArrayList<>();

        for (String packages : test_packages) {
            List<Class<?>> test_classes = getTestClass(this, packages);
            String temp_item = null;
            boolean temp_isBase = false;
            boolean temp_isStability = false;
            boolean temp_isFunction = false;
            Class<?> isBaseClass = null;
            Class<?> isFunctionClass = null;
            Class<?> isStabilityClass = null;
            for (Class<?> test_class : test_classes) {
                if (test_class.getName().contains("TestItemName")) {
                    for (Method method : test_class.getMethods()) {
                        if (method.getAnnotations().length > 0) {
                            String str = method.getAnnotations()[0].toString();
                            if (!str.contains("description=,")) {
                                int first = str.indexOf("description="); //?????????????????????????????????
                                str = str.substring(first + 11);//??????????????????????????????
                                int last = str.indexOf(","); //????????????????????????????????????
                                temp_item = str.substring(1, last);//??????????????????????????????
                            }
                        }
                    }
                }
                if (test_class.getName().contains("Base")) {
                    temp_isBase = true;
                    isBaseClass = test_class;
                }
                if (test_class.getName().contains("Stability")) {
                    temp_isStability = true;
                    isStabilityClass = test_class;
                }
                if (test_class.getName().contains("Function")) {
                    temp_isFunction = true;
                    isFunctionClass = test_class;
                }
            }
            InfoGroup.add(new TestFrameworkClassModel(temp_item, temp_isBase, temp_isStability, temp_isFunction, isBaseClass, isFunctionClass, isStabilityClass));
        }

        for (int i = 0; i < InfoGroup.size(); i++) {
            if (InfoGroup.get(i).isBase()) {
                String ss = InfoGroup.get(i).getItemName();
                Class<?> ssClass = InfoGroup.get(i).getIsBaseClass();
                BaseInfoGroup.add(new TestItemModel(ss, ssClass));
            }
            if (InfoGroup.get(i).isFunction()) {
                String ss2 = InfoGroup.get(i).getItemName();
                Class<?> ssClass2 = InfoGroup.get(i).getIsFunctionClass();
                FunctionInfoGroup.add(new TestItemModel(ss2, ssClass2));
            }
            if (InfoGroup.get(i).isStability()) {
                String ss3 = InfoGroup.get(i).getItemName();
                Class<?> ssClass3 = InfoGroup.get(i).getIsStabilityClass();
                StabilityInfoGroup.add(new TestItemModel(ss3, ssClass3));
            }
        }
    }

    private void initView() {
        mAdapter = new ResultMyAdapter(BaseInfoGroup);
        recyclerview_list.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_list.setAdapter(mAdapter);
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

    public static Context getContext() {
        return context;
    }
}
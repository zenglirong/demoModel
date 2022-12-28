package com.iflytek.tabview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager = null;
    ArrayList<View> viewContainter = new ArrayList<View>();
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] titles = {"冒烟测试", "功能测试", "压力测试", "自定义测试"};
        TabLayout tabLayout =  findViewById(R.id.tablayout_id);
        for (int i = 0; i < 4; i++) {
            tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.item), i);
            textView = tabLayout.getTabAt(i).getCustomView().findViewById(R.id.tab_text);
            textView.setText(titles[i]);//设置tab上的文字
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager =  this.findViewById(R.id.viewpager);

        View view1 = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.tab2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.tab3, null);
        View view4 = LayoutInflater.from(this).inflate(R.layout.tab4, null);
        //viewpager开始添加view
        viewContainter.add(view1);
        viewContainter.add(view2);
        viewContainter.add(view3);
        viewContainter.add(view4);
        //设置Adapter
        viewPager.setAdapter(new MyPagerAdapters());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });




    }
    /**
     *   ViewPager的数据适配器
     */
    class MyPagerAdapters extends PagerAdapter {
        //返回可以滑动的VIew的个数
        @Override
        public int getCount() {
            return viewContainter.size();
        }
        //滑动切换的时候销毁当前的组件
        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            ((ViewPager) container).removeView(viewContainter.get(position));
        }
        //将当前视图添加到container中并返回当前View视图
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(viewContainter.get(position));
            return viewContainter.get(position);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}
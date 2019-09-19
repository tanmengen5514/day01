package com.tanmengen.en.tanmengenqi;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.tanmengen.en.tanmengenqi.fragments.Fragment1;
import com.tanmengen.en.tanmengenqi.fragments.Fragment2;

public class MainActivity extends AppCompatActivity {

    private ViewPager mVp;
    private TabLayout mTab;
    private FragmentManager manager;
    private FrameLayout mContain;
    private Fragment1 fragment1;
    private Fragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //找到控件对象
        mTab = (TabLayout) findViewById(R.id.tab);
       mTab.addTab(mTab.newTab().setText("首页").setIcon(R.drawable.tab1));
       mTab.addTab(mTab.newTab().setText("网页").setIcon(R.drawable.tab2));
        //创建Fragment对象
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        //frament管理器
        manager = getSupportFragmentManager();
        //开启事务
        final FragmentTransaction transaction = manager.beginTransaction();
        //添加事务 提交事务
        transaction.add(R.id.contain, fragment1).add(R.id.contain, fragment2).hide(fragment2).commit();
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction1 = manager.beginTransaction();
                if (tab.getText().equals("首页")) {
                    transaction1.show(fragment1).hide(fragment2);

                } else if (tab.getText().equals("网页")) {
                    transaction1.show(fragment2).hide(fragment1);

                }
                transaction1.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mContain = (FrameLayout) findViewById(R.id.contain);
    }
}

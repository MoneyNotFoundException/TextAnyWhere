package com.zhouzining.testfortext.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhouzining.testfortext.Constans;
import com.zhouzining.testfortext.R;
import com.zhouzining.testfortext.base.BaseActivity;
import com.zhouzining.testfortext.bean.WriteLvBean;
import com.zhouzining.testfortext.fragment.MineFragment;
import com.zhouzining.testfortext.fragment.MoneyWriteFragment;
import com.zhouzining.testfortext.fragment.TextWriteFragment;
import com.zhouzining.testfortext.util.SaveAndGetUtils;
import com.zhouzining.testfortext.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContext(MainActivity.this);
        this.setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    public TabLayout homeTab;
    public NoScrollViewPager homePager;
//    public MyViewPager myTabPager;

    @Override
    protected void initView() {
        homeTab = findViewById(R.id.main_tab);
        homePager = findViewById(R.id.main_vp);
//        myTabPager = findViewById(R.id.mytabpager);
    }

    public ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void initConfig() {
//        添加viewpager的数据源
        fragments.add(0, new TextWriteFragment());
        fragments.add(1, new MoneyWriteFragment());
        fragments.add(2, new MineFragment());
//        myTabPager.addFragmentData(fragments,getSupportFragmentManager());
//        预缓存的数量
        homePager.setOffscreenPageLimit(3);
//        myTabPager.setOffscreenPageLimit(3);
        homePager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (fragments.size() > 0) {
                    return fragments.get(position);
                }
                return new TextWriteFragment();
            }

            @Override
            public int getCount() {
                if (fragments.size() > 0) {
                    return fragments.size();
                }
                return 1;
            }
        });

        homeTab.setupWithViewPager(homePager);

//        设置tab和viewpager的关联
        homeTab.getTabAt(0).setCustomView(getTabView(0));
        homeTab.getTabAt(1).setCustomView(getTabView(1));
        homeTab.getTabAt(2).setCustomView(getTabView(2));
        ArrayList<View> views = new ArrayList<>();
        views.add(getTabView(0));
        views.add(getTabView(1));
        views.add(getTabView(2));
        homeTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        ImageView iv_title = view.findViewById(R.id.item_tab_iv);
        TextView tv_title = view.findViewById(R.id.item_tab_tv);
        switch (position) {
            case 0:
                tv_title.setText("1");
                break;
            case 1:
                tv_title.setText("2");

                break;
            case 2:
                tv_title.setText("3");

                break;
            default:
                tv_title.setText("default");
                break;
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        这里是对创建和编辑笔记时候的判断
        if (requestCode == Integer.parseInt(Constans.INTENT_REQUEST1)) {
//            创建空数据的request
            if ((resultCode == Integer.parseInt(Constans.INTENT_RESULT1))) {
//                成功写入数据并保存,保存数据的操作在TextDetailActivty里面做了
            } else {
//                 没有写入数据，删除最后一条空的数据
                SaveAndGetUtils utils = SaveAndGetUtils.getInstance(MainActivity.this);
                utils.createDB(Constans.DBNAME_TEXTWRITE, null);
                HashMap<String, String> deleteMap = new HashMap<>();
                deleteMap.put("writeTextId", ((WriteLvBean)
                        (utils.getNewData(WriteLvBean.class))).getWriteTextId() + "");
                utils.deleteData(deleteMap);
            }
        }
    }

}

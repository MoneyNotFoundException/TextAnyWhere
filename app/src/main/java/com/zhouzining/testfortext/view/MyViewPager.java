package com.zhouzining.testfortext.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhouzining.testfortext.R;

import java.util.ArrayList;

/**
 * Created by Walter on 2018/1/8.
 */

public class MyViewPager extends LinearLayout {
    public MyViewPager(Context context) {
        super(context);
        initView(context);
    }

    public MyViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyViewPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_myviewpager, null);
        viewPager = view.findViewById(R.id.view_viewpager_vp);
        tabLayout = view.findViewById(R.id.view_viewpager_tab);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
//        继承view的话没有addview方法
        addView(view);
    }

    public ViewPager viewPager;
    public TabLayout tabLayout;

    //添加多个fragment到viewpager里面
    public void addFragmentData(final ArrayList<Fragment> datas, FragmentManager manager) {
        viewPager.setAdapter(new FragmentPagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                if (datas.size() > 0)
                    return datas.get(position);
                return null;
            }

            @Override
            public int getCount() {
                if (datas.size() > 0)
                    return datas.size();
                return 1;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    //设置预加载的数量
    public void setOffscreenPageLimit(int num) {
        viewPager.setOffscreenPageLimit(num);
    }

    //设置tab的样子的数量
    public void setCustomView(ArrayList<View> views) {
        for (int i = 0; i < views.size(); i++) {
            tabLayout.getTabAt(i).setCustomView(views.get(i));
        }
    }

    //    设置tab点击的效果
    public void setOnTabSelectedListener(TabLayout.OnTabSelectedListener listener) {
        tabLayout.setOnTabSelectedListener(listener);
    }
}

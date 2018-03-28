package com.zhouzining.testfortext.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.zhouzining.testfortext.R;
import com.zhouzining.testfortext.base.BaseActivity;
import com.zhouzining.testfortext.view.titanic.Titanic;
import com.zhouzining.testfortext.view.titanic.TitanicTextView;
import com.zhouzining.testfortext.view.titanic.Typefaces;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Walter on 2018/1/2.
 */

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContext(LaunchActivity.this);
        setContentView(R.layout.activity_launch);
//        TestEx.test(this);

        ShimmerTextView goodMemory = findViewById(R.id.goodmemory);
        TitanicTextView tv = findViewById(R.id.launch_appname_tv);
        tv.setTypeface(Typefaces.get(this, "ziti.ttf"));
        goodMemory.setTypeface(Typefaces.get(this, "ziti.ttf"));
        new Titanic().start(tv);

        Shimmer shimmer = new Shimmer();
        shimmer.start(goodMemory);

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initConfig() {

    }


    @Override
    protected void initData() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                LaunchActivity.super.finishSelf();
            }
        }, 3000);
    }

    //    分渠道打包需要
    public String getStoreName() {
        ActivityInfo info = null;
        try {
            info = LaunchActivity.this.getPackageManager()
                    .getActivityInfo(getComponentName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String storeName = info.metaData.getString("STORE_NAME");
        if (storeName == null) {
            storeName = "";
        }
        return storeName;
    }

    @Override
    public void onClick(View v) {

    }
}

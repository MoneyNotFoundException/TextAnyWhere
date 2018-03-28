package com.zhouzining.testfortext.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Walter on 2018/1/2.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    public String TAG = "BaseActivity";
    public Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initConfig();
    }

    protected void setContext(Activity activity) {
        this.activity = activity;
    }

    protected abstract void initView();

    protected abstract void initConfig();

    protected abstract void initData();

    protected void finishSelf() {
        activity.finish();
    }
}

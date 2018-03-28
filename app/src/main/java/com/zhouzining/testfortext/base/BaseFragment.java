package com.zhouzining.testfortext.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Walter on 2018/1/2.
 */

public abstract class BaseFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    public String TAG = "BaseFragment";
    public Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    protected void setContext(Activity activity) {
        this.activity = activity;
    }

    protected void init(View view) {
        initView(view);
        initData();
        initConfig();
    }

    protected Activity getMyContext() {
        return activity;
    }

    protected abstract void initView(View view);

    protected abstract void initConfig();

    protected abstract void initData();

    protected void finishSelf() {
        activity.finish();
    }
}

package com.zhouzining.testfortext.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhouzining.testfortext.R;
import com.zhouzining.testfortext.base.BaseFragment;

/**
 * Created by Walter on 2018/1/8.
 */

public class MoneyWriteFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.money_write_fragment, container, false);
        super.setContext(this.getActivity());
        return view;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initConfig() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {

    }
}

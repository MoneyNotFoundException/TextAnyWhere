package com.zhouzining.testfortext.util;

import android.content.Context;
import android.widget.Toast;

import com.zhouzining.testfortext.MyApplication;

/**
 * Created by Walter on 2018/1/2.
 */

public class ToastUtils {

    public static void toast() {
        toast("ToastUtils");
    }

    public static void toast(String msg) {
        if (msg.equals("") || msg == null) {
            msg = "ToastUtils";
        }
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void toast(String msg, int time) {
        if (msg.equals("") || msg == null) {
            msg = "ToastUtils";
        }
        toast(MyApplication.getInstance(), msg, time);
    }

    public static void toast(Context context, String msg, int time) {
        if (msg.equals("") || msg == null) {
            msg = "ToastUtils";
        }
        Toast.makeText(context, msg, time).show();
    }
}

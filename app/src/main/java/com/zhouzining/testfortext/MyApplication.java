package com.zhouzining.testfortext;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.yixia.camera.VCamera;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Created by Walter on 2018/1/2.
 * 在application里面应该初始化所有全局变量
 * 包括友盟统计，后台所要求的激活，分渠道打包时需要的数据
 */

public class MyApplication extends Application {
    public static MyApplication application;

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        Stetho.initializeWithDefaults(this);
//        初始化拍摄的部分需要
//        initVideo();
    }

    public static String VIDEO_PATH = Constans.VIDEO_PATH;

    public void initVideo() {
        VIDEO_PATH += String.valueOf(System.currentTimeMillis());
        File file = new File(VIDEO_PATH);
        if (!file.exists()) file.mkdirs();

        //设置视频缓存路径
        VCamera.setVideoCachePath(VIDEO_PATH);

        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);

        // 初始化拍摄SDK，必须
        VCamera.initialize(this);
    }
}

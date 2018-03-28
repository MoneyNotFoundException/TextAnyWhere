package com.zhouzining.testfortext;

import android.content.Context;
import android.util.Log;

import com.zhouzining.testfortext.util.HttpUtils;
import com.zhouzining.testfortext.util.LogUtils;
import com.zhouzining.testfortext.util.SaveAndGetUtils;
import com.zhouzining.testfortext.util.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Walter on 2018/1/5.
 */

public class TestEx {
    public static void test(Context context) {

        HashMap<String, String> map = new HashMap<>();
        map.put("appname", "抢红包助手");
        map.put("version", "1.1");
        map.put("ClientID", SystemUtils.getPid(context));
        HttpUtils.doAsk(Constans.url + SystemUtils.mapToUrl(map), null, new HttpUtils.HttpListener() {
            @Override
            public void success(String result) {
                Log.e("123", result);
            }

            @Override
            public void error(String result) {
                Log.e("123", result);

            }
        });
        HttpUtils.doAsk(Constans.url1, null, new HttpUtils.HttpListener() {
            @Override
            public void success(String result) {
                Log.e("aaa", result);
            }

            @Override
            public void error(String result) {
                Log.e("aaa", result);

            }
        });
        HashMap<String, String> dbMap = new HashMap<>();
        dbMap.put("dbName", "person");
        dbMap.put("dbKey", "personId");
        dbMap.put("dbType", "int");
        dbMap.put("name", "string");
        dbMap.put("age", "int");
        dbMap.put("sex", "boolean");
        dbMap.put("height", "double");
//        NoteDataHelper helper = new NoteDataHelper(context, "test", 1, dbMap);
//        SQLiteDatabase db = helper.getDb(helper);

        HashMap<String, String> mapInsert = new HashMap<>();
        mapInsert.put("name", "张三");
        mapInsert.put("age", "18");
        mapInsert.put("sex", "0");
        mapInsert.put("height", "165.00");
        HashMap<String, String> mapUpWhere = new HashMap<>();
        mapUpWhere.put("name", "张三");
//        HashMap<String, String> mapLikeWhere = new HashMap<>();
//        mapLikeWhere.put("name", "张");
//
//        HashMap<String, String> mapUpdata = new HashMap<>();
//        mapUpdata.put("name", "张三");
//        mapUpdata.put("age", "20");
//        mapUpdata.put("sex", "1");
//        mapUpdata.put("height", "175.00");
//
//        HashMap<String, String> mapSelect = new HashMap<>();
//        mapSelect.put("name", "张三");
//
//        HashMap<String, String> mapDelete = new HashMap<>();
//        mapDelete.put("name", "张三");
//
//        ArrayList<Object> list = null;
//
//        helper.insert(db, mapInsert);
//
//        list = helper.select(db, mapUpWhere, TestBean.class);
//        for (int i = 0; i < list.size(); i++) {
//            LogUtils.e("mapUpWhere", list.get(i).toString());
//            TestBean bean = (TestBean) list.get(i);
//        }
//        list = helper.selectLike(db, mapLikeWhere, TestBean.class);
//        for (int i = 0; i < list.size(); i++) {
//            LogUtils.e("mapLikeWhere", list.get(i).toString());
//            TestBean bean = (TestBean) list.get(i);
//        }
//
//        helper.update(db, mapUpdata, mapUpWhere);
//
//        list = helper.select(db, mapUpWhere, TestBean.class);
//        for (int i = 0; i < list.size(); i++) {
//            LogUtils.e("mapUpWhere", list.get(i).toString());
//            TestBean bean = (TestBean) list.get(i);
//        }
//
//        helper.delete(db, mapUpWhere);
//
//        list = helper.select(db, mapUpWhere, TestBean.class);
//        for (int i = 0; i < list.size(); i++) {
//            LogUtils.e("mapUpWhere", list.get(i).toString());
//            TestBean bean = (TestBean) list.get(i);
//        }

        SaveAndGetUtils utils = SaveAndGetUtils.getInstance(context);
        HashMap<String, String> dbMapNew = new HashMap<>();
        dbMapNew.put("dbName", "person");
        dbMapNew.put("dbKey", "personId");
        dbMapNew.put("dbType", "int");
        dbMapNew.put("name", "string");
        dbMapNew.put("age", "int");
        dbMapNew.put("sex", "boolean");
        dbMapNew.put("height", "double");
        utils.createDB("test", dbMapNew);

        HashMap<String, String> mapInsertNew = new HashMap<>();
        mapInsertNew.put("name", "张三");
        mapInsertNew.put("age", "18");
        mapInsertNew.put("sex", "0");
        mapInsertNew.put("height", "165.00");
        utils.saveData(mapInsertNew);
        utils.saveData(mapInsertNew);

        ArrayList<TestBean> beanArrayList = (ArrayList<TestBean>) utils.getData(mapUpWhere, TestBean.class);
        LogUtils.e("beanArrayList", "" + beanArrayList.size());

        for (int i = 0; i < beanArrayList.size(); i++) {
            LogUtils.e("SaveAndGetUtils", beanArrayList.get(i).toString());
        }
    }

}

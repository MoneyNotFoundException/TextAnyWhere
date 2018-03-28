package com.zhouzining.testfortext.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.daimajia.swipe.util.Attributes;
import com.zhouzining.testfortext.Constans;
import com.zhouzining.testfortext.R;
import com.zhouzining.testfortext.activity.TextDetailActivity;
import com.zhouzining.testfortext.adapter.ListViewAdapter;
import com.zhouzining.testfortext.base.BaseFragment;
import com.zhouzining.testfortext.bean.WriteLvBean;
import com.zhouzining.testfortext.util.SaveAndGetUtils;
import com.zhouzining.testfortext.util.SystemUtils;
import com.zhouzining.testfortext.view.DragButton;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Walter on 2018/1/8.
 */

public class TextWriteFragment extends BaseFragment {
    //    private WriteLVAdapter adapter;
    private ListViewAdapter adapter;
    private List datas;
    private SaveAndGetUtils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_write_fragment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        super.setContext(this.getActivity());
        super.init(view);
        return view;
    }

    //    public SatelliteMenu menu;
    public ListView lv;
    public DragButton dgBtn;

    //    初始化View
    @Override
    protected void initView(View view) {
//        menu = view.findViewById(R.id.menu_satellite);
        lv = view.findViewById(R.id.write_lv);
        dgBtn = view.findViewById(R.id.write_dragbtn);
    }

    //    初始化后进行的设置
    @Override
    protected void initConfig() {

        dgBtn.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TextWriteFragment.this.getActivity(), TextDetailActivity.class);
                intent.putExtra("data", (WriteLvBean) adapter.getItem(position));
                intent.putExtra("from", "TextWriteFragment");
                TextWriteFragment.this.getActivity()
                        .startActivity(intent);
            }
        });

//        下面是对listview的侧拉进行操作

        adapter = new ListViewAdapter(TextWriteFragment.this.getActivity(), datas);
        lv.setAdapter(adapter);
        adapter.setMode(Attributes.Mode.Single);
        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });
    }

    @Override
    protected void initData() {
        //对首页的menu进行初始化
//        List<SatelliteMenuItem> items = new ArrayList<>();
//        items.add(new SatelliteMenuItem(3, R.mipmap.ic_launcher));
//        items.add(new SatelliteMenuItem(2, R.mipmap.ic_launcher));
//        items.add(new SatelliteMenuItem(1, R.mipmap.ic_launcher));
//        menu.addItems(items);

//        对首页的数据进行初始化
        utils = SaveAndGetUtils.getInstance(TextWriteFragment.this.getContext());
        utils.createDB(Constans.DBNAME_TEXTWRITE, null);
        datas = utils.getAllData(WriteLvBean.class);
        //如果没有数据就加载默认用户须知
        if (datas == null || datas.size() == 0) {
            configData();
            datas.clear();
            datas.addAll(utils.getAllData(WriteLvBean.class));
        }
//        adapter = new WriteLVAdapter(this.getMyContext(), datas);
//        lv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            添加按钮
            case R.id.write_dragbtn:
                newData();
                break;
            default:
                break;
        }

    }

    public void newData() {
//        先在数据库中添加一条空数据
        SaveAndGetUtils utils = SaveAndGetUtils.getInstance(TextWriteFragment.this.getContext());
        utils.createDB(Constans.DBNAME_TEXTWRITE, null);
        String time = SystemUtils.getCurrentTime();
        HashMap<String, String> newMap = new HashMap<>();
        newMap.put("time", time);
        newMap.put("text", "");
        newMap.put("title", "");
        utils.saveData(newMap);


//        通过和listview的点击事件一样的方式进入编辑页面
        Intent intent = new Intent(TextWriteFragment.this.getActivity(), TextDetailActivity.class);
        intent.putExtra("data", (WriteLvBean)
                (utils.getAllData(WriteLvBean.class).get(utils.getAllData(WriteLvBean.class).size() - 1)));
        intent.putExtra("from", "TextWriteFragment");
        TextWriteFragment.this.getActivity()
                .startActivityForResult(intent, Integer.parseInt(Constans.INTENT_REQUEST1));
    }

    public void deleteData() {

    }

    //    没有数据的时候加载默认用户须知
    public void configData() {
        SaveAndGetUtils utils = SaveAndGetUtils.getInstance(TextWriteFragment.this.getContext());
        utils.createDB(Constans.DBNAME_TEXTWRITE, null);
        String time = "1994-01-08";
        String text = "这里是用户须知，你晓得不？";
        String title = "用户须知";
        HashMap<String, String> insertMap = new HashMap<>();
        insertMap.put("time", time);
        insertMap.put("text", text);
        insertMap.put("title", title);
        utils.saveData(insertMap);
    }

    //    每次退出重新进入的时候进行保存
    @Override
    public void onResume() {
        super.onResume();
        adapter.getDatas().clear();
        datas.addAll(utils.getAllData(WriteLvBean.class));
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
    }
}

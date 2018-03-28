package com.zhouzining.testfortext.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.zhouzining.testfortext.Constans;
import com.zhouzining.testfortext.R;
import com.zhouzining.testfortext.bean.WriteLvBean;
import com.zhouzining.testfortext.util.SaveAndGetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<Object> datas;

    public ListViewAdapter(Context mContext, List<Object> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    @Override
    public View generateView(final int position, ViewGroup parent) {
        View convertView = null;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_write_lv, null);
            holder.write_lv_item_title_tv = convertView.findViewById(R.id.write_lv_item_title_tv);
            holder.write_lv_item_time_tv = convertView.findViewById(R.id.write_lv_item_time_tv);
            holder.write_lv_item_voice_iv = convertView.findViewById(R.id.write_lv_item_voice_iv);
            holder.write_lv_item_pic_iv = convertView.findViewById(R.id.write_lv_item_pic_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SwipeLayout swipeLayout = convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100)
                        .playOn(layout.findViewById(R.id.trash));
            }
        });
        Button delete = convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认删除吗?")
                        .setContentText("删除后本文件不可恢复！")
                        .setCancelText("不，我点错了!")
                        .setConfirmText("是，干掉他!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
//                                取消删除
                                sDialog.setTitleText("取消!")
                                        .setContentText("取消删除")
                                        .setConfirmText("确认")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sDialog.cancel();
                                                swipeLayout.close();
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
//                                删除列表数据
                                SaveAndGetUtils utils = SaveAndGetUtils.getInstance(mContext);
                                utils.createDB(Constans.DBNAME_TEXTWRITE, null);
                                WriteLvBean bean = (WriteLvBean) datas.get(position);
                                HashMap<String, String> whereMap = new HashMap<>();
                                whereMap.put("writeTextId", bean.getWriteTextId() + "");
                                utils.deleteData(whereMap);
                                sDialog.setTitleText("删除!")
                                        .setContentText("本文件已经被删除!")
                                        .setConfirmText("确认")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                datas.remove(position);
                                                ListViewAdapter.this.notifyDataSetChanged();
                                                swipeLayout.close();
                                                sDialog.cancel();
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
            }
        });

//        对数据源data进行填充
        List<WriteLvBean> datas = new ArrayList<>();
        for (int i = 0; i < this.datas.size(); i++) {
            WriteLvBean bean = (WriteLvBean) this.datas.get(i);
            datas.add(bean);
        }
        holder.write_lv_item_title_tv.setText(datas.get(position).getTitle());
        holder.write_lv_item_time_tv.setText(datas.get(position).getTime());

//         如果没有设置图片和声音就不显示图标
        if (datas.get(position).isPic) {
            holder.write_lv_item_pic_iv.setVisibility(View.VISIBLE);
        } else {
            holder.write_lv_item_pic_iv.setVisibility(View.INVISIBLE);
        }
        if (datas.get(position).isVoice()) {
            holder.write_lv_item_voice_iv.setVisibility(View.VISIBLE);
        } else {
            holder.write_lv_item_voice_iv.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_write_lv, null);
            holder.write_lv_item_title_tv = convertView.findViewById(R.id.write_lv_item_title_tv);
            holder.write_lv_item_time_tv = convertView.findViewById(R.id.write_lv_item_time_tv);
            holder.write_lv_item_voice_iv = convertView.findViewById(R.id.write_lv_item_voice_iv);
            holder.write_lv_item_pic_iv = convertView.findViewById(R.id.write_lv_item_pic_iv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SwipeLayout swipeLayout = convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100)
                        .playOn(layout.findViewById(R.id.trash));
            }
        });
        Button delete = convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确认删除吗?")
                        .setContentText("删除后本文件不可恢复！")
                        .setCancelText("不，我点错了!")
                        .setConfirmText("是，干掉他!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
//                                取消删除
                                sDialog.setTitleText("取消!")
                                        .setContentText("取消删除")
                                        .setConfirmText("确认")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sDialog.cancel();
                                                swipeLayout.close();
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
//                                删除列表数据
                                SaveAndGetUtils utils = SaveAndGetUtils.getInstance(mContext);
                                utils.createDB(Constans.DBNAME_TEXTWRITE, null);
                                WriteLvBean bean = (WriteLvBean) datas.get(position);
                                HashMap<String, String> whereMap = new HashMap<>();
                                whereMap.put("writeTextId", bean.getWriteTextId() + "");
                                utils.deleteData(whereMap);
                                sDialog.setTitleText("删除!")
                                        .setContentText("本文件已经被删除!")
                                        .setConfirmText("确认")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                datas.remove(position);
                                                ListViewAdapter.this.notifyDataSetChanged();
                                                swipeLayout.close();
                                                sDialog.cancel();
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();
            }
        });

//        对数据源data进行填充
        List<WriteLvBean> datas = new ArrayList<>();
        for (int i = 0; i < this.datas.size(); i++) {
            WriteLvBean bean = (WriteLvBean) this.datas.get(i);
            datas.add(bean);
        }
        holder.write_lv_item_title_tv.setText(datas.get(position).getTitle());
        holder.write_lv_item_time_tv.setText(datas.get(position).getTime());

//         如果没有设置图片和声音就不显示图标
        if (datas.get(position).isPic) {
            holder.write_lv_item_pic_iv.setVisibility(View.VISIBLE);
        } else {
            holder.write_lv_item_pic_iv.setVisibility(View.INVISIBLE);
        }
        if (datas.get(position).isVoice()) {
            holder.write_lv_item_voice_iv.setVisibility(View.VISIBLE);
        } else {
            holder.write_lv_item_voice_iv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Object> getDatas() {
        return datas;
    }

    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }

    class ViewHolder {
        TextView write_lv_item_title_tv;
        TextView write_lv_item_time_tv;
        ImageView write_lv_item_voice_iv;
        ImageView write_lv_item_pic_iv;
    }
}

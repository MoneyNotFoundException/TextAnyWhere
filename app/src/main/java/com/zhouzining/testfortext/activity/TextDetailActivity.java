package com.zhouzining.testfortext.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhaoshuang.weixinrecordeddemo.MyVideoView;
import com.example.zhaoshuang.weixinrecordeddemo.VideoMainActivity;
import com.yixia.camera.MediaRecorderBase;
import com.zhouzining.testfortext.Constans;
import com.zhouzining.testfortext.R;
import com.zhouzining.testfortext.base.BaseActivity;
import com.zhouzining.testfortext.bean.WriteLvBean;
import com.zhouzining.testfortext.util.LogUtils;
import com.zhouzining.testfortext.util.SaveAndGetUtils;
import com.zhouzining.testfortext.util.ToastUtils;
import com.zhouzining.testfortext.view.satemenu.SatelliteMenu;
import com.zhouzining.testfortext.view.satemenu.SatelliteMenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Walter on 2018/1/29.
 */

public class TextDetailActivity<T> extends BaseActivity {

    private WriteLvBean data;
    private String from;
    private String name = "系统错误，请联系系统管理员";
    private SatelliteMenu menu;
    private String info = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.setContext(TextDetailActivity.this);
        setContentView(R.layout.activity_textdetail);
        super.onCreate(savedInstanceState);
    }

    private TextView doneTv;
    private TextView backTv;
    private LinearLayout backLayout;
    private LinearLayout textdetail_add_layout;

    @Override
    protected void initView() {
        doneTv = findViewById(R.id.textdetail_done_tv);
        backTv = findViewById(R.id.textdetail_back_tv);
        backLayout = findViewById(R.id.textdetail_back_layout);
        menu = findViewById(R.id.menu_textdetail);
        textdetail_add_layout = findViewById(R.id.textdetail_add_layout);
    }

    @Override
    protected void initConfig() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "ziti.ttf");
        doneTv.setTypeface(typeface);
        backTv.setTypeface(typeface);

        backLayout.setOnClickListener(TextDetailActivity.this);
        doneTv.setOnClickListener(TextDetailActivity.this);
        menu.setOnItemClickedListener(new SatelliteMenu.SateliteClickedListener() {

            public void eventOccured(int id) {
                if (id < 1) {
                    ToastUtils.toast(getString(R.string.system_error));
                    Log.e("error95", "id < 1");
                    return;
                }
                switch (id) {
                    case 1:
//                        添加视频
                        startActivityForResult(new Intent(TextDetailActivity.this,
                                VideoMainActivity.class), 0);
                        break;
                    case 2:
//                        添加音频
                        break;
                    case 3:
//                        添加图片
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 2);
                        break;
                    default:
                        break;
                }
            }
        });
//        所有配置完成后开始加载
        if (data.getText().equals("") || data.getText() == null) {
//            如果是刚刚创建的或者本身没有文字内容（包括图片和视频等）
            addView(textType, "");
        } else {
//            对传过来的info进行解码，转换为view的格式
            decode(data.getText());
        }
    }

    @Override
    protected void initData() {
//        如果没收到传过来的data就视为系统错误
        Intent intent = getIntent();
        data = (WriteLvBean) intent.getSerializableExtra("data");
        from = intent.getStringExtra("from");
        if (from == null || from.equals("") || data == null) {
            ToastUtils.toast(getString(R.string.system_error));
            Log.e("error140", "from == null || from.equals(\"\") || data == null");
            return;
        }

//        对menu进行配置
        List<SatelliteMenuItem> items = new ArrayList<>();
        items.add(new SatelliteMenuItem(3, R.mipmap.ic_launcher_round));
        items.add(new SatelliteMenuItem(2, R.mipmap.ic_launcher_round));
        items.add(new SatelliteMenuItem(1, R.mipmap.ic_launcher_round));
        menu.addItems(items);

    }


    @Override
    public void onClick(View v) {
        if (v == null) {
            ToastUtils.toast(getString(R.string.system_error));
            Log.e("error158", "v == null");

            return;
        }
        switch (v.getId()) {
            case R.id.textdetail_done_tv:
                try {
                    if (data.getTitle().equals("")
                            || data.getTitle() == null) {
                        showRenameDialog();
                    } else {
//                        对现有的view进行编码，并转换为string格式进行保存
                        encode();
                        saveDataChanged();
                        TextDetailActivity.this.finishSelf();
                    }
                } catch (Exception e) {
                    Log.e("error175", e.toString());
                    ToastUtils.toast(getString(R.string.system_error));
                }
                break;
            case R.id.textdetail_back_layout:
                TextDetailActivity.this.finishSelf();
                break;
            default:
                ToastUtils.toast(getString(R.string.system_error));
                Log.e("error184", "default");

                break;
        }
    }


    public String code0 = "%zzn%";
    private ArrayList<String> decodeViewsInfo = new ArrayList<>();
    private ArrayList<String> encodeViewsInfo = new ArrayList<>();
    private ArrayList<EditText> encodeTextView = new ArrayList<>();
    private String viewVideo = "mp4";
    private String viewImage = "jpg";
    private String viewVoice = "mp3";

    //    解码为view
    /*
    * 解码格式为
    * abc[zzn /sdcard/..... zzn]
    * 对[zzn  zzn]进行匹配解码
    * 并填充相应的view
    * */
    public void decode(String info) {
//        只有同时存在code0/1的时候才能说明有其他的类容（视频、图片等）
        if (info.contains(code0)) {
            String infos[] = info.split(code0);
            for (int i = 0; i < infos.length; i++) {
                decodeViewsInfo.add(infos[i]);
            }
            for (int i = 0; i < decodeViewsInfo.size(); i++) {
//                这里表示一定是除了textview之外的 view
                if (i % 2 == 1) {
                    String path = decodeViewsInfo.get(i);
                    StringBuffer pathBuffer = new StringBuffer(path);
                    String type = pathBuffer.substring(pathBuffer.length() - 4, pathBuffer.length());
                    int viewType = 0;
                    if (type.contains(viewVideo)) {
                        viewType = videoType;
                    } else if (type.contains(viewImage)) {
                        viewType = imageType;
                    } else if (type.contains(viewVoice)) {
                        viewType = voiceType;
                    }
                    addView(viewType, path);
                } else {
//                    addTextView(decodeViewsInfo.get(i));
                    addView(textType, decodeViewsInfo.get(i));
                }
            }
            addView(textType, "");
        } else {
            addTextView(info);
        }
    }

    //    编码为string进行保存
    /*
    * 编码格式为
    * 直接将相应的路径保存进去
    * abc[zzn /sdcard/..... zzn]
    * */
    public void encode() {
        if (encodeTextView.size() == 0 && encodeViewsInfo.size() == 0) {
            return;
        }
        StringBuffer infoBuffer = new StringBuffer(info);
        for (int i = 0; i < encodeTextView.size() + encodeViewsInfo.size(); i++) {
//            说明是TextView
            LogUtils.e(TAG, "encodeTextView.size()  " + encodeTextView.size());
            LogUtils.e(TAG, "encodeViewsInfo.size()  " + encodeViewsInfo.size());
            if (i % 2 == 0) {
                infoBuffer.append(encodeTextView.get(i / 2).getText());
            } else {
//                不是TextView，要先在前后添加标识符，在添加path
                infoBuffer.append(code0);
                infoBuffer.append(encodeViewsInfo.get(i / 2));
                infoBuffer.append(code0);
            }
        }
        info = infoBuffer.toString();
        data.setText(info);
    }

    private void saveDataChanged() {
        SaveAndGetUtils utils = SaveAndGetUtils.getInstance(TextDetailActivity.this);
        utils.createDB(Constans.DBNAME_TEXTWRITE, null);
        int writeTextId = data.getWriteTextId();

        HashMap<String, String> whereMap = new HashMap<>();
        whereMap.put("writeTextId", writeTextId + "");

        HashMap<String, String> updateMap = new HashMap<>();
        updateMap.put("text", info);
        updateMap.put("title", (data.getTitle().equals("") || data.getTitle() == null)
                ? name : data.getTitle());
        utils.upDate(updateMap, whereMap);

        setResult(Integer.parseInt(Constans.INTENT_RESULT1));
    }

    //    重命名对话框
    public void showRenameDialog() {

        AlertDialog.Builder renameBuilder = new AlertDialog.Builder(this);
        // 获取布局
        View view = View.inflate(TextDetailActivity.this, R.layout.dialog_rename, null);
        // 获取布局中的控件
        final EditText renameEt = view.findViewById(R.id.rename_dialog_et);
        Button sure = view.findViewById(R.id.rename_dialog_sure);
        Button cancel = view.findViewById(R.id.rename_dialog_cancel);
        // 设置参数
        renameBuilder.setView(view);
        // 创建对话框
        final AlertDialog alertDialog = renameBuilder.create();
//        设置点击监听
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                name = renameEt.getText().toString();
                if (name.equals("") || name == null) {
                    ToastUtils.toast(getString(R.string.input_name));
                    return;
                }
                data.setTitle(name);
                encode();
                saveDataChanged();
                TextDetailActivity.this.finishSelf();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    public final int videoType = 1;
    public final int imageType = 2;
    public final int voiceType = 3;
    public final int textType = 4;

    //这里对添加view进行操作
    public void addView(int type, String info) {
        switch (type) {
            case videoType:
                addVideoView(info);
//                每次添加其他view之后要重新添加一次TextView进行编辑
                encodeViewsInfo.add(info);
                break;
            case imageType:
                addImageView(info);
                encodeViewsInfo.add(info);
                break;
            case voiceType:
                addVoiceView(info);
                encodeViewsInfo.add(info);
                break;
            case textType:
                addTextView(info);
                break;
            default:
                break;
        }
    }

    public void addVideoView(String path) {
        if (textdetail_add_layout == null) {
            ToastUtils.toast(getString(R.string.system_error));
            Log.e("error365", "textdetail_add_layout == null");
            return;
        }
        View videoView = LayoutInflater.from(TextDetailActivity.this)
                .inflate(R.layout.detail_video, null);
        final MyVideoView myVideoView = videoView.findViewById(R.id.detail_video);
        myVideoView.setVideoPath(path);
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                myVideoView.setLooping(true);
                myVideoView.start();
                float widthF = myVideoView.getVideoWidth() * 1f / MediaRecorderBase.VIDEO_HEIGHT;
                float heightF = myVideoView.getVideoHeight() * 1f / MediaRecorderBase.VIDEO_WIDTH;
                ViewGroup.LayoutParams layoutParams = myVideoView.getLayoutParams();
                layoutParams.width = (int) (getWindowManager().getDefaultDisplay().getWidth() * widthF * 0.8);
                layoutParams.height = (int) (getWindowManager().getDefaultDisplay().getHeight() * heightF * 0.2);
                myVideoView.setLayoutParams(layoutParams);
            }
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        textdetail_add_layout.addView(videoView, lp);
    }

    public void addTextView(String text) {
        if (textdetail_add_layout == null) {
            ToastUtils.toast(getString(R.string.system_error));
            Log.e("error394", "textdetail_add_layout == null");
            return;
        }
        View textView = LayoutInflater.from(TextDetailActivity.this)
                .inflate(R.layout.detail_text, null);
        final EditText myTextView = textView.findViewById(R.id.detail_text);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "ziti.ttf");
        myTextView.setTypeface(typeface);
        myTextView.setText(text);
        myTextView.setFocusable(true);
        myTextView.setFocusableInTouchMode(true);
        myTextView.requestFocus();//获取焦点 光标出现
        encodeTextView.add(myTextView);
        LogUtils.e(TAG, "encodeTextView.add  " + text);
        textdetail_add_layout.addView(textView);
    }

    public void addVoiceView(final String path) {
        if (textdetail_add_layout == null) {
            ToastUtils.toast(getString(R.string.system_error));
            Log.e("error410", "textdetail_add_layout == null");
            return;
        }
        View videoView = LayoutInflater.from(TextDetailActivity.this)
                .inflate(R.layout.detail_voice, null);
        final TextView myVoiceTv = videoView.findViewById(R.id.detail_voice_time);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "ziti.ttf");
        myVoiceTv.setTypeface(typeface);
        String time = "";
//        获取录音的时长
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        time = "" + mediaPlayer.getDuration();
        myVoiceTv.setText(time);

        LinearLayout voiceDetail = videoView.findViewById(R.id.detail_voice);
        voiceDetail.setOnClickListener(new View.OnClickListener() {
            public MediaPlayer mPlayer;

            @Override
            public void onClick(View v) {
//                播放录音
                mPlayer = new MediaPlayer();
                if (mPlayer.isPlaying()) {
                    mPlayer.release();
                    mPlayer = null;
                } else {
                    try {
                        mPlayer.setDataSource(path);
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (IOException e) {
                        ToastUtils.toast(getString(R.string.system_error));
                        Log.e("error448", e.toString());
                    }
                }
            }
        });
        textdetail_add_layout.addView(videoView);
    }

    public void addImageView(String path) {
        if (textdetail_add_layout == null) {
            ToastUtils.toast(getString(R.string.system_error));
            Log.e("error459", "textdetail_add_layout == null");
            return;
        }
        View imageView = LayoutInflater.from(TextDetailActivity.this)
                .inflate(R.layout.detail_image, null);
        final ImageView myImageView = imageView.findViewById(R.id.detail_image);
        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            myImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage(), e);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        textdetail_add_layout.addView(imageView, lp);
    }

    /**
     * 当edittext中的文字被删除完毕后
     * 应该删除当前edittext的前一个view
     * 并且删除当前edittext，将焦点和光标移到上上个view中
     * 也就是上一个edittext之中
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        boolean flag = super.dispatchKeyEvent(event);
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() != KeyEvent.ACTION_UP) {
            for (int i = 0; i < encodeTextView.size(); i++) {
//                判断当前焦点是哪一个
                if (encodeTextView.get(i).isFocused()) {
//                    已经删除完了,并且不是第一个edittext
                    if (encodeTextView.get(i).getText().toString().trim().equals("") && i > 0) {
////                        删除上一个View并移除当前edittext
                        encodeTextView.get(i).setVisibility(View.GONE);
                        textdetail_add_layout.getChildAt(2 * i - 1).setVisibility(View.GONE);
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }


    //    这里是从拍摄/录音界面返回过来的
//    todo 录制语音的页面还没做好
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        拍摄视频返回
        if (requestCode == 0) {
            String resultPath = data.getStringExtra("path");
            addView(videoType, resultPath);
            addView(textType, "");
            return;
        }
//        拍摄照片返回
        if (requestCode == 2 && resultCode == RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

            FileOutputStream b = null;
            File file = new File(Constans.IMAGE_PATH);
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/Image/" + name;

            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            addView(imageType, fileName);
            addView(textType, "");
        }
    }
}

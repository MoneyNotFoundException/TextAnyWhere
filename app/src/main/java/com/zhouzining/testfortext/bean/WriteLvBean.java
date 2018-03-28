package com.zhouzining.testfortext.bean;

import java.io.Serializable;

/**
 * Created by Walter on 2018/1/10.
 */

public class WriteLvBean implements Serializable {
    public String title;
    public String time;
    public boolean isPic;
    public boolean isVoice;
    public String picPath;
    public String voicePath;
    public String text;
    public int writeTextId;

    public WriteLvBean(String title, String time, String text) {
        this(title, time, false, false, "", "", text);
    }


    public WriteLvBean(String title, String time, boolean isPic, boolean isVoice, String picPath, String voicePath, String text) {
        this.title = title;
        this.time = time;
        this.isPic = isPic;
        this.isVoice = isVoice;
        this.picPath = picPath;
        this.voicePath = voicePath;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isPic() {
        return isPic;
    }

    public void setPic(boolean pic) {
        isPic = pic;
    }

    public boolean isVoice() {
        return isVoice;
    }

    public void setVoice(boolean voice) {
        isVoice = voice;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public String getText() {
        return text;
    }

    public void setText(String info) {
        this.text = info;
    }

    public int getWriteTextId() {
        return writeTextId;
    }

    public void setWriteTextId(int writeTextId) {
        this.writeTextId = writeTextId;
    }

    @Override
    public String toString() {
        return "WriteLvBean{" +
                "title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", isPic=" + isPic +
                ", isVoice=" + isVoice +
                ", picPath='" + picPath + '\'' +
                ", voicePath='" + voicePath + '\'' +
                ", text='" + text + '\'' +
                ", writeTextId=" + writeTextId +
                '}';
    }
}

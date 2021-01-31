package com.wyp.wxplayer.bean;

/**
 * Created by Mr.Wang
 * Date  2016/9/7.
 * Email 1198190260@qq.com
 */
public class LyricBean {

    public String content;
    public long startPoint;

    public LyricBean(String content, long startPoint) {
        this.content = content;
        this.startPoint = startPoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(long startPoint) {
        this.startPoint = startPoint;
    }
}

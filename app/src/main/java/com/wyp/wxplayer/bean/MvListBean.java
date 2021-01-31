package com.wyp.wxplayer.bean;

import java.util.List;

/**
 * Created by Mr.Wang
 * Date  2016/9/4.
 * Email 1198190260@qq.com
 */
public class MvListBean {

    private int totalCount;
    private List<VideoBean> videos;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<VideoBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoBean> videos) {
        this.videos = videos;
    }
}

package com.wyp.wxplayer.fragment.mvpage;

import com.wyp.wxplayer.bean.VideoBean;

import java.util.List;

/**
 * Created by wyp on 2021/2/10.
 */

public interface MvChildMvp {

    interface Presenter{
        void loadData(String area, int offset, int size);
    }

    interface View{
        void setData(List<VideoBean> videos);
        void onError(int code, Exception e);
    }
}

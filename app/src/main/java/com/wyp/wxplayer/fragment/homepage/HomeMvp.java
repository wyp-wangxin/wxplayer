package com.wyp.wxplayer.fragment.homepage;

import com.wyp.wxplayer.bean.VideoBean;

import java.util.List;

/**
 * Created by wyp on 2021/1/31.
 */

public interface HomeMvp {

    /** Presenter 层接口,用于加载数据 */
    interface Presenter {
        void loadData(int offset, int size);
    }

    /** view 层接口，用于更新界面的回调 */
    interface View{
        void setData(List<VideoBean> videoBeen);
        void onError(int code, Exception e);
    }
}

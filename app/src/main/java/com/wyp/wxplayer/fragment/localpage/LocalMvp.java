package com.wyp.wxplayer.fragment.localpage;

import com.wyp.wxplayer.bean.LocalVideoBean;


import java.util.List;

/**
 * Created by Administrator on 2021/2/9.
 */

public interface LocalMvp {
    /** Presenter 层接口,用于加载数据 */
    interface Presenter {
        void loadData();
    }

    /** view 层接口，用于更新界面的回调 */
    interface View{
        void setData(List<LocalVideoBean> localVideoBean);
       // void onError(int code, Exception e);
    }
}

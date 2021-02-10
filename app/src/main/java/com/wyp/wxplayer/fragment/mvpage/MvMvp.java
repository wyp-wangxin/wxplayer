package com.wyp.wxplayer.fragment.mvpage;

import com.wyp.wxplayer.bean.AreaBean;

import java.util.List;

/**
 * Created by wyp on 2021/2/10.
 */

public interface MvMvp {
    interface Presener{
        void loadData();
    }

    interface View{
        void setData(List<AreaBean> areaBeen);
        void onError(int code, Exception e);
    }
}

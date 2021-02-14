package com.wyp.wxplayer.fragment.mvpage;

import com.wyp.wxplayer.URLProviderUtil;
import com.wyp.wxplayer.bean.AreaBean;
import com.wyp.wxplayer.http.BaseCallBack;
import com.wyp.wxplayer.http.HttpManager;

import java.util.List;

/**
 * Created by wyp on 2021/2/10.
 */

public class MvPresenter implements MvMvp.Presener {

    MvMvp.View view;

    public MvPresenter(MvMvp.View view) {
        this.view = view;
    }

    @Override
    public void loadData() {

    }


//    public void loadData(String area, int offset, int size) {
//        String url = URLProviderUtil.getMVareaUrl();
//
//        HttpManager.getInstance().get(url, new BaseCallBack<List<AreaBean>>() {
//            @Override
//            public void onFailure(int code, Exception e) {
//                view.onError(code,e);
//            }
//
//            @Override
//            public void onSuccess(List<AreaBean> areaBeen) {
//                view.setData(areaBeen);
//            }
//        });
//    }


}

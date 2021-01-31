package com.wyp.wxplayer.fragment.homepage;

import com.wyp.wxplayer.URLProviderUtil;
import com.wyp.wxplayer.bean.VideoBean;
import com.wyp.wxplayer.http.BaseCallBack;
import com.wyp.wxplayer.http.HttpManager;
import com.wyp.wxplayer.utils.myLog;

import java.util.List;

/**
 * Created by wyp on 2021/1/31.
 */

public class HomePresenter implements HomeMvp.Presenter {
    private static final String TAG = "HomePresenter";

    HomeMvp.View view;

    public HomePresenter(HomeMvp.View view) {
        this.view = view;
    }

    @Override
    public void loadData(int offset, int size) {
        String url = URLProviderUtil.getMainPageUrl(offset,size);
        myLog.e(TAG,"HomePresenter.loadData,开始加载数据,url="+url);

        HttpManager.getInstance().get(url, new BaseCallBack<List< VideoBean >>() {
            @Override
            public void onFailure(int code, Exception e) {
                view.onError(code,e);
            }

            @Override
            public void onSuccess(List< VideoBean > videoBeans) {
                view.setData(videoBeans);
            }
        });
    }

}

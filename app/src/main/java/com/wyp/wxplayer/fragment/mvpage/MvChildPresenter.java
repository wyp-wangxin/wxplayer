package com.wyp.wxplayer.fragment.mvpage;

import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.wxplayer.URLProviderUtil;
import com.wyp.wxplayer.bean.MvListBean;
import com.wyp.wxplayer.http.BaseCallBack;
import com.wyp.wxplayer.http.HttpManager;

/**
 * Created by wyp on 2021/2/10.
 */

public class MvChildPresenter implements MvChildMvp.Presenter {
    private static final String TAG = "MvChildPresenter";

    private MvChildMvp.View view;

    public MvChildPresenter(MvChildMvp.View view) {
        this.view = view;
    }



    @Override
    public void loadData(String area, int offset, int size) {
        String url = URLProviderUtil.getMVListUrl(area,offset,size);//atl+回车，可以创建变量
        MyLog.d("MvChildPresenter.loadData,url="+url);
        HttpManager.getInstance().get(url, new BaseCallBack<MvListBean>() {
            @Override
            public void onFailure(int code, Exception e) {
                view.onError(code,e);
            }

            @Override
            public void onSuccess(MvListBean mvListBean) {
                view.setData(mvListBean.getVideos());
            }
        });
    }
}

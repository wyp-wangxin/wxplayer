package com.wyp.wxplayer.fragment.homepage;

import android.widget.TextView;

import com.wyp.wxplayer.R;
import com.wyp.wxplayer.bean.VideoBean;
import com.wyp.wxplayer.fragment.BaseFragment;
import com.wyp.wxplayer.utils.myLog;

import java.util.List;

/**
 * Created by wyp on 2021/1/31.
 */

public class HomeFragment extends BaseFragment implements HomeMvp.View {

    private static String TAG = "HomeFragment";
    private HomeMvp.Presenter mPresenter;

    public HomeFragment() {
        myLog.e(TAG,"HomeFragment");
    }

    @Override
    protected void initVeiw() {
        TextView textView = mRootview.findViewById(R.id.tv_test);

        textView.setText("这是home fragment");

        // 创建 presenter
        mPresenter = new HomePresenter(this);
        mPresenter.loadData(0, 10);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }


    @Override
    public void setData(List<VideoBean> videoBeen) {
        myLog.e(TAG,"videoBeen = "+videoBeen.size());
    }

    @Override
    public void onError(int code, Exception e) {

    }
}

package com.wyp.wxplayer.fragment.homepage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyp.wxplayer.R;
import com.wyp.wxplayer.bean.VideoBean;
import com.wyp.wxplayer.fragment.BaseFragment;
import com.wyp.wxplayer.utils.myLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wyp on 2021/1/31.
 */

public class HomeFragment extends BaseFragment implements HomeMvp.View {

    private static String TAG = "HomeFragment";
    @Bind(R.id.recylerview)
    RecyclerView mRecylerview;
    private HomeMvp.Presenter mPresenter;

    public HomeFragment() {
        myLog.e(TAG, "HomeFragment");
    }

    @Override
    protected void initVeiw() {
        // 创建 presenter
        mPresenter = new HomePresenter(this);
        mPresenter.loadData(0, 10);

        // 填充 RecylerView 列表。 RecylerView,的基础用法
        // 布局管理器
        LinearLayoutManager layout = new LinearLayoutManager(getContext());//RecylerView 列表
//        GridLayoutManager layout = new GridLayoutManager(getContext(),2);// RecylerView 九宫格
//        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL);// RecylerView 瀑布流
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecylerview.setLayoutManager(layout);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }


    @Override
    public void setData(List<VideoBean> videoBeen) {
        myLog.e(TAG, "videoBeen = " + videoBeen.size());
    }

    @Override
    public void onError(int code, Exception e) {

    }



}

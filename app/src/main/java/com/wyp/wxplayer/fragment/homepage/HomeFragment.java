package com.wyp.wxplayer.fragment.homepage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyp.wxplayer.R;
import com.wyp.wxplayer.adapter.HomeAdapter;
import com.wyp.wxplayer.bean.VideoBean;
import com.wyp.wxplayer.fragment.BaseFragment;
import com.wyp.wxplayer.utils.myLog;

import java.util.ArrayList;
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
    @Bind(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private HomeMvp.Presenter mPresenter;
    private List<VideoBean> mVideoBeans;
    private HomeAdapter mHomeAdapter;
    private boolean isRefresh;

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

        // 设置 Adapter
        mVideoBeans = new ArrayList<>();
        mHomeAdapter = new HomeAdapter(mVideoBeans);
        mRecylerview.setAdapter(mHomeAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new MyOnRefreshListener());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }


    @Override
    public void setData(List<VideoBean> videoBeen) {
        myLog.e(TAG, "videoBeen = " + videoBeen.size());

        if (isRefresh){// 下拉刷新需要清空原有数据
            this.mVideoBeans.clear();
            isRefresh = false;
            mSwipeRefreshLayout.setRefreshing(false);//设置false表示刷新结束
        }

        this.mVideoBeans.addAll(videoBeen);
        mHomeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(int code, Exception e) {

    }


    private class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            offset = 0;
            mPresenter.loadData(offset,SIZE);
            isRefresh = true;
        }
    }
}

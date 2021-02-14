package com.wyp.wxplayer.fragment.localpage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.android.wxvideoplayer.player.WxVideoPlayer;
import com.wyp.android.wxvideoplayer.player.WxVideoPlayerManager;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.adapter.HomeVideoAdapter;
import com.wyp.wxplayer.adapter.LocalVideoAdapter;
import com.wyp.wxplayer.bean.LocalVideoBean;
import com.wyp.wxplayer.bean.VideoBean;
import com.wyp.wxplayer.fragment.BaseFragment;
import com.wyp.wxplayer.fragment.homepage.HomeFragment;
import com.wyp.wxplayer.utils.DataUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2021/2/9.
 */

public class LocalFragment extends BaseFragment implements LocalMvp.View{


    @Bind(R.id.recylerview)
    RecyclerView mRecylerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<LocalVideoBean> mLocalVideoBeans;

    private LocalVideoAdapter mLocalVideoAdapter ;
    private LocalPresenter mPresenter;
    @Override
    protected void initVeiw() {
        MyLog.d(" 设置 Adapter initVeiwinitVeiwinitVeiwinitVeiw");
        // 创建 presenter
        mPresenter = new LocalPresenter(getContext(),this);


        // 布局管理器
        LinearLayoutManager layout = new LinearLayoutManager(getContext());//RecylerView 列表
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecylerview.setLayoutManager(layout);

        // 设置 Adapter

        mLocalVideoBeans = new ArrayList<>();
        mLocalVideoAdapter = new LocalVideoAdapter(getContext(),mLocalVideoBeans);
        mRecylerview.setHasFixedSize(true);
        mRecylerview.setAdapter(mLocalVideoAdapter);
        mPresenter.loadData();
        mRecylerview.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new MyOnRefreshListener());
    }

    @Override
    protected int getLayoutID() {
         return R.layout.fragment_local;
    }

    @Override
    public void setData(List<LocalVideoBean> localVideoBean) {
        MyLog.d("size :" +localVideoBean.size());
        this.mLocalVideoBeans.addAll(localVideoBean);
        mLocalVideoAdapter.notifyDataSetChanged();
    }


    private static class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {

        }
    }
}

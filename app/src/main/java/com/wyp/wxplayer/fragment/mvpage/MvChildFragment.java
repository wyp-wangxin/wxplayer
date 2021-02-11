package com.wyp.wxplayer.fragment.mvpage;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wyp.wxplayer.R;
import com.wyp.wxplayer.adapter.MvChildAdapter;
import com.wyp.wxplayer.bean.VideoBean;
import com.wyp.wxplayer.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wyp on 2021/2/10.
 */

public class MvChildFragment extends BaseFragment implements MvChildMvp.View {


    @Bind(R.id.recylerview)
    RecyclerView mRecylerview;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mRefresh;

    private String code;
    private MvChildMvp.Presenter presenter;
    private List<VideoBean> videoList;
    private MvChildAdapter mMvChildAdapter;
    private boolean isRefresh;
    private boolean hasMore = true;

    public static MvChildFragment newInstance(String code){
        Bundle args = new Bundle();
        args.putString("code",code);

        MvChildFragment fragment = new MvChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initVeiw() {
        // 获取要显示的地区类型
        Bundle arguments = getArguments();
        String code = arguments.getString("code");
//        LogUtils.e(TAG,"MvChildFragment.initView,code="+code);

        // 使用 presenter 加载数据
        presenter = new MvChildPresenter(this);
        presenter.loadData(code,offset,SIZE);


        // 初始化列表
        mRecylerview.setLayoutManager(new LinearLayoutManager(getContext()));
        videoList = new ArrayList<>();
        mMvChildAdapter = new MvChildAdapter(videoList);
        mRecylerview.setAdapter(mMvChildAdapter);

        // 下拉刷新监听
        mRefresh.setOnRefreshListener(new OnMvRefreshListener());

        mRecylerview.addOnScrollListener(new OnMvScrollListener());//上滑刷新
    }


    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    @Override
    public void setData(List<VideoBean> videos) {

        if (isRefresh){
            videoList.clear();
            isRefresh = false;
            mRefresh.setRefreshing(false);// 隐藏刷新提示
        }

        // 计算下一次加载数据的起始位置
        offset += videos.size();
        hasMore = videos.size() >= SIZE;

        videoList.addAll(videos);
        mMvChildAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(int code, Exception e) {
        Toast.makeText(getContext(), "MvChildFragment 错误码为："+code, Toast.LENGTH_SHORT).show();
    }

    private class OnMvRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            isRefresh = true;
            offset = 0;
            presenter.loadData(code,offset,SIZE);
        }
    }

    private class OnMvScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            // 获取当前可见的最后一个条目位置
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecylerview.getLayoutManager();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

            // 当用户松手时发现已经是在最后一项，则获取下一页数据
            if (newState== 0&& lastVisibleItemPosition == videoList.size()-1 && hasMore){
                presenter.loadData(code,offset,SIZE);
            }
        }
    }
}

package com.wyp.wxplayer.fragment.homepage;


import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.android.wxvideoplayer.player.WxVideoPlayer;
import com.wyp.android.wxvideoplayer.player.WxVideoPlayerManager;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.adapter.HomeVideoAdapter;
import com.wyp.wxplayer.bean.VideoBean;
import com.wyp.wxplayer.fragment.BaseFragment;
import com.wyp.wxplayer.utils.DataUtil;
import com.wyp.wxplayer.utils.myLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


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
    private HomeVideoAdapter mHomeVideoAdapter;
    private boolean isRefresh;

    public HomeFragment() {
        myLog.e(TAG, "HomeFragment");
    }

    @Override
    protected void initVeiw() {
        // 创建 presenter
        mPresenter = new HomePresenter(this);
        mPresenter.loadData(0, 10);
        MyLog.d("HomeFragmentHomeFragment ");
        // 填充 RecylerView 列表。 RecylerView,的基础用法
        // 布局管理器
        LinearLayoutManager layout = new LinearLayoutManager(getContext());//RecylerView 列表
//        GridLayoutManager layout = new GridLayoutManager(getContext(),2);// RecylerView 九宫格
//        StaggeredGridLayoutManager layout = new StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL);// RecylerView 瀑布流
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        mRecylerview.setLayoutManager(layout);

        // 设置 Adapter
        mVideoBeans = new ArrayList<>();
        mHomeVideoAdapter = new HomeVideoAdapter(getContext(),DataUtil.getVideoListData());
        mRecylerview.setHasFixedSize(true);
        mRecylerview.setAdapter(mHomeVideoAdapter);
        mRecylerview.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                WxVideoPlayer VideoPlayer = ((HomeVideoAdapter.MyViewHolder) holder).mVideoPlayer;
                if (VideoPlayer == WxVideoPlayerManager.instance().getCurrentWxVideoPlayer()) {
                    WxVideoPlayerManager.instance().releaseWxVideoPlayer();
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new MyOnRefreshListener());
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE){
            //bottomBar.hide();
          /*  MyLog.d("onConfigurationChanged ffffffffffffffffffff");
            ViewGroup viewGroup = (ViewGroup)  WxVideoPlayerManager.instance().getCurrentWxVideoPlayer().getParent().getParent().getParent();
            if (viewGroup == null)
                return;
           // hideActionBar();
            viewGroup.removeAllViews();
            ViewGroup viewGroup2 = (ViewGroup)  WxVideoPlayerManager.instance().getCurrentWxVideoPlayer().getParent().getParent();*/


        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            //bottomBar.show();
        }



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
        mHomeVideoAdapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {

        MyLog.d("hhhhhhh onResume ????");
        WxVideoPlayer VideoPlayer = WxVideoPlayerManager.instance().getCurrentWxVideoPlayer();
       if(VideoPlayer!=null)
           VideoPlayer.resumeGLSurfaceView();

        super.onResume();


    }

    @Override
    public void onPause() {
        MyLog.d("hhhhhhh onPause ????");
        WxVideoPlayer VideoPlayer = WxVideoPlayerManager.instance().getCurrentWxVideoPlayer();
        if(VideoPlayer!=null)
            VideoPlayer.pauseGLSurfaceView();
        super.onPause();
    }
}

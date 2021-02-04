package com.wyp.wxplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wyp.android.wxvideoplayer.player.WxVideoPlayer;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.bean.Video;
import com.wyp.android.wxvideoplayer.player.WxPlayerController;
import com.wyp.wxplayer.utils.myLog;

import java.util.List;

/**
 * Created by wyp on 2021/1/31.
 */

public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.MyViewHolder> {
    private static String TAG = "HomeVideoAdapter";
    private List<Video> mVideoList;
    private Context mContext;

    public HomeVideoAdapter( List<Video> videoList) {
        //mContext = context;
        mVideoList = videoList;
    }

    @Override
    /**
     * 创建新的 ViewHolder，只在没有 itemView 的时候被调用
     */
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myLog.e(TAG,"onCreateViewHolder");
        View itemView = View.inflate(parent.getContext(), R.layout.home_item_video, null);
        MyViewHolder holder = new MyViewHolder(itemView);
        WxPlayerController controller = new WxPlayerController(mContext);
        holder.setController(controller);
        return holder;
    }

    @Override
    /**
     * 为 ViewHolder 绑定数据
     */
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Video video = mVideoList.get(position);
        holder.bindData(video);
    }

    @Override
    public int getItemCount() {
       return mVideoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private WxPlayerController mController;
        public WxVideoPlayer mVideoPlayer;
        public MyViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mVideoPlayer = (WxVideoPlayer) itemView.findViewById(R.id.nice_video_player);
        }

        public void setController(WxPlayerController controller) {
            mController = controller;
            mVideoPlayer.setController(mController);
        }

        public void bindData(Video video) {
            mController.setTitle(video.getTitle());
            mController.setLenght(video.getLength());
            Glide.with(itemView.getContext())
                    .load(video.getImageUrl())
                    .placeholder(R.drawable.img_default)
                    .crossFade()
                    .into(mController.imageView());
            mVideoPlayer.setUp(video.getVideoUrl(), null);
        }
    }

}

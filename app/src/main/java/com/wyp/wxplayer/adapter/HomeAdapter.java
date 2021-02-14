package com.wyp.wxplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyp.wxplayer.R;
import com.wyp.wxplayer.Util;
import com.wyp.wxplayer.bean.VideoBean;
import com.wyp.android.wxvideoplayer.player.WxPlayerController;
import com.wyp.wxplayer.utils.myLog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wyp on 2021/1/31.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
    private static String TAG = "HomeAdapter";
    private List<VideoBean> mVideoBeans;
    private Context mContext;
    public HomeAdapter(List<VideoBean> videoBeen) {
        this.mVideoBeans = videoBeen;
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
        // 获取到当前条目的数据 我们没有真正的资源
        VideoBean videoBean = mVideoBeans.get(position);

        // 填充内容 我们没有真正的资源
        //holder.mTvTitle.setText(videoBean.getTitle());
        //holder.mTvDescription.setText(videoBean.getDescription());
        holder.mTvTitle.setText("《火影忍者》");
        holder.mTvDescription.setText("好看的日漫更新了！");
        // 加载图片 我们没有真正的资源
        //Glide.with(holder.itemView.getContext()).load(videoBean.getPosterPic()).into(holder.mIvContentimg);

        final int tag;
        String type = videoBean.getType();
        if ("ACTIVITY".equalsIgnoreCase(type)) {//打开页面
            tag = 0;
            holder.mIvType.setImageResource(R.drawable.home_page_activity);
        } else if ("VIDEO".equalsIgnoreCase(type)) {//首播，点击进去显示MV描述，相关MV
            tag = 1;
            holder.mIvType.setImageResource(R.drawable.home_page_video);
        } else if ("WEEK_MAIN_STAR".equalsIgnoreCase(type)) {//(悦单)点击进去跟显示悦单详情一样
            tag = 2;
            holder.mIvType.setImageResource(R.drawable.home_page_star);
        } else if ("PLAYLIST".equalsIgnoreCase(type)) {//(悦单)点击进去跟显示悦单详情一样
            tag = 3;
            holder.mIvType.setImageResource(R.drawable.home_page_playlist);
        } else if ("AD".equalsIgnoreCase(type)) {
            tag = 4;
            holder.mIvType.setImageResource(R.drawable.home_page_ad);
        } else if ("PROGRAM".equalsIgnoreCase(type)) {//跳到MV详情
            tag = 5;
            holder.mIvType.setImageResource(R.drawable.home_page_program);
        } else if ("bulletin".equalsIgnoreCase(type)) {
            tag = 6;
            holder.mIvType.setImageResource(R.drawable.home_page_bulletin);
        } else if ("fanart".equalsIgnoreCase(type)) {
            tag = 7;
            holder.mIvType.setImageResource(R.drawable.home_page_fanart);
        } else if ("live".equalsIgnoreCase(type)) {
            tag = 8;
            holder.mIvType.setImageResource(R.drawable.home_page_live);
        } else if ("LIVENEW".equalsIgnoreCase(type) || ("LIVENEWLIST".equals(type))) {
            tag = 9;
            holder.mIvType.setImageResource(R.drawable.home_page_live_new);
        } else if ("INVENTORY".equalsIgnoreCase(videoBean.getType())) {//打开页面
            tag = 10;
            holder.mIvType.setImageResource(R.drawable.home_page_project);
        } else {
            tag = -100;
            holder.mIvType.setImageResource(0);
        }

        // 更新当前条目的类型
        holder.tag = tag;
    }

    @Override
    public int getItemCount() {
       // return mVideoBeans.size();
        return 10; //我们没有真正的资源
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_item_logo)
        ImageView mIvItemLogo;
        @Bind(R.id.iv_contentimg)
        ImageView mIvContentimg;
        @Bind(R.id.iv_type)
        ImageView mIvType;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_description)
        TextView mTvDescription;
        @Bind(R.id.viewbg)
        View viewbg;
        int tag;// 当前条目的类型

        private WxPlayerController mController;
       // public WxVideoPlayer mVideoPlayer;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            // 初始化图片大小
            Point point = Util.computeImgSize(640,540,itemView.getContext());
            mIvContentimg.getLayoutParams().width = point.x;
            mIvContentimg.getLayoutParams().height = point.y;
            mIvContentimg.requestLayout();
            //给布局一个灰色，让文字看的清楚
            viewbg.getLayoutParams().width = point.x;
            viewbg.getLayoutParams().height = point.y;
            viewbg.requestLayout();
            // 注册点击监听
            itemView.setOnClickListener(new MyOnClickListener());
        }

        public void setController(WxPlayerController controller) {
            mController = controller;
            //mVideoPlayer.setController(mController);
        }

        private class MyOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext, "嘿嘿嘿 "+getAdapterPosition()+";tag="+tag, Toast.LENGTH_SHORT).show();

                switch (tag){
                    case 0:
                    case 4:
                    case 10:
                        break;
                    case 1:
                    case 5:
                    case 7:

                        break;

                    case 2:
                    case 3:
                        break;
            }
        }
    }

    }

}

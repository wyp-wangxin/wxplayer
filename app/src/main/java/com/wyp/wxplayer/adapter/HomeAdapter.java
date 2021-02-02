package com.wyp.wxplayer.adapter;

import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.Util;
import com.wyp.wxplayer.bean.VideoBean;
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

    public HomeAdapter(List<VideoBean> videoBeen) {
        this.mVideoBeans = videoBeen;
    }

    @Override
    /**
     * 创建新的 ViewHolder，只在没有 itemView 的时候被调用
     */
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        myLog.e(TAG,"onCreateViewHolder");
        View itemView = View.inflate(parent.getContext(), R.layout.homepage_item, null);
        return new MyViewHolder(itemView);
    }

    @Override
    /**
     * 为 ViewHolder 绑定数据
     */
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // 获取到当前条目的数据 我们没有真正的资源
        //VideoBean videoBean = mVideoBeans.get(position);

        // 填充内容 我们没有真正的资源
        //holder.mTvTitle.setText(videoBean.getTitle());
        //holder.mTvDescription.setText(videoBean.getDescription());
        holder.mTvTitle.setText("《火影忍者》");
        holder.mTvDescription.setText("好看的日漫更新了！");
        // 加载图片 我们没有真正的资源
        //Glide.with(holder.itemView.getContext()).load(videoBean.getPosterPic()).into(holder.mIvContentimg);
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

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // 初始化图片大小
            Point point = Util.computeImgSize(640,540,itemView.getContext());
            mIvContentimg.getLayoutParams().width = point.x;
            mIvContentimg.getLayoutParams().height = point.y;
            mIvContentimg.requestLayout();
            //给布局一个灰色，让文字看的清楚
            viewbg.getLayoutParams().width = point.x;
            viewbg.getLayoutParams().height = point.y;
            viewbg.requestLayout();
        }
    }

}

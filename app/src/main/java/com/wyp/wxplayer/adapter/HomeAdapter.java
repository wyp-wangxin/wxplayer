package com.wyp.wxplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyp.wxplayer.R;
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
        // 获取到当前条目的数据
        VideoBean videoBean = mVideoBeans.get(position);

        // 填充内容
        holder.mTvTitle.setText(videoBean.getTitle());
        holder.mTvDescription.setText(videoBean.getDescription());
    }

    @Override
    public int getItemCount() {
        return mVideoBeans.size();
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

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

package com.wyp.wxplayer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.bean.VideoBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wyp on 2021/2/11.
 */

public class MvChildAdapter extends RecyclerView.Adapter<MvChildAdapter.MyViewHolder> {

    private List<VideoBean> videoList;

    public MvChildAdapter(List<VideoBean> videoList) {
        this.videoList = videoList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.fragment_mvitem, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VideoBean videoBean = videoList.get(position);

        // 填充文字
        holder.mName.setText(videoBean.getTitle());
        holder.mAuthor.setText(videoBean.getArtistName());
        holder.mPlayCount.setText(videoBean.getDescription());//简介

        // 填充图片
        Glide.with(holder.itemView.getContext()).load(videoBean.getPosterPic()).into(holder.mIvPostimg);

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_postimg)
        ImageView mIvPostimg;
        @Bind(R.id.viewbgs)
        View mViewbgs;
        @Bind(R.id.name)
        TextView mName;
        @Bind(R.id.author)
        TextView mAuthor;
        @Bind(R.id.play_count)
        TextView mPlayCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}

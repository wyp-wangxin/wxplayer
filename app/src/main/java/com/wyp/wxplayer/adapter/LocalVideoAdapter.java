package com.wyp.wxplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyp.android.wxvideoplayer.player.WxPlayerController;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.bean.LocalVideoBean;

import java.util.List;

/**
 * Created by Administrator on 2021/2/9.
 */

public class LocalVideoAdapter extends RecyclerView.Adapter<LocalVideoAdapter.MyViewHolder> {

    private Context mContext;
    private List<LocalVideoBean> mLocalVideoBeans;
    public LocalVideoAdapter(Context context, List<LocalVideoBean> localVideoBeanList) {
        mContext=context;
        mLocalVideoBeans = localVideoBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_localvideo_layout,parent,false);
        LocalVideoAdapter.MyViewHolder holder = new LocalVideoAdapter.MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LocalVideoBean localVideoBean = mLocalVideoBeans.get(position);


    }


    @Override
    public int getItemCount() {
        return mLocalVideoBeans.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}

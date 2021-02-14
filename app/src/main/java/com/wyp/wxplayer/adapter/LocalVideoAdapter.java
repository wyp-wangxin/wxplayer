package com.wyp.wxplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.activity.LocalVideoActivity;
import com.wyp.wxplayer.bean.LocalVideoBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2021/2/9.
 */

public class LocalVideoAdapter extends RecyclerView.Adapter<LocalVideoAdapter.MyViewHolder> {

    private Context mContext;
    private List<LocalVideoBean> mLocalVideoBeans;

    public LocalVideoAdapter(Context context, List<LocalVideoBean> localVideoBeanList) {
        mContext = context;
        mLocalVideoBeans = localVideoBeanList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_localvideo_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LocalVideoBean localVideoBean = mLocalVideoBeans.get(position);
        holder.mTvName.setText(localVideoBean.getName());

    }


    @Override
    public int getItemCount() {
        return mLocalVideoBeans.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_name)
        TextView mTvName;

        public MyViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LocalVideoBean localVideoBean = mLocalVideoBeans.get(MyViewHolder.this.getAdapterPosition());
                    MyLog.d(" localVideoBean getPath :"+localVideoBean.getPath());
                    Intent intent = new Intent(itemView.getContext(), LocalVideoActivity.class);
                    intent.putExtra("url",localVideoBean.getPath());
                    itemView.getContext().startActivity(intent);
                }
            });
        }


    }


}

package com.wyp.wxplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by wyp on 2021/1/31.
 */

public abstract class BaseFragment extends Fragment {

    protected View mRootview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(mRootview==null){
            mRootview = inflater.inflate(getLayoutID(),null);
        }
        ButterKnife.bind(this, mRootview);
        initVeiw();
        return mRootview;
    }

    protected abstract void initVeiw();

    protected abstract int getLayoutID();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

package com.wyp.wxplayer.fragment;

import android.widget.TextView;

import com.wyp.wxplayer.R;
import com.wyp.wxplayer.utils.myLog;

/**
 * Created by wyp on 2021/1/31.
 */

public class HomeFragment extends BaseFragment {

    private static String TAG = "HomeFragment";

     public HomeFragment() {
        myLog.e(TAG,"HomeFragment");
    }

    @Override
    protected void initVeiw() {
        TextView textView = mRootview.findViewById(R.id.tv_test);

        textView.setText("这是home fragment");
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }
}

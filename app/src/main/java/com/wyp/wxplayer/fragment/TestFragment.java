package com.wyp.wxplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyp.wxplayer.R;

/**
 * Created by wyp on 2021/1/30.
 */

public class TestFragment extends Fragment {



    /**
     * 获取 Fragment 对象
     * @param content
     * @return
     */
    public static TestFragment newInstance(String content){
        // 填充初始化参数
        Bundle args = new Bundle();
        args.putString("content",content);

        TestFragment testFragment = new TestFragment();
        testFragment.setArguments(args);

        return testFragment;
    }

    @Nullable
    @Override
    /*
    * 要学习一下Fragment 的基础知识，不知道onCreateView
    * */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test,container,false);
        TextView tv_test = (TextView) view.findViewById(R.id.tv_test);

        // 获取初始化参数
        Bundle args = getArguments();
        String content = args.getString("content");
        tv_test.setText(content);
        return view;
    }
}

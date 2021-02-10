package com.wyp.wxplayer.fragment.mvpage;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyp.android.wxvideoplayer.log.MyLog;
import com.wyp.wxplayer.R;
import com.wyp.wxplayer.adapter.MvPageAdapter;
import com.wyp.wxplayer.bean.AreaBean;
import com.wyp.wxplayer.fragment.BaseFragment;
import com.wyp.wxplayer.fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wyp on 2021/2/10.
 */

public class MvFragment extends BaseFragment implements MvMvp.View {

    private static final String TAG = "MvFragment";
    @Bind(R.id.tablayout)
    TabLayout mTablayout;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;

    private MvMvp.Presener presener;

    @Override
    protected void initVeiw() {
        presener = new MvPresenter(this);
        presener.loadData();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mv;
    }


    @Override
    public void setData(List<AreaBean> areaBeen) {
        MyLog.d("MvFragment.setData,areaBeen="+areaBeen.size());
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        for (AreaBean areaBean : areaBeen) {
            fragmentList.add(TestFragment.newInstance(areaBean.getCode()));
            titleList.add(areaBean.getName());
        }

        MvPageAdapter mvPageAdapter = new MvPageAdapter(getFragmentManager(), fragmentList, titleList);
        mViewpager.setAdapter(mvPageAdapter);
        // 关联 ViewPager
        mTablayout.setupWithViewPager(mViewpager);
    }

    @Override
    public void onError(int code, Exception e) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

package com.wyp.wxplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by wyp on 2021/2/10.
 *
 *
 FragmentStatePagerAdapter  调用 notifyDataSetChanged 方法可以刷新 ViewPager 的所有子界面
 FragmentPagerAdapter  调用 notifyDataSetChanged 方法，只有当 Adapter使用的 Fragment 集合发生变化才会刷新界面
   自行百度了解 FragmentStatePagerAdapter FragmentPagerAdapter的区别
 */

public class MvPageAdapter extends FragmentStatePagerAdapter {


    private List<Fragment> fragmentList;
    private List<String> titleList;

    public MvPageAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }


    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}

package com.ccq.app.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccq.app.base.BaseFragment;

import java.util.List;

/****************************************
 * 功能说明: 主页面Fragment适配器
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class MainFragmentAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragments;

    public MainFragmentAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        this.mFragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getCount() {
        if (mFragments != null) return mFragments.size();
        return 0;
    }
}

package com.ccq.app.ui.user.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccq.app.base.BaseFragment;
import com.ccq.app.ui.user.TabHomeFragment;
import com.ccq.app.ui.user.TabIntroFragment;

import java.util.List;

/**
 * Created by littlemax on 2018/5/2.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<String> titleList;

    private List<BaseFragment> mFragments;

    public MyFragmentAdapter (FragmentManager fm, List<BaseFragment> fragments,List<String> titls){
        super(fm);
        this.titleList = titls;
        this.mFragments = fragments;
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}

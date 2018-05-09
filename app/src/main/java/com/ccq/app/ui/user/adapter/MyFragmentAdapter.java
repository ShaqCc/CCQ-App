package com.ccq.app.ui.user.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccq.app.ui.user.TabHomeFragment;
import com.ccq.app.ui.user.TabIntroFragment;

/**
 * Created by littlemax on 2018/5/2.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {


    public MyFragmentAdapter (FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            // 首页
            case 0:
                fragment = new TabHomeFragment();
                break;
            // 简介
            case 1:
                fragment = new TabIntroFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


}

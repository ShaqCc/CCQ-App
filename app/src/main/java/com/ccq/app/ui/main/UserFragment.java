package com.ccq.app.ui.main;

import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;

/****************************************
 * 功能说明:  我的
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class UserFragment extends BaseFragment {
    @Override
    protected int inflateContentView() {
        return R.layout.fragment_user;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    public void initData() {

    }
}

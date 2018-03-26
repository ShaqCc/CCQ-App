package com.ccq.app.ui.message;

import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;

/****************************************
 * 功能说明:  消息
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class MessageFragment extends BaseFragment {
    @Override
    protected int inflateContentView() {
        return R.layout.fragment_message;
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

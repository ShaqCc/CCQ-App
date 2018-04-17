package com.ccq.app.ui.message;

import android.content.Intent;
import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.ui.TestHeader;

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
        rootView.findViewById(R.id.tstHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(get(), TestHeader.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

    }



}

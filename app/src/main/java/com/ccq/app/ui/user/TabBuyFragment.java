package com.ccq.app.ui.user;

import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/****************************************
 * 功能说明:  我的--求购 页签
 *****************************************/

public class TabBuyFragment extends BaseFragment   {



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
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

    }

    @Subscribe
    public void onReceiveLoginSuccess(Integer eventId) {

    }


}

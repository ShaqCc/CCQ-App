package com.ccq.app.ui.home;

import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.Banner;

import java.util.List;

/****************************************
 * 功能说明:  首页
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView{

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    public void initData() {
        mPresenter.loadData();
    }

    @Override
    public void showBanner(List<Banner> banners) {

    }
}

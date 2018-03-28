package com.ccq.app.ui.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.Car;
import com.ccq.app.utils.ViewState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/****************************************
 * 功能说明:  首页
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView {

    @BindView(R.id.home_recyclerview)
    RecyclerView homeRecyclerview;
    @BindView(R.id.home_srl)
    SwipeRefreshLayout homeSrl;
    private HomeAdapter homeAdapter;
    private List<Car> dataList = new ArrayList<>();
    private final int ACTION_REFRESH = 0;
    private final int ACTION_LOADMORE = 1;
    private int ACTION_TYPE = ACTION_REFRESH;

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
        homeRecyclerview.setLayoutManager(new LinearLayoutManager(getHostActivity()));
        homeAdapter = new HomeAdapter(dataList);
        homeRecyclerview.setAdapter(homeAdapter);
    }

    @Override
    public void initData() {
        mPresenter.loadData();
    }

    @Override
    public void showBanner(List<BannerBean> banners) {
        homeAdapter.setBanner(banners);
    }

    @Override
    public void showCarList(List<Car> cars) {
        if (ACTION_TYPE == ACTION_REFRESH) {
            dataList.clear();
        }
        dataList.addAll(cars);
        if (dataList.size() > 0)
        {
            setViewState(ViewState.STATE_CONTENT);
            homeAdapter.refresh(cars);
        }
        else {
            setViewState(ViewState.STATE_EMPTY);
        }
    }


}

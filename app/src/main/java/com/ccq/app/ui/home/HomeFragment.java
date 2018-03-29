package com.ccq.app.ui.home;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.Car;
import com.ccq.app.http.ApiParams;
import com.ccq.app.utils.ViewState;
import com.youth.banner.WeakHandler;

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
    private int pageIndex = 1;
    private boolean isLoading;
    private WeakHandler mHandler = new WeakHandler();
    private boolean isInit = false;

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
        final LinearLayoutManager manager = new LinearLayoutManager(getHostActivity());
        homeRecyclerview.setLayoutManager(manager);
        homeAdapter = new HomeAdapter(dataList);
        homeRecyclerview.setAdapter(homeAdapter);

        homeSrl.setRefreshing(false);
        //设置下拉刷新
        homeSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ACTION_TYPE = ACTION_REFRESH;
                pageIndex = 1;
                ApiParams.setPage(pageIndex);
                initData();
            }
        });
        //设置加载更多
        homeRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == homeAdapter.getItemCount()) {
                    boolean isRefreshing = homeSrl.isRefreshing();
                    if (isRefreshing) {
                        homeAdapter.notifyItemRemoved(homeAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadMore();
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });
    }

    private void loadMore() {
        pageIndex++;
        ApiParams.setPage(pageIndex);
        ACTION_TYPE = ACTION_LOADMORE;
        mPresenter.filterCar(ApiParams.getCarMap());
    }

    @Override
    public void initData() {
        mPresenter.loadData(isInit);
    }

    @Override
    public void showBanner(List<BannerBean> banners) {
        homeAdapter.setBanner(banners);
    }

    @Override
    public void showCarList(List<Car> cars) {
        isInit = true;
        homeSrl.setRefreshing(false);
        if (ACTION_TYPE == ACTION_REFRESH) {
            dataList.clear();
        }
        dataList.addAll(cars);
        if (dataList.size() > 0) {
            setViewState(ViewState.STATE_CONTENT);
            homeAdapter.refresh(dataList);
        } else {
            setViewState(ViewState.STATE_EMPTY);
        }
    }


}

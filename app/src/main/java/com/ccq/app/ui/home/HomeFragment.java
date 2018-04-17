package com.ccq.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.Car;
import com.ccq.app.http.ApiParams;
import com.ccq.app.ui.city.ProvinceActivity;
import com.ccq.app.utils.DialogUtils;
import com.ccq.app.utils.GlideImageLoader;
import com.ccq.app.utils.RequestCode;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.utils.ViewState;
import com.ccq.app.weidget.RecyclerViewHeader;
import com.youth.banner.Banner;
import com.youth.banner.WeakHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

import static android.app.Activity.RESULT_OK;

/****************************************
 * 功能说明:  首页
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView {

    @BindView(R.id.home_recyclerview)
    RecyclerView homeRecyclerView;
    @BindView(R.id.home_srl)
    SwipeRefreshLayout homeSrl;
    @BindView(R.id.home_head)
    RecyclerViewHeader homeHeader;
    @BindView(R.id.home_banner)
    Banner homeBanner;
    @BindView(R.id.banner_rb_city)
    CheckBox bannerRbCity;
    @BindView(R.id.banner_rb_brand)
    CheckBox bannerRbBrand;
    @BindView(R.id.banner_rb_size)
    CheckBox bannerRbSize;
    @BindView(R.id.banner_rb_age)
    CheckBox bannerRbAge;
    @BindView(R.id.banner_rb_order)
    CheckBox bannerRbOrder;
    @BindView(R.id.tag_group)
    TagGroup tagGroup;

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
        homeRecyclerView.setLayoutManager(manager);
        homeAdapter = new HomeAdapter(dataList);
        homeRecyclerView.setAdapter(homeAdapter);
        homeHeader.attachTo(homeRecyclerView);


        homeSrl.setRefreshing(false);
        //设置下拉刷新
        homeSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        //设置加载更多
        homeRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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

        EventBus.getDefault().register(this);
    }

    private void refresh() {
        pageIndex = 1;
        ApiParams.setPage(pageIndex);
        ACTION_TYPE = ACTION_REFRESH;
        initData();
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
        if (banners != null && banners.size() > 0) {
            homeBanner.setImageLoader(new GlideImageLoader());
            homeBanner.setImages(getBannerImages(banners));
            homeBanner.start();
        }
    }

    private List<String> getBannerImages(List<BannerBean> bannerList) {
        ArrayList<String> strings = new ArrayList<>();
        if (bannerList != null && bannerList.size() > 0) {
            for (BannerBean bean : bannerList) {
                strings.add(bean.getImage());
            }
        }
        return strings;
    }

    @Override
    public void showCarList(List<Car> cars) {
        isInit = true;
        homeSrl.setRefreshing(false);

        if (cars != null && cars.size() > 0) {
            if (ACTION_TYPE == ACTION_REFRESH) {
                dataList.clear();
            }
            dataList.addAll(cars);
        }
        if (dataList.size() > 0) {
            setViewState(ViewState.STATE_CONTENT);
            homeAdapter.refresh(dataList);
        }
    }

    @Override
    public void showBrandList(List<BrandBean> list) {
        if (list!=null && list.size()>0){
            DialogUtils.showListPopWindow(get(), homeHeader, new BrandAdapter(list),
                    new DialogUtils.OnPopItemClickListener<BrandBean>() {
                        @Override
                        public void OnPopItemClick(BrandBean bean) {
                            ToastUtils.show(get(),bean.getName());
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCitySelected(Integer code){
        if (code == RequestCode.SET_CITY){
            pageIndex = 1;
            ApiParams.setPage(pageIndex);
            ACTION_TYPE = ACTION_REFRESH;
            mPresenter.filterCar(ApiParams.getCarMap());
        }
    }


    private void resetCheckBoxState(){
        bannerRbCity.setChecked(false);
        bannerRbAge.setChecked(false);
        bannerRbBrand.setChecked(false);
        bannerRbOrder.setChecked(false);
        bannerRbSize.setChecked(false);
    }

    @OnClick({R.id.home_action_tv_sort, R.id.home_action_tv_buy, R.id.home_action_tv_vip, R.id.banner_rb_city, R.id.banner_rb_brand, R.id.banner_rb_size, R.id.banner_rb_age, R.id.banner_rb_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_action_tv_sort:
                break;
            case R.id.home_action_tv_buy:
                break;
            case R.id.home_action_tv_vip:
                break;
            case R.id.banner_rb_city:
                resetCheckBoxState();
                ProvinceActivity.launch(get(),new Bundle());
                break;
            case R.id.banner_rb_brand:
                mPresenter.chooseBrand();
                break;
            case R.id.banner_rb_size:
                break;
            case R.id.banner_rb_age:
                break;
            case R.id.banner_rb_order:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

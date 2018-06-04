package com.ccq.app.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.Car;
import com.ccq.app.entity.TypeBean;
import com.ccq.app.entity.YearLimitBean;
import com.ccq.app.http.ApiParams;
import com.ccq.app.ui.city.ProvinceActivity;
import com.ccq.app.ui.home.adapter.BrandAdapter;
import com.ccq.app.ui.home.adapter.OrderAdapter;
import com.ccq.app.ui.home.adapter.TypeAdapter;
import com.ccq.app.ui.home.adapter.YearAdapter;
import com.ccq.app.ui.user.OpenVipActivity;
import com.ccq.app.utils.DialogUtils;
import com.ccq.app.utils.GlideImageLoader;
import com.ccq.app.utils.RequestCode;
import com.ccq.app.utils.ViewState;
import com.ccq.app.weidget.RecyclerViewHeader;
import com.ccq.app.weidget.Toasty;
import com.youth.banner.Banner;
import com.youth.banner.WeakHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/****************************************
 * 功能说明:  首页
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeView, View.OnClickListener {

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
    @BindView(R.id.tag_province)
    View tagProvince;
    @BindView(R.id.tag_brand)
    View tagBrand;
    @BindView(R.id.tag_type)
    View tagType;
    @BindView(R.id.tag_age)
    View tagAge;
    @BindView(R.id.tag_order)
    View tagOrder;


    private HomeAdapter homeAdapter;
    private List<Car> dataList = new ArrayList<>();
    private final int ACTION_REFRESH = 0;
    private final int ACTION_LOADMORE = 1;
    private int ACTION_TYPE = ACTION_REFRESH;
    private int pageIndex = 1;
    private boolean isLoading;
    private WeakHandler mHandler = new WeakHandler();
    private boolean isInit = false;
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            resetCheckBoxState();
            updateTags();
        }
    };
    private String[] orderStringArray;

    /**
     * 设置tag
     */
    private void updateTags() {
        //城市
        String cityName = ApiParams.getCityName();
        if (!TextUtils.isEmpty(cityName)) {
            tagProvince.setVisibility(View.VISIBLE);
            tvCity.setText(cityName);
        } else {
            tagProvince.setVisibility(View.GONE);
        }
        //品牌
        String brandName = ApiParams.getBrandName();
        if (!TextUtils.isEmpty(brandName)) {
            tagBrand.setVisibility(View.VISIBLE);
            tvBrand.setText(brandName);
        } else {
            tagBrand.setVisibility(View.GONE);
        }
        //型号
        String typeName = ApiParams.getTypeName();
        if (!TextUtils.isEmpty(typeName)) {
            tagType.setVisibility(View.VISIBLE);
            tvType.setText(typeName);
        } else {
            tagType.setVisibility(View.GONE);
        }
        //车龄
        String age = ApiParams.getAge();
        if (!TextUtils.isEmpty(age)) {
            tagAge.setVisibility(View.VISIBLE);
            tvAge.setText(age);
        } else {
            tagAge.setVisibility(View.GONE);
        }
        //排序方式
        String orderFunc = ApiParams.getOrderFunc();
        if (!TextUtils.isEmpty(orderFunc)) {
            tagOrder.setVisibility(View.VISIBLE);
            tvOrder.setText(orderFunc);
        } else {
            tagOrder.setVisibility(View.GONE);
        }
    }

    private OrderAdapter orderAdapter;
    private TextView tvCity;
    private TextView tvBrand;
    private TextView tvType;
    private TextView tvAge;
    private TextView tvOrder;

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
        //init tags
        tvCity = tagProvince.findViewById(R.id.tag_tv_content);
//        View ivDeleteCity = tagProvince.findViewById(R.id.tag_iv_delete);
        tagProvince.setOnClickListener(this);
        tvBrand = tagBrand.findViewById(R.id.tag_tv_content);
//        View ivDeleteBrand = tagBrand.findViewById(R.id.tag_iv_delete);
        tagBrand.setOnClickListener(this);
        tvType = tagType.findViewById(R.id.tag_tv_content);
//        View ivDeleteType = tagType.findViewById(R.id.tag_iv_delete);
        tagType.setOnClickListener(this);
        tvAge = tagAge.findViewById(R.id.tag_tv_content);
//        View ivDeleteAge = tagAge.findViewById(R.id.tag_iv_delete);
        tagAge.setOnClickListener(this);
        tvOrder = tagOrder.findViewById(R.id.tag_tv_content);
//        View ivDeleteOrder = tagOrder.findViewById(R.id.tag_iv_delete);
        tagOrder.setOnClickListener(this);
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
        orderStringArray = getResources().getStringArray(R.array.order_array);
        List<String> list = Arrays.asList(orderStringArray);
        orderAdapter = new OrderAdapter(list);
    }

    /**
     * 设置banner数据
     *
     * @param banners
     */
    @Override
    public void showBanner(List<BannerBean> banners) {
        if (banners != null && banners.size() > 0) {
            homeBanner.setImageLoader(new GlideImageLoader());
            homeBanner.setImages(getBannerImages(banners));
            homeBanner.start();
        }
    }

    /**
     * 获取banner的url
     *
     * @param bannerList
     * @return
     */
    private List<String> getBannerImages(List<BannerBean> bannerList) {
        ArrayList<String> strings = new ArrayList<>();
        if (bannerList != null && bannerList.size() > 0) {
            for (BannerBean bean : bannerList) {
                strings.add(bean.getImage());
            }
        }
        return strings;
    }

    /**
     * 设置首页列表数据
     *
     * @param cars
     */
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

    /**
     * 设置弹窗品牌列表
     *
     * @param list
     */
    @Override
    public void showBrandList(List<BrandBean> list) {
        if (list != null && list.size() > 0) {
            DialogUtils.showListPopWindow(get(), homeHeader, new BrandAdapter(list),
                    new DialogUtils.OnPopItemClickListener<BrandBean>() {
                        @Override
                        public void OnPopItemClick(BrandBean bean, int position) {
                            ApiParams.setBrandid(bean.getId());
                            ApiParams.setBrandName(bean.getName());
                            mPresenter.filterCar(ApiParams.getCarMap());
                        }
                    }, dismissListener);
        }
    }

    /**
     * 设置型号列表数据
     *
     * @param list
     */
    @Override
    public void showTypeList(List<TypeBean.NumberListBean> list) {
        DialogUtils.showListPopWindow(get(), homeHeader, new TypeAdapter(list), new DialogUtils.OnPopItemClickListener() {
            @Override
            public void OnPopItemClick(Object o, int position) {
                TypeBean.NumberListBean type = (TypeBean.NumberListBean) o;
                if (type.isSub()) {
                    //子列表
                    ApiParams.setTon(type.getTid());
                    ApiParams.setNumberid(type.getId());
                } else {
                    //一级列表
                    ApiParams.setTon(type.getId());
                }
                mPresenter.filterCar(ApiParams.getCarMap());
            }
        }, dismissListener);
    }

    /**
     * 设置年份列表数据
     *
     * @param list
     */
    @Override
    public void showYearList(List<YearLimitBean> list) {
        DialogUtils.showListPopWindow(get(), homeHeader, new YearAdapter(list), new DialogUtils.OnPopItemClickListener() {
            @Override
            public void OnPopItemClick(Object o, int position) {
                YearLimitBean year = (YearLimitBean) o;
                ApiParams.setYearid(year.getId());
                ApiParams.setAge(year.getName());
                mPresenter.filterCar(ApiParams.getCarMap());
            }
        }, dismissListener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCitySelected(Integer code) {
        if (code == RequestCode.SET_CITY) {
            pageIndex = 1;
            ApiParams.setPage(pageIndex);
            ACTION_TYPE = ACTION_REFRESH;
            updateTags();
            mPresenter.filterCar(ApiParams.getCarMap());
        }
    }


    /**
     * 重置checkbox状态
     */
    private void resetCheckBoxState() {
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
                showSortDialog();
                break;
            case R.id.home_action_tv_buy:
                Toasty.info(get(), "求购开发中...").show();
                break;
            case R.id.home_action_tv_vip:
                startActivity(new Intent(get(), OpenVipActivity.class));
                break;
            case R.id.banner_rb_city:
                resetCheckBoxState();
                ProvinceActivity.launch(get(), new Bundle());
                break;
            case R.id.banner_rb_brand:
                resetCheckBoxState();
                bannerRbBrand.setChecked(true);
                mPresenter.chooseBrand();
                break;
            case R.id.banner_rb_size:
                resetCheckBoxState();
                bannerRbSize.setChecked(true);
                mPresenter.chooseType();
                break;
            case R.id.banner_rb_age:
                resetCheckBoxState();
                bannerRbAge.setChecked(true);
                mPresenter.chooseYear();
                break;
            case R.id.banner_rb_order:
                resetCheckBoxState();
                bannerRbOrder.setChecked(true);
                DialogUtils.showListPopWindow(get(), homeHeader, orderAdapter, new DialogUtils.OnPopItemClickListener() {
                    @Override
                    public void OnPopItemClick(Object o, int position) {
                        ApiParams.setOrder(String.valueOf(position));
                        ApiParams.setOrderFunc(orderStringArray[position]);
                        mPresenter.filterCar(ApiParams.getCarMap());
                    }
                }, dismissListener);
                break;
        }
    }

    /**
     * 切换分类对话框
     */
    private void showSortDialog() {
        final Dialog sortDialog = new Dialog(get(), R.style.no_bg_dialog_theme);
        View rootView = LayoutInflater.from(get()).inflate(R.layout.dialog_change_sort_layout, null, false);
        sortDialog.setContentView(rootView);
        rootView.findViewById(R.id.sort_tv_ccq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDialog.dismiss();
            }
        });

        rootView.findViewById(R.id.sort_tv_wjq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(get(), "挖机圈APP开发中..").show();
                sortDialog.dismiss();
            }
        });

        sortDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * tags的点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tag_province:
                ApiParams.removeCity();
                break;
            case R.id.tag_brand:
                ApiParams.removeBrand();
                break;
            case R.id.tag_type:
                ApiParams.removeType();
                break;
            case R.id.tag_age:
                ApiParams.removeAge();
                break;
            case R.id.tag_order:
                ApiParams.removeOrderFunc();
                break;
        }
        updateTags();
        mPresenter.filterCar(ApiParams.getCarMap());
    }
}

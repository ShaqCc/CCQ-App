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
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.Car;
import com.ccq.app.entity.TypeBean;
import com.ccq.app.entity.UserBean;
import com.ccq.app.entity.YearLimitBean;
import com.ccq.app.http.HomeCarParams;
import com.ccq.app.ui.MainActivity;
import com.ccq.app.ui.city.ProvinceActivity;
import com.ccq.app.ui.home.adapter.BrandAdapter;
import com.ccq.app.ui.home.adapter.OrderAdapter;
import com.ccq.app.ui.home.adapter.TypeAdapter;
import com.ccq.app.ui.home.adapter.YearAdapter;
import com.ccq.app.ui.user.OpenVipActivity;
import com.ccq.app.utils.DensityUtils;
import com.ccq.app.utils.DialogUtils;
import com.ccq.app.utils.GlideImageLoader;
import com.ccq.app.utils.RequestCode;
import com.ccq.app.weidget.RecycleViewDivider;
import com.ccq.app.weidget.RecyclerViewHeader;
import com.ccq.app.weidget.Toasty;
import com.youth.banner.Banner;
import com.youth.banner.WeakHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import jiguang.chat.pickerimage.utils.ScreenUtil;

/****************************************
 * 功能说明:  首页

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
    View tagCity;
    @BindView(R.id.tag_brand)
    View tagBrand;
    @BindView(R.id.tag_type)
    View tagType;
    @BindView(R.id.tag_age)
    View tagAge;
    @BindView(R.id.user_layout)
    View userLayout;
    @BindView(R.id.user_iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.icon_vip_logo)
    ImageView iconVipLogo;
    @BindView(R.id.home_head_ll_tag_parent)
    View llTagParent;
    private TextView tvCity;
    @BindView(R.id.home_checkbox_parent)
    View homeCheckBoxParent;
    @BindView(R.id.bt_home_top)
    View btBackTop;
    private int statusBarHeight;
    private int chockBoxHeight;


    @OnClick(R.id.user_iv_header)
    public void gotoUserPage() {
        ((MainActivity) getHostActivity()).setCurrentTab(3);
    }

    @OnClick(R.id.home_head_tv_reset_tags)
    public void resetTags() {
        bannerRbCity.setText("全国");
        HomeCarParams.getInstance().resetTags();
        HomeCarParams.getInstance().resetParams();
        refreshHomeData();
    }

    @OnClick(R.id.bt_home_top)
    public void backToTop(){
        homeRecyclerView.scrollToPosition(0);
    }

    /**
     * 刷新首页数据
     */
    private void refreshHomeData() {
        homeSrl.setRefreshing(true);
        updateTags();
        ACTION_TYPE = ACTION_REFRESH;
        mPresenter.filterCar(HomeCarParams.getInstance().getParams());
    }


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
//            WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
//            attributes.alpha = 1f;
//            getActivity().getWindow().setAttributes(attributes);
        }
    };
    private String[] orderStringArray;

    /**
     * 设置tag
     */
    private void updateTags() {
        if (TextUtils.isEmpty(HomeCarParams.getInstance().getCarTypeName()) &&
                TextUtils.isEmpty(HomeCarParams.getInstance().getCarBrandName()) &&
                TextUtils.isEmpty(HomeCarParams.getInstance().getCarUseTime()) &&
                TextUtils.isEmpty(HomeCarParams.getInstance().getCityName())) {
            //为空，隐藏tag
            llTagParent.setVisibility(View.GONE);
        } else {
            llTagParent.setVisibility(View.VISIBLE);
            //城市
            String cityName = HomeCarParams.getInstance().getCityName();
            if (!TextUtils.isEmpty(cityName)) {
                tagCity.setVisibility(View.VISIBLE);
                tvCity.setText(cityName);
            } else {
                tagCity.setVisibility(View.GONE);
            }
            //品牌
            String brandName = HomeCarParams.getInstance().getCarBrandName();
            if (!TextUtils.isEmpty(brandName)) {
                tagBrand.setVisibility(View.VISIBLE);
                tvBrand.setText(brandName);
            } else {
                tagBrand.setVisibility(View.GONE);
            }
            //型号
            String typeName = HomeCarParams.getInstance().getCarTypeName();
            if (!TextUtils.isEmpty(typeName)) {
                tagType.setVisibility(View.VISIBLE);
                tvType.setText(typeName);
            } else {
                tagType.setVisibility(View.GONE);
            }
            //车龄
            String age = HomeCarParams.getInstance().getCarUseTime();
            if (!TextUtils.isEmpty(age)) {
                tagAge.setVisibility(View.VISIBLE);
                tvAge.setText(age);
            } else {
                tagAge.setVisibility(View.GONE);
            }
        }
    }

    private OrderAdapter orderAdapter;
    private TextView tvBrand;
    private TextView tvType;
    private TextView tvAge;

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
        homeRecyclerView.addItemDecoration(new RecycleViewDivider(get(), LinearLayoutManager.HORIZONTAL, R.drawable.shape_divide_line_1px));
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
        tvCity = tagCity.findViewById(R.id.tag_tv_content);
        tagCity.setOnClickListener(this);
        tvBrand = tagBrand.findViewById(R.id.tag_tv_content);
        tagBrand.setOnClickListener(this);
        tvType = tagType.findViewById(R.id.tag_tv_content);
        tagType.setOnClickListener(this);
        tvAge = tagAge.findViewById(R.id.tag_tv_content);
        tagAge.setOnClickListener(this);
        EventBus.getDefault().register(this);

        statusBarHeight = ScreenUtil.getStatusBarHeight(get());
        chockBoxHeight = DensityUtils.dp2px(get(),42);
        DialogUtils.setPopWindowOffset(chockBoxHeight + statusBarHeight);

        btBackTop.setVisibility(View.GONE);
    }

    private void refresh() {
        pageIndex = 1;
        HomeCarParams.getInstance().put("page", "1");
        ACTION_TYPE = ACTION_REFRESH;
        initData();
    }

    private void loadMore() {
        pageIndex++;
        HomeCarParams.getInstance().put("page", String.valueOf(pageIndex));
        ACTION_TYPE = ACTION_LOADMORE;
        mPresenter.filterCar(HomeCarParams.getInstance().getParams());
    }

    @Override
    public void initData() {
        mPresenter.loadData(isInit);
        orderStringArray = getResources().getStringArray(R.array.order_array);
        List<String> list = Arrays.asList(orderStringArray);
        orderAdapter = new OrderAdapter(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        //更新会员信息
        mPresenter.updateUser();
    }

    @Override
    public void updateUser(UserBean bean) {
        if (bean != null) {
            userLayout.setVisibility(View.VISIBLE);
            Glide.with(get()).load(bean.getHeadimgurl()).into(ivHeader);
            tvUserName.setText(bean.getNickname());
            if (bean.getVip() == 1) {
                iconVipLogo.setImageResource(R.drawable.icon_vip_enable);
            } else {
                iconVipLogo.setImageResource(R.drawable.icon_no_vip);
            }
        } else {
            userLayout.setVisibility(View.GONE);
            iconVipLogo.setImageResource(R.drawable.icon_no_vip);
        }
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
        btBackTop.setVisibility(View.VISIBLE);
        homeSrl.setRefreshing(false);
        if (ACTION_TYPE == ACTION_REFRESH) {
            //刷新
            dataList.clear();
            if (cars == null || cars.isEmpty()) {
                //do nothing
            } else {
                dataList.addAll(cars);
            }
            homeAdapter.refresh(dataList);
        } else {
            //加载更多
            if (cars == null || cars.isEmpty()) {
                Toasty.info(get(), "没有更多数据啦！").show();
            } else {
                dataList.addAll(cars);
                homeAdapter.refresh(dataList);
            }
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
                            HomeCarParams.getInstance().put("brandid", bean.getId());
                            HomeCarParams.getInstance().setCarBrandName(bean.getName());
                            //刷新
                            refreshHomeData();
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
        DialogUtils.showListPopWindow(get(), homeCheckBoxParent, new TypeAdapter(list), new DialogUtils.OnPopItemClickListener() {
            @Override
            public void OnPopItemClick(Object o, int position) {
                TypeBean.NumberListBean type = (TypeBean.NumberListBean) o;
                if (type.isSub()) {
                    //子列表
                    HomeCarParams.getInstance().put("numberid", type.getId());
                }
                HomeCarParams.getInstance().put("tonnageid", type.getTid());
                HomeCarParams.getInstance().setCarTypeName(type.getName());
                //刷新
                refreshHomeData();
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
        DialogUtils.showListPopWindow(get(), homeCheckBoxParent, new YearAdapter(list), new DialogUtils.OnPopItemClickListener() {
            @Override
            public void OnPopItemClick(Object o, int position) {
                YearLimitBean year = (YearLimitBean) o;
                HomeCarParams.getInstance().put("yearid", year.getId());
                HomeCarParams.getInstance().setCarUseTime(year.getName());
                //刷新
                refreshHomeData();
            }
        }, dismissListener);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCitySelected(Integer code) {
        if (code == RequestCode.SET_CITY) {
            pageIndex = 1;
            HomeCarParams.getInstance().put("page", "1");
            bannerRbCity.setText(HomeCarParams.getInstance().getCityName());
            refreshHomeData();
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
                //品牌
                resetCheckBoxState();
                bannerRbBrand.setChecked(true);
                scrollToTop();
                mPresenter.chooseBrand();
                break;
            case R.id.banner_rb_size:
                //型号
                resetCheckBoxState();
                bannerRbSize.setChecked(true);
                scrollToTop();
                mPresenter.chooseType();
                break;
            case R.id.banner_rb_age:
                //年限
                resetCheckBoxState();
                bannerRbAge.setChecked(true);
                scrollToTop();
                mPresenter.chooseYear();
                break;
            case R.id.banner_rb_order:
                //排序
                resetCheckBoxState();
                bannerRbOrder.setChecked(true);
                scrollToTop();
                DialogUtils.showListPopWindow(get(), homeCheckBoxParent, orderAdapter, new DialogUtils.OnPopItemClickListener() {
                    @Override
                    public void OnPopItemClick(Object o, int position) {
                        HomeCarParams.getInstance().put("order", String.valueOf(position));
                        HomeCarParams.getInstance().setOrderName(orderStringArray[position]);
                        homeSrl.setRefreshing(true);
                        mPresenter.filterCar(HomeCarParams.getInstance().getParams());
                    }
                }, dismissListener);
                break;
        }
    }

    private void scrollToTop() {
        int statusBarHeight = ScreenUtil.getStatusBarHeight(get());
        int[] loc = new int[2];
        homeCheckBoxParent.getLocationOnScreen(loc);
        homeRecyclerView.scrollBy(0, loc[1] - statusBarHeight);
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
                HomeCarParams.getInstance().delete("provinceid");
                HomeCarParams.getInstance().delete("cityid");
                HomeCarParams.getInstance().setCityName("");
                bannerRbCity.setText("全国");
                break;
            case R.id.tag_brand:
                HomeCarParams.getInstance().delete("brandid");
                HomeCarParams.getInstance().setCarBrandName("");
                break;
            case R.id.tag_type:
                HomeCarParams.getInstance().delete("numberid");
                HomeCarParams.getInstance().delete("tonnageid");
                HomeCarParams.getInstance().setCarTypeName("");
                break;
            case R.id.tag_age:
                HomeCarParams.getInstance().delete("yearid");
                HomeCarParams.getInstance().setCarUseTime("");
                break;
        }
        updateTags();
        ACTION_TYPE = ACTION_REFRESH;
        pageIndex = 1;
        HomeCarParams.getInstance().put("page", "1");
        homeSrl.setRefreshing(true);
        mPresenter.filterCar(HomeCarParams.getInstance().getParams());
    }
}

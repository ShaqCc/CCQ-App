package com.ccq.app.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.UserBean;
import com.ccq.app.entity.UserLocationBean;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.weidget.Toasty;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/****************************************
 * 功能说明:  我的--简介 页签
 *****************************************/

public class TabIntroFragment extends BaseFragment {


    @BindView(R.id.tv_on_sale_count)
    TextView tvOnSaleCount;
    @BindView(R.id.tv_sale_out_count)
    TextView tvSaleOutCount;
    @BindView(R.id.tv_myinfo_edit)
    TextView tvMyinfoEdit;
    TextView tvMyinfoContent;
    @BindView(R.id.tv_myimg_edit)
    TextView tvMyimgEdit;
    @BindView(R.id.myinfo_banner)
    Banner myinfoBanner;
    @BindView(R.id.tv_mylocation_edit)
    TextView tvMylocationEdit;
    @BindView(R.id.tencent_map)
    com.tencent.tencentmap.mapsdk.map.MapView mapView;
    @BindView(R.id.tv_my_location)
    TextView tvMyLocation;

    Unbinder unbinder;
    String defaultInfo = "我是%s，我来自%s，我的联系方式是：%s，如有业务请与我联系我吧";

    UserBean userBean;
    private UserLocationBean userLocationBean;
    private TencentMap tencentMap;

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_my_intro;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        tvMyinfoContent = rootView.findViewById(R.id.tv_myinfo_content);
        tencentMap = mapView.getMap();
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

        userBean = (UserBean) get().getIntent().getSerializableExtra("bean");
        if (userBean == null) {
            userBean = AppCache.getUserBean();
        } else {
            tvMyinfoEdit.setVisibility(View.GONE);
        }
        //地址，简介
        RetrofitClient.getInstance().getApiService().getUserLocation(userBean.getUserid())
                .enqueue(new Callback<UserLocationBean>() {
                    @Override
                    public void onResponse(Call<UserLocationBean> call, Response<UserLocationBean> response) {
                        userLocationBean = response.body();
                        String content = String.format(defaultInfo, userLocationBean.getName(), userLocationBean.getAddress(), userBean.getMobile());
                        if (TextUtils.isEmpty(userBean.getContent())) {
                            tvMyinfoContent.setText(content);
                        } else {
                            tvMyinfoContent.setText(userBean.getContent());
                        }
                        tvOnSaleCount.setText(String.valueOf(userBean.getZaishou_count()));
                        tvSaleOutCount.setText(String.valueOf(userBean.getYishou_count()));
                        //设置地图
                        tencentMap.setZoom(18);
//                        tencentMap.setTrafficEnabled(true);
                        LatLng latLng = new LatLng(Double.parseDouble(userLocationBean.getLatitude()), Double.parseDouble(userLocationBean.getLongitude()));
                        tencentMap.setCenter(latLng);
                        tencentMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("当前位置")
                                .anchor(1, 1)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                                .draggable(false));
                        //地址
                        RetrofitClient.getInstance().getApiService().getAddressByLgt(userBean.getMobile(), userLocationBean.getLongitude(), userLocationBean.getLatitude())
                                .enqueue(new Callback<BaseBean>() {
                                    @Override
                                    public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                                        BaseBean body = response.body();
                                        if (body != null) {
                                            tvMyLocation.setText(body.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseBean> call, Throwable t) {

                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<UserLocationBean> call, Throwable t) {

                        Toasty.warning(getActivity(), t.getMessage()).show();
                    }
                });


        //图片
        RetrofitClient.getInstance().getApiService().getUserImageList(userBean.getUserid())
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Log.e("圖片", response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toasty.error(getActivity(), t.getMessage()).show();
                    }
                });
    }

    @Subscribe
    public void onReceiveLoginSuccess(Integer eventId) {
        if (eventId == Constants.WX_LOGIN_SUCCESS) {
            initData();
        }
    }

    @Subscribe
    public void onRefreshData(Integer eventId) {
        if (eventId == Constants.REFRESH_EVENT) {
            getUser(AppCache.getUserBean().getUserid());
        }

    }

    /**
     * 获取用户信息
     *
     * @param userid
     */
    void getUser(String userid) {
        RetrofitClient.getInstance().getApiService().getUser(userid).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (response.body() != null) {
                    AppCache.setUserBean(response.body());
                    initData();
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                ToastUtils.show(CcqApp.getAppContext(), t.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.tv_myinfo_edit)
    public void onViewClicked() {
        Intent i = new Intent(get(), EditMyIntroActivity.class);
        get().startActivity(i);
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.setVisibility(View.INVISIBLE);
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.setVisibility(View.VISIBLE);
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        super.onStop();
    }

}

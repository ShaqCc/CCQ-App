package com.ccq.app.ui.user.introduce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.entity.UserBean;
import com.ccq.app.entity.UserLocationBean;
import com.ccq.app.ui.TencentMapActivity;
import com.ccq.app.ui.user.EditMyIntroActivity;
import com.ccq.app.ui.user.TabHomeFragment;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.GlideImageLoader;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.youth.banner.Banner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/****************************************
 * 功能说明:  我的--简介 页签
 *****************************************/

public class TabIntroFragment extends BaseFragment<UserIntroPresenter> implements IUserIntroView {


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
    private UserImageBean imageBean;

    private final int requestCode_INFO = 111;
    private final int requestCode_IMAGE = 123;
    private final int requestCode_LOCATION = 456;
    private boolean isUserSelf = true;


    @OnClick(R.id.tv_myinfo_edit)
    public void onViewClicked() {
        Intent i = new Intent(get(), EditMyIntroActivity.class);
        startActivityForResult(i, requestCode_INFO);
    }

    @OnClick(R.id.tv_myimg_edit)
    public void click(View view) {
        Intent intent = new Intent();
        intent.setClass(getAty(), EditUserImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EditUserImageActivity.KEY_IMAGE, (Serializable) imageBean);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode_IMAGE);
    }

    @OnClick(R.id.tv_mylocation_edit)
    public void editLocation(View view) {
        Intent intent = new Intent();
        intent.setClass(get(), TencentMapActivity.class);
        intent.putExtra(TencentMapActivity.KEY_ONLY_SHOW, false);
        intent.putExtra(TencentMapActivity.KEY_ADDRESS, userLocationBean.getAddress());
        intent.putExtra(TencentMapActivity.KEY_LONGITUDE, userLocationBean.getLongitude());
        intent.putExtra(TencentMapActivity.KEY_LATITUDE, userLocationBean.getLatitude());
        startActivityForResult(intent, requestCode_LOCATION);
    }

    Unbinder unbinder;
    String defaultInfo = "我是%s，我来自%s，我的联系方式是：%s，如有业务请与我联系我吧";

    UserBean otherUserBean;
    private UserLocationBean userLocationBean;
    private TencentMap tencentMap;

    @Override
    protected int inflateContentView() {
        return R.layout.fragment_my_intro;
    }

    @Override
    protected UserIntroPresenter createPresenter() {
        return new UserIntroPresenter(this);
    }

    @Override
    protected void initView(View rootView) {
        isUserSelf = getArguments().getBoolean(TabHomeFragment.KEY_IS_SELF);
        tvMyinfoContent = rootView.findViewById(R.id.tv_myinfo_content);
        tencentMap = mapView.getMap();
    }

    @Override
    public void initData() {
        if (!isUserSelf) {
            otherUserBean = (UserBean) getArguments().getSerializable("bean");
            tvMyinfoEdit.setVisibility(View.GONE);
            tvMyimgEdit.setVisibility(View.GONE);
            tvMylocationEdit.setVisibility(View.GONE);
            //地址，简介
            mPresenter.queryLocation(otherUserBean);
            //图片列表
            mPresenter.queryImageList(otherUserBean);
        } else {
            tvMyinfoEdit.setVisibility(View.VISIBLE);
            tvMyimgEdit.setVisibility(View.VISIBLE);
            tvMylocationEdit.setVisibility(View.VISIBLE);
            //地址，简介
            mPresenter.queryLocation(AppCache.getUserBean());
            //图片列表
            mPresenter.queryImageList(AppCache.getUserBean());
        }
    }


    /**
     * 获取地址回调
     *
     * @param bean
     */
    @Override
    public void setLocation(UserLocationBean bean) {
        userLocationBean = bean;
        UserBean temp = null;
        if (isUserSelf) {
            temp = AppCache.getUserBean();
        } else
            temp = otherUserBean;
        String content = String.format(defaultInfo, userLocationBean.getName(), userLocationBean.getAddress(), temp.getMobile());
        if (TextUtils.isEmpty(temp.getContent())) {
            tvMyinfoContent.setText(content);
        } else {
            tvMyinfoContent.setText(temp.getContent());
        }
        tvOnSaleCount.setText(String.valueOf(temp.getZaishou_count()));
        tvSaleOutCount.setText(String.valueOf(temp.getYishou_count()));
        //设置地图
        tencentMap.setZoom(15);
        LatLng latLng = new LatLng(Double.parseDouble(userLocationBean.getLatitude()), Double.parseDouble(userLocationBean.getLongitude()));
        tencentMap.setCenter(latLng);
        tencentMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("当前位置")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                .draggable(false));
    }

    /**
     * 从经纬度获取地址回调
     *
     * @param address
     */
    @Override
    public void setAddress(String address) {
        tvMyLocation.setText(address);
    }

    /**
     * 图片简介回调
     *
     * @param data
     */
    @Override
    public void setImageList(UserImageBean data) {
        imageBean = data;
        if (data != null && data.getData() != null && data.getData().size() > 0) {
            Collections.sort(imageBean.getData());
            ArrayList<String> imageList = new ArrayList<>();
            List<UserImageBean.DataBean> list = data.getData();
            for (int i = 0; i < list.size(); i++) {
                String osspath = list.get(i).getResources().getOsspath();
                if (!TextUtils.isEmpty(osspath) && osspath.startsWith("http")) {
                    imageList.add(osspath + "!auto");
                }
            }
            myinfoBanner.setImageLoader(new GlideImageLoader());
            myinfoBanner.setImages(imageList);
            myinfoBanner.setDelayTime(2000);
            myinfoBanner.startAutoPlay();
            myinfoBanner.start();
        }
    }

    @Override
    public void setUserIntoduce(UserBean user) {

        if (isUserSelf) {
            AppCache.setUserBean(user);
        } else {
            otherUserBean = user;
        }
        if (TextUtils.isEmpty(user.getContent())) {
            String content = String.format(defaultInfo, userLocationBean.getName(), userLocationBean.getAddress(), user.getMobile());
            tvMyinfoContent.setText(content);
        } else {
            tvMyinfoContent.setText(user.getContent());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == requestCode_INFO) {
                if (isUserSelf) {
                    mPresenter.getUserInfo(AppCache.getUserBean().getUserid());

                } else mPresenter.getUserInfo(otherUserBean.getUserid());
            } else if (requestCode == requestCode_IMAGE) {
                if (isUserSelf) {
                    mPresenter.getUserInfo(AppCache.getUserBean().getUserid());

                } else mPresenter.getUserInfo(otherUserBean.getUserid());
            } else if (requestCode == requestCode_LOCATION) {
                //上传位置
                LatLng point = new LatLng(data.getDoubleExtra(TencentMapActivity.KEY_LATITUDE, 0),
                        data.getDoubleExtra(TencentMapActivity.KEY_LONGITUDE, 0));
                String locAddress = data.getStringExtra(TencentMapActivity.KEY_ADDRESS);
                mPresenter.updateLocation(point, locAddress);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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

    @Override
    public BaseActivity getAty() {
        return getHostActivity();
    }

}

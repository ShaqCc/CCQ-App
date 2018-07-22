package com.ccq.app.ui;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccq.app.R;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.Car;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Utils;
import com.ccq.app.weidget.Toasty;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/21 21:13
 * 描述：
 * 版本：
 *
 **************************************************/

public class TencentMapActivity extends Activity {

    private boolean isOnlyShow;
    //    private Car car;
    private double markLatitude;
    private double markLongitude;
    private TencentMap map;
    private TencentLocationManager locationManager;
    private TencentLocationListener listener;
    private TencentLocationRequest request;

    @OnClick({R.id.activity_map_titlebar_btnRight, R.id.activity_map_titlebar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_map_titlebar_btnRight:
                Intent intent = new Intent();
                intent.putExtra("address", tvLocation.getText().toString());
                intent.putExtra("latitude", markLatitude);
                intent.putExtra("longitude", markLongitude);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.activity_map_titlebar_back:
                finish();
                break;
        }
    }

    @BindView(R.id.map_marker)
    ImageView ivmarker;

    @BindView(R.id.tencent_map)
    MapView tencentMap;

    @BindView(R.id.tv_location)
    TextView tvLocation;

    @BindView(R.id.activity_map_titlebar_btnRight)
    TextView btEnsure;

    public static String KEY_LATITUDE = "latitude";
    public static String KEY_LONGITUDE = "longitude";
    public static String KEY_ADDRESS = "address";
    public static String KEY_ONLY_SHOW = "showMap";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tencent_map_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        map = tencentMap.getMap();
        map.setZoom(16);
        tencentMap.getUiSettings().setAnimationEnabled(true);
        tencentMap.getUiSettings().setScaleViewPosition(UiSettings.LOGO_POSITION_LEFT_BOTTOM);
        map.setOnMapCameraChangeListener(new TencentMap.OnMapCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LatLng position = cameraPosition.getTarget();
                markLatitude = position.getLatitude();
                markLongitude = position.getLongitude();
                RetrofitClient.getInstance().getApiService().getAddressByLgt("", String.valueOf(markLongitude), String.valueOf(markLatitude))
                        .enqueue(new Callback<BaseBean>() {
                            @Override
                            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                                BaseBean body = response.body();
                                if (body != null) {
                                    startMarkerAnimation();
                                    tvLocation.setText(body.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseBean> call, Throwable t) {

                            }
                        });
            }
        });

        Intent intent = getIntent();
        isOnlyShow = intent.getBooleanExtra(KEY_ONLY_SHOW, false);
        if (isOnlyShow) {
            markLatitude = intent.getDoubleExtra(KEY_LATITUDE, 39.5427);
            markLongitude = intent.getDoubleExtra(KEY_LONGITUDE, 116.2317);
            tvLocation.setText(intent.getStringExtra(KEY_ADDRESS));
            btEnsure.setVisibility(View.GONE);
            updateLocation(new LatLng(markLatitude, markLongitude));
        } else {
            LatLng latLng = null;
            if (AppCache.mLocation != null) {
                markLatitude = AppCache.mLocation.getLatitude();
                markLongitude = AppCache.mLocation.getLongitude();
                tvLocation.setText(Utils.getAddress(AppCache.mLocation));
                latLng = new LatLng(markLatitude, markLongitude);
                updateLocation(latLng);
            } else {
                //开始定位
                prepareLocationService();
            }
        }

    }

    private void startMarkerAnimation() {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(ivmarker, "translationY", 0f, 20f, 0f, 20f, 0f);
        translationY.setDuration(1000);
//        translationY.setInterpolator(new AccelerateDecelerateInterpolator());
        translationY.start();
    }

    private void updateLocation(LatLng latLng) {
        if (latLng != null) {
            map.setCenter(latLng);
        }
    }

    private void prepareLocationService() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, 0);
            } else {
                //开启定位
                initLocationListener();
            }
        } else {
            initLocationListener();
        }
    }

    private void initLocationListener() {
        listener = new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
                if (TencentLocation.ERROR_OK == error) {
                    AppCache.mLocation = tencentLocation;
                    markLatitude = AppCache.mLocation.getLatitude();
                    markLongitude = AppCache.mLocation.getLongitude();
                    tvLocation.setText(Utils.getAddress(tencentLocation));
                    updateLocation(new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude()));
                } else {
                    // 定位失败
                    AppCache.mLocation = null;
                    Toasty.success(getApplication(), error + "  :" + s).show();
                }
                locationManager.removeUpdates(listener);
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {

            }
        };

        request = TencentLocationRequest.create();
        request.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_POI);
        locationManager = TencentLocationManager.getInstance(getApplicationContext());
        locationManager.requestLocationUpdates(request, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 && grantResults.length >= 1) {
            //开启定位
            initLocationListener();
        }
    }
}

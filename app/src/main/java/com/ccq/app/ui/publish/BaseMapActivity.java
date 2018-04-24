package com.ccq.app.ui.publish;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ccq.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @ClassName: BaseMapActivity
 * @author: MLY
 * @date: 2018年2月18日
 */
public class BaseMapActivity extends Activity implements OnGetGeoCoderResultListener {

    @SuppressWarnings("unused")
    private static final String LTAG = BaseMapActivity.class.getSimpleName();
    @BindView(R.id.activity_map_titlebar_back)
    ImageView activityMapTitlebarBack;
    @BindView(R.id.activity_map_titlebar_btnRight)
    TextView activityMapTitlebarBtnRight;
    @BindView(R.id.titlebar)
    RelativeLayout titlebar;
    @BindView(R.id.bmapView)
    MapView bmapView;
    @BindView(R.id.zywz)
    ImageView zywz;


    private BaiduMap mBaiduMap;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private double markLatitude;
    private double markLongitude;
    private String markContent;
    private GeoCoder mSearch = null;
    private Activity ac;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // 介绍如何使用个性化地图，需在MapView 构造前设置个性化地图文件路径
        // 注: 设置个性化地图，将会影响所有地图实例。
        // MapView.setCustomMapStylePath("个性化地图config绝对路径");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_poi_location);
        ButterKnife.bind(this);
        ac = this;
        // 地图初始化
        bmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = bmapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_position);
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        LocationMode.NORMAL, true, mCurrentMarker,
                        accuracyCircleFillColor, accuracyCircleStrokeColor));
        initUI();
        initParam();
        initListeners();
        initGeoCoder();
    }

    private void initParam() {
        Intent intent = getIntent();
        if (intent.hasExtra("latlng")) {
            // 当用intent参数时，设置中心点为指定点
            Bundle bundle = intent.getExtras();
            LatLng poi = bundle.getParcelable("latlng");
            markLatitude = poi.latitude;
            markLongitude = poi.longitude;
            String address = intent.getStringExtra("address");
            if (!TextUtils.isEmpty(address)) {
                markContent = address;
            }

            updateView(poi);
        } else {//没有传过来数据 重新定位，改变视图

        }
    }

    // 切换视图
    private void updateView(LatLng poi) {
        //设定中心点坐标
        //定义地图状态
        try {
            MapStatus mMapStatus = new MapStatus.Builder().target(poi).zoom(18).build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {

    }

    private void initListeners() {
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {

            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                markLatitude = arg0.target.latitude;
                markLongitude = arg0.target.longitude;
            }
        });

    }

    private void initGeoCoder() {
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        bmapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        bmapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // activity 销毁时同时销毁地图控件
//        if (client != null) {
//            client.stopLocation();
//        }
        mBaiduMap.setMyLocationEnabled(false);
        bmapView.onDestroy();
        bmapView = null;
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            markContent = null;
            return;
        }
        if (!TextUtils.isEmpty(result.getAddress())) {
            markContent = result.getAddress()+result.getSematicDescription();
        }
        Intent data = new Intent();
        data.putExtra("longitude", markLongitude);
        data.putExtra("latitude", markLatitude);
        data.putExtra("address", markContent);
        setResult(RESULT_OK, data);
        BaseMapActivity.this.finish();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @OnClick({R.id.activity_map_titlebar_back, R.id.activity_map_titlebar_btnRight, R.id.bmapView, R.id.zywz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_map_titlebar_back:
                finish();
                break;
            case R.id.activity_map_titlebar_btnRight:
                LatLng ptCenter = new LatLng(markLatitude, markLongitude);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
                break;
            case R.id.bmapView:
                break;
            case R.id.zywz:
                break;
        }
    }

}

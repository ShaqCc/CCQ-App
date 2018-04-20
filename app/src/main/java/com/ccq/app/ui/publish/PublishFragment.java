package com.ccq.app.ui.publish;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.service.LocationService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/****************************************
 * 功能说明:  发布
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class PublishFragment extends BaseFragment {
    @BindView(R.id.btn_band_model)
    Button btnBandModel;
    @BindView(R.id.btn_car_age)
    Button btnCarAge;
    @BindView(R.id.et_travel_time)
    EditText etTravelTime;
    @BindView(R.id.tv_image_size)
    TextView tvImageSize;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.btn_car_location)
    Button btnCarLocation;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.tv_fold)
    TextView tvFold;
    @BindView(R.id.lin_addition)
    LinearLayout linAddition;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    Unbinder unbinder;
    @BindView(R.id.gridview_photo_video)
    GridView gridviewPhotoVideo;
    @BindView(R.id.scrollView_content)
    ScrollView scrollViewContent;
    private LocationService locationService;

    GridViewPhotoAdapter gridViewAdapter;

    private ArrayList<String>  mMultiSelectPath = new ArrayList<>();
    ;


    @Override
    protected int inflateContentView() {
        return R.layout.fragment_publish;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.drawable.add_photo);
        gridviewPhotoVideo.setColumnWidth(bm.getWidth() * 2);
        gridviewPhotoVideo.setHorizontalSpacing(1);
        gridviewPhotoVideo.setNumColumns(1);
        gridviewPhotoVideo.setStretchMode(GridView.NO_STRETCH);
        gridViewAdapter = new GridViewPhotoAdapter(getActivity(),mMultiSelectPath,bm.getWidth());
        gridviewPhotoVideo.setAdapter(gridViewAdapter);
     }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_band_model, R.id.btn_car_age, R.id.tv_more, R.id.btn_car_location, R.id.tv_fold, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_band_model:
                break;
            case R.id.btn_car_age:
                break;
            case R.id.tv_more:
                linAddition.setVisibility(View.VISIBLE);
                tvMore.setVisibility(View.GONE);
                tvFold.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_car_location:
                break;
            case R.id.tv_fold:
                linAddition.setVisibility(View.GONE);
                tvMore.setVisibility(View.VISIBLE);
                tvFold.setVisibility(View.GONE);
                break;
            case R.id.btn_submit:
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        locationService = ((CcqApp) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK

//        btnCarLocation.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (btnCarLocation.getText().toString().equals(getString(R.string.startlocation))) {
//                   startlocation locationService.start();// 定位SDK
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
//                    btnCarLocation.setText(getString(R.string.stoplocation));
//                } else {
//                    locationService.stop();
////                    btnCarLocation.setText(getString(R.string.startlocation));
//                }
//            }
//        });

    }

    @Override
    public void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                logMsg(location.getAddrStr());
            }
        }

    };


    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        final String s = str;
        try {
            if (btnCarLocation != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        btnCarLocation.post(new Runnable() {
                            @Override
                            public void run() {
                                btnCarLocation.setText(s);
                            }
                        });

                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

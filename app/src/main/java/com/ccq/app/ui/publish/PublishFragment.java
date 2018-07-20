package com.ccq.app.ui.publish;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.ccq.app.R;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.Car;
import com.ccq.app.entity.TypeBean;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.HttpClient;
import com.ccq.app.http.ProgressCallBack;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.service.LocationService;
import com.ccq.app.ui.MainActivity;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.DensityUtils;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.weidget.ListDialog;
import com.ccq.app.weidget.MyGridView;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.qiniu.android.jpush.utils.StringUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jiguang.chat.utils.ToastUtil;
import jiguang.chat.utils.imagepicker.ImageLoader;
import jiguang.chat.utils.imagepicker.ImagePicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.ccq.app.ui.publish.CheckListAdapter.ListItemClickListener;

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
    @BindView(R.id.et_car_price)
    EditText etCarPrice;
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
    MyGridView gridView;
    @BindView(R.id.scrollView_content)
    ScrollView scrollViewContent;
//    private LocationService locationService;

//    GridViewPhotoAdapter gridViewAdapter;

    private ArrayList<String> mMultiSelectPath = new ArrayList<>();
    private ArrayList<String> photoPath = new ArrayList<>();
    private ArrayList<String> videoPath = new ArrayList<>();
    private List<String> carAgeList = new ArrayList<>();
    private AlertDialog alert;
    private CheckListAdapter checkListAdapter;

    private LatLng point = null;//位置标记

    private String selectCarAge;
    public final int REQUEST_MAP_LOCATE_CODE = 1001;
    public final int REQUEST_BRAND_MODEL_CODE = 1002;
    public static final int RESULT_LOAD_IMAGE = 1003;
    public static final int RESULT_LOAD_VIDEO = 1004;

    private String locAddress = "";

    private BrandBean brandBean = new BrandBean();
    private TypeBean.NumberListBean brandModelBean;

    private ApiService apiService;

    private List<String> imgidList = new ArrayList<>();
    private List<String> videoidList = new ArrayList<>();
    private String imgids;
    private String videoids;

    private Car car;
    private Map<String, String> imgInfoMap;
    private boolean isLocal;
    private ChooseMediaAdapter mediaAdapter;
    private ProgressDialog loading;
    private Handler mHandler = new Handler();

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
        car = (Car) get().getIntent().getSerializableExtra("bean");
        if (car != null) {
            //修改的
            isLocal = false;
            initCarInfo();
        } else {
            //发布的
            isLocal = true;
            initGridView();
        }
        //图片选择配置
        setImageSetting();
        //手机号
        if (AppCache.getUserBean() != null) etUserPhone.setText(AppCache.getUserBean().getMobile());

    }

    /**
     * 初始化列表
     */
    private void initGridView() {
        gridView.setColumnWidth(DensityUtils.dp2px(get(), 82));
        mediaAdapter = new ChooseMediaAdapter(new ArrayList<String>(), get(), isLocal);
        gridView.setAdapter(mediaAdapter);
        mediaAdapter.setDeleteItemClickListener(new ChooseMediaAdapter.DeleteItemClickListener() {
            @Override
            public void onListItemClickListener(int position) {
                if (mMultiSelectPath.size() > position) {
                    String path = mMultiSelectPath.get(position);
                    if(!path.startsWith("http")){
                        mMultiSelectPath.remove(position);
                        if(photoPath!=null && photoPath.contains(path)){
                            photoPath.remove(path);
                        }
                        if(videoPath!=null && videoPath.contains(path)){
                            videoPath.remove(path);
                        }
                        mediaAdapter.notifyDataSetChanged();
                    }else{
                        deleteFileFromService(mMultiSelectPath.get(position));
                    }
                }
            }
        });
    }

    @Override
    public void initData() {
        apiService = RetrofitClient.getInstance().getApiService();
        getBDlocation();
        getNianFen();
    }

    private void initCarInfo() {
        if (car != null) {
            getCarLocation();
            etDescription.setText(car.getContent());

            btnBandModel.setText(car.getBrandName() + car.getNumberName());
            etUserPhone.setText(car.getPhone());
            etCarPrice.setText(String.valueOf(car.getPrice()));
            btnCarAge.setText(String.valueOf(car.getYear()));
            btnSubmit.setText("修改");

            brandBean.setId(car.getBrandId());
            if(brandModelBean == null){
                brandModelBean = new TypeBean.NumberListBean();
            }
            brandModelBean.setId(car.getNumberId());
            brandModelBean.setBid(car.getBrandId());
            brandModelBean.setTid(car.getTonnageId());

            List<com.ccq.app.entity.Car.PicImgBean> imgBeans = car.getPic_img();
            List<Car.VideoBean> videoBeans = car.getVideoList();
            imgInfoMap = new HashMap<>();

            if (imgBeans != null) {
                for (int i = 0; i < imgBeans.size(); i++) {
                    Car.PicImgBean img = imgBeans.get(i);
                    mMultiSelectPath.add(img.getSavename());
                    imgInfoMap.put(img.getSavename(), img.getId());
                    imgidList.add(img.getId());
                }
            }

            if (videoBeans != null && videoBeans.size()>0) {
                for (int i = 0; i < videoBeans.size(); i++) {
                    Car.VideoBean video = videoBeans.get(i);
                    mMultiSelectPath.add(video.getOsspath());
                    imgInfoMap.put(video.getOsspath(), video.getId());
                    videoidList.add(video.getId());
                }
            }

            initGridView();
            mediaAdapter.refresh(mMultiSelectPath);
        }
    }

    private void getCarLocation() {
        RetrofitClient.getInstance().getApiService().getCarAddress(String.valueOf(car.getId())).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Map<String, Object> map = (Map<String, Object>) response.body();
                if (0.0 == (Double) map.get("code")) {
                    car.setDetailAddress((String) map.get("address"));
                    car.setLatitude(map.get("latitude").toString());
                    car.setLongitude(map.get("longitude").toString());

                    btnCarLocation.setText(car.getAddress());
                    if (!TextUtils.isEmpty(car.getLatitude()) && !TextUtils.isEmpty(car.getLongitude())) {
                        point = new LatLng(Double.parseDouble(car.getLatitude()), Double.parseDouble(car.getLongitude()));
                    }

                } else {
                    ToastUtils.show(get(), (String) map.get("message"));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    public void initYearList() {
        checkListAdapter = new CheckListAdapter(getActivity(), carAgeList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                showBrandSelectDialog();
                break;
            case R.id.btn_car_age:
                showSelectDialog();
                break;
            case R.id.tv_more:
                linAddition.setVisibility(View.VISIBLE);
                tvMore.setVisibility(View.GONE);
                tvFold.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_car_location:
                selectLocationAtMap();
                break;
            case R.id.tv_fold:
                linAddition.setVisibility(View.GONE);
                tvMore.setVisibility(View.VISIBLE);
                tvFold.setVisibility(View.GONE);
                break;
            case R.id.btn_submit:
                sendCheck();
                break;
        }
    }

    /**
     * 检查是否登录，和 今日是否可上报（最多3次）
     */
    private void sendCheck() {

        if(isLocal){
            if (AppCache.getUserBean() == null) {
                ToastUtils.show(get(), "请先登录");
                return;
            }

            apiService.sendCheck(AppCache.getUserBean().getUserid()).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Map<String, Object> map = (Map<String, Object>) response.body();
                    if (0.0 == (Double) map.get("code")) {
                        uploadFile();
                    } else {
                        ToastUtils.show(get(), (String) map.get("message"));
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        }else{
            //修改信息
            uploadFile();
        }

    }

    private void uploadFile() {

        if (!validate()) {
            return;
        }
        loading = new ProgressDialog(get());
        loading.setMessage("图片上传中...");
        loading.show();

        Observable.create(
                new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                        if (!emitter.isDisposed()) {
                            try {
                                //访问网络操作
                                if (photoPath != null && photoPath.size() > 0) {
                                    for (String path : photoPath) {
                                        uploadImg(path);
                                    }
                                }

                                if (videoPath != null && videoPath.size() > 0) {
                                    for (String path : videoPath) {
                                        uploadVideo(path);
                                    }
                                }
                                emitter.onNext(true);
                                emitter.onComplete();

                            } catch (IOException ioe) {
                                emitter.onError(ioe);
                            }

                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        if (d.isDisposed()) {
                        }
                    }

                    @Override
                    public void onNext(Boolean boo) {
                        if (boo) {
                            imgids = StringUtils.join(imgidList.toArray(new String[imgidList.size()]), ",");
                            videoids = StringUtils.join(videoidList.toArray(new String[videoidList.size()]), ",");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        submitData();
                    }
                });

    }

    private void getNianFen() {
        apiService.getCarNianFenList().enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response != null && response.body() != null) {
                    List<Object> list = response.body();
                    if (list.size() > 0) {
                        for (Object obj : list) {
                            com.google.gson.jpush.JsonObject returnData = new com.google.gson.jpush.JsonParser().parse(obj.toString()).getAsJsonObject();
                            String year = returnData.get("name").getAsString();
                            carAgeList.add(year);
                        }
                        initYearList();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {

            }
        });
    }

    private void uploadImg(String path) throws IOException {

        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);

        Call<Object> result = apiService.uploadImages(body);
        Response<Object> response = result.execute();
        Map<String, Object> map = (Map<String, Object>) response.body();
        if (0.0 == (Double) map.get("code")) {
            imgidList.add(String.valueOf(map.get("imageid")));
        }

    }

    private void uploadVideo(String path) throws IOException {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);

        Call<Object> result = apiService.uploadVideos(body);
        Response<Object> response = result.execute();
        Map<String, Object> map = (Map<String, Object>) response.body();
        if (0.0 == (Double) map.get("code")) {
            videoidList.add(String.valueOf(map.get("imageid")));
        }
    }

    private boolean validate() {
        if (brandModelBean == null || TextUtils.isEmpty(brandModelBean.getId())) {
            ToastUtils.show(get(), "请选择品牌型号");
            return false;
        }
        if (TextUtils.isEmpty(btnCarAge.getText())) {
            ToastUtils.show(get(), "请选择车龄");
            return false;
        }
        if (mMultiSelectPath == null || mMultiSelectPath.size() < 4 || mMultiSelectPath.size() > 18) {
            ToastUtils.show(get(), "请上传4-18张图片");
            return false;
        }
        if (TextUtils.isEmpty(etUserPhone.getText())) {
            ToastUtils.show(get(), "请输入手机号码");
            return false;
        }
        return true;
    }

    private void submitData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", AppCache.getUserBean().getUserid());
        map.put("address", btnCarLocation.getText().toString());
        map.put("content", etDescription.getText().toString());
        map.put("imglist", TextUtils.isEmpty(imgids) ? "" : imgids);
        map.put("videolist", TextUtils.isEmpty(videoids) ? "" : videoids);
        map.put("latitude", String.valueOf(point.latitude));
        map.put("longitude", String.valueOf(point.longitude));
        map.put("phone", etUserPhone.getText().toString());
        map.put("year", btnCarAge.getText().toString());
        map.put("price", etCarPrice.getText().toString());
        map.put("pinpai", brandModelBean.getBid());
        map.put("tonnage",brandModelBean.getTid());
        map.put("number", brandModelBean.getId());

        if(isLocal){
            apiService.addCarInfo(map).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Map<String, Object> map = (Map<String, Object>) response.body();
                    String message = (String) map.get("message");
                    if (0.0 == (Double) map.get("code")) {
                        message = "发布成功";
                    }
                    dismissLoading();
                    ToastUtils.show(get(), message);
                    //重置页面
                    reset();
                    //跳转到首页
                    ((MainActivity) getActivity()).setCurrentTab(0);
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    dismissLoading();
                    if (t != null) {
                        Log.e("sssss====", t.getCause().toString());
                    }
                }
            });
        }else{
            map.put("carid",car.getId());
            map.put("BrandName",car.getBrandName());
            map.put("TonnageName",car.getTonnageName());
            map.put("NumberName",car.getNumberName());
            map.put("ProvinceName",car.getProvinceName());
            map.put("CityName",car.getCityName());

            apiService.editCarInfo(map).enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Map<String, Object> map = (Map<String, Object>) response.body();
                    String message = (String) map.get("message");
                    if (0.0 == (Double) map.get("code")) {
                        message = "修改成功";
                    }
                    dismissLoading();
                    ToastUtils.show(get(), message);
                    get().finish();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    dismissLoading();
                    if (t != null && t.getCause()!=null) {
                        Log.e("sssss====", t.getCause().toString());
                    }
                }
            });
        }

    }

    private void dismissLoading() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (loading != null && loading.isShowing()) {
                    loading.dismiss();
                }
            }
        });
    }

    /**
     * 重置页面
     */
    private void reset() {
        btnBandModel.setText("");
        btnCarAge.setText("");
        etCarPrice.setText("");
        mMultiSelectPath.clear();
        photoPath.clear();
        videoPath.clear();
        mediaAdapter.refresh(mMultiSelectPath);
    }


    private void deleteFileFromService(final String deletePath) {
        Call<Object> call;
        if (deletePath.endsWith("jpg") || deletePath.endsWith("jpeg") || deletePath.endsWith("png") || deletePath.endsWith("bmp")) {
            call = apiService.delCarImg(imgInfoMap.get(deletePath));
        } else {
            call = apiService.delCarVideo(AppCache.getUserBean().getUserid(), imgInfoMap.get(deletePath));
        }
        HttpClient.getInstance(get()).sendRequest(call, new ProgressCallBack<Object>(get(), true, "正在删除") {
            @Override
            protected void onSuccess(Object response) {
                super.onSuccess(response);
                if (response != null) {
                    Map<String, Object> map = (Map<String, Object>) response;
                    if (0.0 == (Double) map.get("code")) {
                        if (photoPath.size() > 0 && photoPath.contains(deletePath)) {
                            photoPath.remove(deletePath);
                        }
                        if (videoPath.size() > 0 && videoPath.contains(deletePath)) {
                            videoPath.remove(deletePath);
                        }
                        mMultiSelectPath.remove(deletePath);
                        mediaAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            protected void onFailure(Object response) {
                super.onFailure(response);
                ToastUtil.shortToast(get(), "删除失败");
            }

            @Override
            protected void onError(Throwable t) {
                super.onError(t);
            }
        });
    }

    public void showBrandSelectDialog() {

        Intent intent = new Intent();
        intent.setClass(get(), BrandModelSelectActivity.class);
        startActivityForResult(intent, REQUEST_BRAND_MODEL_CODE);

    }

    public void getBDlocation() {
//        locationService = ((CcqApp) getActivity().getApplication()).locationService;
//        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
//        locationService.registerListener(mListener);
//        LocationClientOption option = locationService.getDefaultLocationClientOption();
//        option.setScanSpan(0);
//        locationService.setLocationOption(option);
//        locationService.start();// 定位SDK
    }


    @Override
    public void onStop() {
//        if (locationService != null) {
//            locationService.unregisterListener(mListener); //注销掉监听
//            locationService.stop(); //停止定位服务
//        }
        super.onStop();
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                logMsg(location.getAddrStr());
                point = new LatLng(location.getLatitude(), location.getLongitude());
            }
        }

    };

    private void selectLocationAtMap() {
        Intent intent = new Intent();
        intent.setClass(get(), BaseMapActivity.class);
        if (point != null) {
            intent.putExtra("latlng", point);
            intent.putExtra("address", locAddress);
        }
        startActivityForResult(intent, REQUEST_MAP_LOCATE_CODE);
    }

    private void showSelectDialog() {
        int selectPosition = carAgeList.indexOf(selectCarAge);
        final ListDialog mdialog = new ListDialog(get());
        alert = new AlertDialog.Builder(get()).create();
        alert.setView(mdialog.getView(), -1, -1, -1, -1);
        checkListAdapter.setSelectItem(selectPosition);
        checkListAdapter.setListItemClickListener(new ListItemClickListener() {
            @Override
            public void onListItemClickListener(int position) {
                selectCarAge = carAgeList.get(position);
                btnCarAge.setText(selectCarAge);
                alert.dismiss();
            }
        });
        mdialog.getDateListView().setAdapter(checkListAdapter);
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            //选择图片结果
            ArrayList<Media> select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (select != null) {
                for (int i = 0; i < select.size(); i++) {
                    String path = select.get(i).path;
                    mMultiSelectPath.add(path);
                    photoPath.add(path);
                }
                mediaAdapter.refresh(mMultiSelectPath);
            }

        } else if (requestCode == 400 && resultCode == PickerConfig.RESULT_CODE) {
            //选择视频
            ArrayList<Media> select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (select != null) {
                for (int i = 0; i < select.size(); i++) {
                    String path = select.get(i).path;
                    mMultiSelectPath.add(path);
                    videoPath.add(path);
                }
                mediaAdapter.refresh(mMultiSelectPath);
            }
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_MAP_LOCATE_CODE:
                    point = new LatLng(data.getDoubleExtra("latitude", 0), data.getDoubleExtra("longitude", 0));
                    locAddress = data.getStringExtra("address");
                    btnCarLocation.setText(TextUtils.isEmpty(locAddress) ? "已标记" : locAddress);
                    break;
                case REQUEST_BRAND_MODEL_CODE:
                    brandBean = (BrandBean) data.getSerializableExtra("brand");
                    brandModelBean = (TypeBean.NumberListBean) data.getSerializableExtra("model");
                    btnBandModel.setText(brandBean.getName() + "  " + brandModelBean.getName());
                    break;
            }
        }

    }


    private void setImageSetting() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
//        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(9);    //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

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

    public void initVars(){
        photoPath.clear();
        videoPath.clear();
        mMultiSelectPath.clear();
    }
}

class PicassoImageLoader implements ImageLoader {

    @Override
    public void displayImages(Activity activity, String path, ImageView imageView, int width, int height) {
        Picasso.with(activity)//
                .load(Uri.fromFile(new File(path)))//
                .placeholder(R.mipmap.default_image)//
                .error(R.mipmap.default_image)//
                .resize(width, height)//
                .centerInside()//
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }
}

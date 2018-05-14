package com.ccq.app.ui.publish;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.model.LatLng;
import com.ccq.app.R;
import com.ccq.app.base.BaseBean;
import com.ccq.app.base.BaseFragment;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.BrandModelBean;
import com.ccq.app.entity.CarInfo;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.service.LocationService;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.FileUtil;
import com.ccq.app.utils.ToastUtils;
import com.ccq.app.weidget.ListDialog;
import com.ccq.app.weidget.MyGridView;

import com.qiniu.android.jpush.utils.StringUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import jiguang.chat.utils.imagepicker.ImageGridActivity;
import jiguang.chat.utils.imagepicker.ImageLoader;
import jiguang.chat.utils.imagepicker.ImagePicker;
import jiguang.chat.utils.imagepicker.bean.ImageItem;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

import static android.app.Activity.RESULT_OK;
import static com.ccq.app.ui.publish.CheckListAdapter.*;

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
    MyGridView gridviewPhotoVideo;
    @BindView(R.id.scrollView_content)
    ScrollView scrollViewContent;
    private LocationService locationService;

    GridViewPhotoAdapter gridViewAdapter;

    private ArrayList<String>  mMultiSelectPath = new ArrayList<>();
    private ArrayList<String>  photoPath = new ArrayList<>();
    private ArrayList<String>  videoPath = new ArrayList<>();
    private List<String> carAgeList = new ArrayList<>();
    private AlertDialog alert;
    private CheckListAdapter checkListAdapter;

    private LatLng point=null;//位置标记

    private String selectCarAge;
    private final int REQUEST_MAP_LOCATE_CODE = 1001 ;
    private final int REQUEST_BRAND_MODEL_CODE = 1002 ;
    private static final int RESULT_LOAD_IMAGE = 1003;
    private static final int RESULT_LOAD_VIDEO = 1004;

    private String locAddress="";

    private BrandBean brandBean;
    private BrandModelBean brandModelBean;

    private ApiService apiService;

    private List<String> imgidList = new ArrayList<>();
    private List<String> videoidList = new ArrayList<>();
    private String imgids;
    private String videoids;


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
        setImageSetting();
        setGridViewAdapter();
        if(AppCache.getUserBean()!=null) etUserPhone.setText(AppCache.getUserBean().getMobile());

        gridviewPhotoVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == mMultiSelectPath.size()){
                    //选照片
//                    Intent intentPic = new Intent(
//                            Intent.ACTION_GET_CONTENT);
//                    intentPic.addCategory(Intent.CATEGORY_OPENABLE);
//                    intentPic.setType("image/*");
//                    startActivityForResult(intentPic,
//                            RESULT_LOAD_IMAGE);

                    Intent intent = new Intent(get(), ImageGridActivity.class);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);

                }else if(position == mMultiSelectPath.size() +1){
                    //视频
                    Intent intentPic = new Intent(
                            Intent.ACTION_GET_CONTENT);
                    intentPic.addCategory(Intent.CATEGORY_OPENABLE);
                    intentPic.setType("video/*");
                    startActivityForResult(intentPic,
                            RESULT_LOAD_VIDEO);
                 }else {
                    //显示图片


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

    public void initYearList(){
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
//                sendCheck();
                uploadFile();
                break;
        }
    }

    /**
     * 检查是否登录，和 今日是否可上报（最多五次）
     */
    private void sendCheck(){
        if(AppCache.getUserBean()==null){
            ToastUtils.show(get(),"请先登录");
            return ;
        }

        apiService.sendCheck(AppCache.getUserBean().getUserid()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Map<String,Object> map = (Map<String, Object>) response.body();
                if(0.0== (Double)map.get("code")) {
                    uploadFile();
                }else{
                    ToastUtils.show(get(), (String) map.get("message"));
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void uploadFile(){

        if(!validate()){
            return ;
        }

        Observable.create(
                new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                        if (!emitter.isDisposed()){
                            try{
                                //访问网络操作
                                if(photoPath!=null && photoPath.size()>0){
                                    for (String path: photoPath){
                                        uploadImg(path);
                                    }
                                }

                                if(videoPath!=null && videoPath.size() >0){
                                    for (String path: videoPath){
                                        uploadVideo(path);
                                    }
                                }
                                emitter.onNext(true);
                                emitter.onComplete();

                            }catch (IOException ioe){
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
                        if(d.isDisposed()){
                            Log.e(" on subscribe -------" ,"===========");
                        }
                    }

                    @Override
                    public void onNext(Boolean boo) {
                        Log.e(" on Next ------" ,"===========");

                        if(boo){
                            imgids = StringUtils.join(imgidList.toArray(new String[imgidList.size()]),",");
                            videoids = StringUtils.join(videoidList.toArray(new String[videoidList.size()]),",");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(" on Error ------" ,"===========");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(" on Complete ------" ,"===========");
                        submitData();
                    }
                });
//
//        if(photoPath!=null && photoPath.size()>0){
//            for (String path: photoPath){
//                uploadImg(path);
//            }
//        }
//
//        if(videoPath!=null && videoPath.size() >0){
//            for (String path: videoPath){
//                uploadVideo(path);
//            }
//        }
//
//        imgids = StringUtils.join(imgidList.toArray(new String[imgidList.size()]),",");
//        videoids = StringUtils.join(videoidList.toArray(new String[videoidList.size()]),",");
//
//        submitData();
    }

    private void getNianFen(){
        apiService.getCarNianFenList().enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response!=null && response.body()!=null){
                    List<Object> list = response.body();
                    if(list.size()>0){
                        for (Object obj : list){
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
            Map<String,Object> map = (Map<String, Object>) response.body();
            if(0.0== (Double)map.get("code")) {
                imgidList.add(String.valueOf(map.get("imageid")));
            }

    }

    private void  uploadVideo(String path) throws IOException{
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body  = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);

        Call<Object> result = apiService.uploadVideos(body);
            Response<Object> response = result.execute();
            Map<String,Object> map = (Map<String, Object>) response.body();
            if(0.0== (Double)map.get("code")) {
                videoidList.add(String.valueOf(map.get("imageid")));
            }
    }

    private boolean validate(){
        if(brandModelBean==null){
            ToastUtils.show(get(),"请选择品牌型号");
            return false;
        }
        if(TextUtils.isEmpty(btnCarAge.getText())){
            ToastUtils.show(get(),"请选择车龄");
            return false;
        }
        if(TextUtils.isEmpty(etUserPhone.getText())){
            ToastUtils.show(get(),"请输入手机号码");
            return false;
        }
        if(mMultiSelectPath == null || mMultiSelectPath.size()< 4 ||mMultiSelectPath.size()>18){
            ToastUtils.show(get(),"请上传4-18张图片");
            return false;
        }
        return true;
    }

    private void submitData(){
//        CarInfo  car = new CarInfo();
//        car.setAddress(btnCarLocation.getText().toString());
//        car.setContent(etDescription.getText().toString());
//        car.setImglist(imgids);
//        car.setVideolist(videoids);
//        car.setLatitude(String.valueOf(point.latitude));
//        car.setLongitude(String.valueOf(point.longitude));
//        car.setNumber(Integer.parseInt(brandModelBean.getId()));
//        car.setPhone(etUserPhone.getText().toString());
//        car.setPhoneCode("");
//        car.setPinpai(Integer.parseInt(brandBean.getId()));
//        if(!TextUtils.isEmpty(etCarPrice.getText().toString())){
//            car.setPrice(Float.parseFloat(etCarPrice.getText().toString()));
//        }
//        if(AppCache.getUserBean()!=null){
//            car.setUserid(Integer.valueOf(AppCache.getUserBean().getUserid()));
//        }
//        car.setYear(Integer.parseInt(btnCarAge.getText().toString()));

        HashMap<String,Object> map = new HashMap<>();
        map.put("address",btnCarLocation.getText().toString());
        map.put("content",etDescription.getText().toString());
        map.put("imglist",TextUtils.isEmpty(imgids)?"":imgids);
        map.put("videolist",TextUtils.isEmpty(videoids)?"":videoids);
        map.put("latitude",point.latitude);
        map.put("longitude",point.longitude);
        map.put("number",brandModelBean.getId());
        map.put("phone",etUserPhone.getText().toString());
        map.put("pinpai",brandBean.getId());
        map.put("price",etCarPrice.getText().toString());
        map.put("userid",AppCache.getUserBean().getUserid());
        map.put("year",btnCarAge.getText().toString());

        apiService.addCarInfo(map).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Map<String,String> map = (Map<String, String>) response.body();
                String message = map.get("message");
                if("0.0".equals(map.get("code"))||"0".equals(map.get("code"))) {
                    message = "发布成功";
                }
                ToastUtils.show(get(),message);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if(t!=null){
                    Log.e("sssss====",t.getCause().toString());
                }
            }
        });

    }

    private void setGridViewAdapter(){
        Bitmap bm = BitmapFactory.decodeResource(this.getResources(),R.drawable.icon_add_photo);
        gridviewPhotoVideo.setColumnWidth( (int)(bm.getWidth()* 1.5));
        gridviewPhotoVideo.setHorizontalSpacing(1);
        gridviewPhotoVideo.setStretchMode(GridView.NO_STRETCH);
        gridViewAdapter = new GridViewPhotoAdapter(getActivity(),mMultiSelectPath,bm.getWidth());
        gridviewPhotoVideo.setAdapter(gridViewAdapter);
        gridViewAdapter.setDeleteItemClickListener(new GridViewPhotoAdapter.DeleteItemClickListener() {
            @Override
            public void onListItemClickListener(int position) {
                //删除图片
                String deletePath = mMultiSelectPath.get(position);
                if(photoPath.size()>0 && photoPath.contains(deletePath)){
                    photoPath.remove(deletePath);
                }
                if(videoPath.size()>0 && videoPath.contains(deletePath)){
                    videoPath.remove(deletePath);
                }
                mMultiSelectPath.remove(deletePath);
                setGridViewAdapter();
            }
        });
    }


    public void showBrandSelectDialog(){

        Intent intent = new Intent();
        intent.setClass(get(), BrandModelSelectActivity.class);
        startActivityForResult(intent,REQUEST_BRAND_MODEL_CODE);

    }

    public void getBDlocation(){
        locationService = ((CcqApp) getActivity().getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        LocationClientOption option = locationService.getDefaultLocationClientOption();
        option.setScanSpan(0);
        locationService.setLocationOption(option);
        locationService.start();// 定位SDK
    }


    @Override
    public void onStop() {
        if(locationService!=null){
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
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

    private void selectLocationAtMap(){
        Intent intent = new Intent();
        intent.setClass(get(), BaseMapActivity.class);
        if(point!=null){
            intent.putExtra("latlng",point);
            intent.putExtra("address",locAddress);
        }
        startActivityForResult(intent,REQUEST_MAP_LOCATE_CODE);
    }

    private void showSelectDialog( ) {
        int selectPosition = carAgeList.indexOf(selectCarAge);
        final ListDialog mdialog = new ListDialog(get());
        alert = new AlertDialog.Builder(get()).create();
        alert.setView(mdialog.getView(), -1, -1, -1, -1);
        checkListAdapter.setSelectItem(selectPosition);
        checkListAdapter.setListItemClickListener(new ListItemClickListener() {
            @Override
            public void onListItemClickListener(int position) {
                selectCarAge = carAgeList.get( position);
                btnCarAge.setText(selectCarAge);
                alert.dismiss();
            }
        });
        mdialog.getDateListView().setAdapter(checkListAdapter);
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if(images!=null && images.size()>0){
                for (ImageItem item : images){
                    String path = item.path;
                    mMultiSelectPath.add(path);
                    photoPath.add(path);
                }
                setGridViewAdapter();
            }
        }

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case  REQUEST_MAP_LOCATE_CODE:
                    point=new LatLng(data.getDoubleExtra("latitude",0), data.getDoubleExtra("longitude",0));
                    locAddress=data.getStringExtra("address");
                    btnCarLocation.setText(TextUtils.isEmpty(locAddress)?"已标记":locAddress);
                    break;
                case REQUEST_BRAND_MODEL_CODE:
                    brandBean = (BrandBean) data.getSerializableExtra("brand");
                    brandModelBean = (BrandModelBean) data.getSerializableExtra("model");
                    btnBandModel.setText(brandBean.getName() + "  " + brandModelBean.getName());
                    break;
                case RESULT_LOAD_IMAGE:

                    try{
                        ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    Uri photoUri = data.getData();
                    String filename = FileUtil.getFileName(get(), photoUri);
                    String filepathtemp = FileUtil.getPath(get(), photoUri);
                    mMultiSelectPath.add(filepathtemp.toString());
                    photoPath.add(filepathtemp.toString());
                    setGridViewAdapter();
                    break;
                case RESULT_LOAD_VIDEO:
                    Uri videoUri = data.getData();
                    String vPath = FileUtil.getPath(get(), videoUri);
                    mMultiSelectPath.add(vPath.toString());
                    videoPath.add(vPath.toString());
                    setGridViewAdapter();
                    break;
            }
        }

    }


    private void setImageSetting(){
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


}

class PicassoImageLoader implements ImageLoader {

//    @Override
//    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//        Picasso.with(activity)//
//                .load(Uri.fromFile(new File(path)))//
//                .placeholder(R.mipmap.default_image)//
//                .error(R.mipmap.default_image)//
//                .resize(width, height)//
//                .centerInside()//
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//
//                .into(imageView);
//    }
//
//    @Override
//    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
//
//    }

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

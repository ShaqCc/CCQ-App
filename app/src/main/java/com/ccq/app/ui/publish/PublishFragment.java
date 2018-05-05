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
import com.ccq.app.utils.FileUtil;
import com.ccq.app.weidget.ListDialog;
import com.ccq.app.weidget.MyGridView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    private List<String> carAgeList;
    private Activity thisActivity;
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

                    Intent intent = new Intent(thisActivity, ImageGridActivity.class);
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
        thisActivity = getActivity();
        getBDlocation();
        carAgeList = new ArrayList<String>();
        for(int a=2017 ;a>=2000 ; a--){
            carAgeList.add( String.valueOf(a));
        }
        apiService = RetrofitClient.getInstance().getApiService();
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
                uploadFile();
                break;
        }
    }

    private void uploadFile(){

        if(!validate()){
            return ;
        }

        if(photoPath!=null && photoPath.size()>0){
            uploadImg();
            return;
        }
        if(videoPath!=null && videoPath.size() >0){
            uploadVideo();
        }


    }

    private void uploadImg(){
        MultipartBody.Part body = null;
        for (String path: photoPath) {
            File file = new File(path);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);
        }
        apiService.uploadImages(body).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call,
                                   Response<Object> response) {
                Map<String,Object> map = (Map<String, Object>) response.body();
                if(0.0== (Double)map.get("code")) {
                    imgids = String.valueOf(map.get("imageid"));
                    if(videoPath!=null && videoPath.size() >0){
                        uploadVideo();
                    }else{
                        submitData();
                    }
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });

    }

    private void  uploadVideo(){
        MultipartBody.Part body = null;
        for (String path: videoPath) {
            File file = new File(path);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);
        }
        apiService.uploadVideos(body).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call,
                                   Response<Object> response) {
                Map<String,String> map = (Map<String, String>) response.body();
                if("0.0".equals(map.get("code"))) {
                    videoids = map.get("imageid");
                    submitData();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

    private boolean validate(){
        if(brandModelBean==null){
            return false;
        }
        if(TextUtils.isEmpty(btnCarAge.getText())){
            return false;
        }
        if(TextUtils.isEmpty(etUserPhone.getText())){
            return false;
        }
        if(mMultiSelectPath!=null && mMultiSelectPath.size()>3){
            return false;
        }
        return true;
    }

    private void submitData(){
        CarInfo  car = new CarInfo();
        car.setAddress(btnCarLocation.getText().toString());
        car.setContent(etDescription.getText().toString());
        car.setImglist(imgids);
        car.setVideolist(videoids);
        car.setLatitude(String.valueOf(point.latitude));
        car.setLongitude(String.valueOf(point.longitude));
        car.setNumber(Integer.parseInt(brandModelBean.getId()));
        car.setPhone(etUserPhone.getText().toString());
        car.setPinpai(Integer.parseInt(brandBean.getId()));
        if(!TextUtils.isEmpty(etCarPrice.getText().toString())){
            car.setPrice(Float.parseFloat(etCarPrice.getText().toString()));
        }
//        car.setUserid();
        car.setYear(Integer.parseInt(btnCarAge.getText().toString()));

        apiService.addCarInfo(car).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Map<String,String> map = (Map<String, String>) response.body();
                if("0.0".equals(map.get("code"))) {
                    videoids = map.get("imageid");
                    submitData();
                }else{
                    String message = map.get("message");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

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
                mMultiSelectPath.remove(position);
                if(photoPath.size()>0){
                    photoPath.remove(position);
                }
                if(videoPath.size()>0){
                    videoPath.remove(position);
                }
                setGridViewAdapter();
            }
        });
    }


    public void showBrandSelectDialog(){

        Intent intent = new Intent();
        intent.setClass(thisActivity, BrandModelSelectActivity.class);
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
        intent.setClass(thisActivity, BaseMapActivity.class);
        if(point!=null){
            intent.putExtra("latlng",point);
        }
        startActivityForResult(intent,REQUEST_MAP_LOCATE_CODE);
    }

    private void showSelectDialog( ) {
        int selectPosition = carAgeList.indexOf(selectCarAge);
        final ListDialog mdialog = new ListDialog(thisActivity);
        alert = new AlertDialog.Builder(thisActivity).create();
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
                String path = images.get(0).path;
                mMultiSelectPath.add(path);
                photoPath.add(path);
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
                    String filename = FileUtil.getFileName(thisActivity, photoUri);
                    String filepathtemp = FileUtil.getPath(thisActivity, photoUri);
                    mMultiSelectPath.add(filepathtemp.toString());
                    photoPath.add(filepathtemp.toString());
                    setGridViewAdapter();
                    break;
                case RESULT_LOAD_VIDEO:
                    Uri videoUri = data.getData();
                    String vPath = FileUtil.getPath(thisActivity, videoUri);
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

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
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
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

    }

    @Override
    public void clearMemoryCache() {
        //这里是清除缓存的方法,根据需要自己实现
    }
}

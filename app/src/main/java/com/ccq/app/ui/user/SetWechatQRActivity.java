package com.ccq.app.ui.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;
import com.ccq.app.weidget.Toasty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jiguang.chat.utils.imagepicker.ImageGridActivity;
import jiguang.chat.utils.imagepicker.ImagePicker;
import jiguang.chat.utils.imagepicker.bean.ImageItem;
import jiguang.chat.utils.imagepicker.view.CropImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * creator by mly ,  2018/5/14
 */
public class SetWechatQRActivity extends BaseActivity {

    private String imagePath;
    private ProgressDialog dialog;
    private Handler handler = new Handler();
    private ApiService apiService;

    @OnClick(R.id.btn_open_vip)
    public void openVip() {
        startActivity(new Intent(SetWechatQRActivity.this, OpenVipActivity.class));
    }

    @OnClick(R.id.select_erweima)
    public void selectPic() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, 123);
    }

    @OnClick(R.id.btn_submit)
    public void upload() {

        apiService = RetrofitClient.getInstance().getApiService();

        if (TextUtils.isEmpty(imagePath)) {
            Toasty.error(this, "请选择二维码图片", Toast.LENGTH_LONG).show();
        } else {
            dialog = new ProgressDialog(this);
            dialog.setMessage("上传图片中...");
            dialog.show();

            uploadFile();
        }
    }

    private String imageUploadId;//上传服务器返回的id

    private void uploadImg(String path) throws IOException {

        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);

        Call<Object> result = apiService.uploadImages(body);
        Response<Object> response = result.execute();
        Map<String, Object> map = (Map<String, Object>) response.body();
        if (0.0 == (Double) map.get("code")) {
            imageUploadId = String.valueOf(map.get("imageid"));
        }

    }

    private void uploadFile() {
        io.reactivex.Observable.create(
                new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Boolean> emitter) throws Exception {
                        if (!emitter.isDisposed()) {
                            try {
                                //访问网络操作
                                if (imagePath != null) {
                                    uploadImg(imagePath);
                                }
//                                emitter.onNext(true);
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
//                        if (boo) {
//                            imgids = StringUtils.join(imgidList.toArray(new String[imgidList.size()]), ",");
//                            videoids = StringUtils.join(videoidList.toArray(new String[videoidList.size()]), ",");
//                        }
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

    private void submitData() {
        apiService.saveUserErWeiMa(AppCache.getUserBean().getUserid(), imageUploadId)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        Map<String, Object> map = (Map<String, Object>) response.body();
                        String message = (String) map.get("message");
                        if (0.0 == (Double) map.get("code")) {
                            message = "上传成功，赶快发布信息试试吧！";
                        }
                        dismissDialog();
                        Toasty.success(getCurrentActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        dismissDialog();
                        Toasty.error(getCurrentActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void dismissDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    @BindView(R.id.iv_user_erweima)
    ImageView ivErweima;
    @BindView(R.id.btn_open_vip)
    Button btOpenVip;


    @Override
    protected int inflateContentView() {
        return R.layout.activity_wechat_qr_image;
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarTitle("微信二维码");
        setBackIconVisible(true);
        if (AppCache.getUserBean().getVip() != 1) {
            showComfirmDialog();
            btOpenVip.setVisibility(View.VISIBLE);
        } else {
            btOpenVip.setVisibility(View.GONE);
        }
        setImageSetting();
    }

    private void setImageSetting() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (!images.isEmpty()) {
                imagePath = images.get(0).path;
                Glide.with(this).load(imagePath).into(ivErweima);
            }
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    private void showComfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetWechatQRActivity.this);
        builder.setTitle("操作提示");
        builder.setMessage("此功能需要'会员'或'认证经销商'可用");
        builder.setCancelable(true);
        builder.setPositiveButton("开通会员", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SetWechatQRActivity.this, OpenVipActivity.class));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();

    }
}

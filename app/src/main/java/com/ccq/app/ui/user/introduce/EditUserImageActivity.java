package com.ccq.app.ui.user.introduce;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ccq.app.R;
import com.ccq.app.base.BaseActivity;
import com.ccq.app.base.BasePresenter;
import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.UploadImageResponse;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.statusbar.StatusBar;
import com.ccq.app.utils.statusbar.StatusBarUtils;
import com.ccq.app.weidget.RecycleViewDivider;
import com.ccq.app.weidget.Toasty;
import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/22 14:20
 * 描述：编辑用户图片的界面
 * 版本：
 *
 **************************************************/

public class EditUserImageActivity extends BaseActivity {

    ApiService apiService = RetrofitClient.getInstance().getApiService();

    public static String KEY_IMAGE = "key_image";

    private UserImageBean imageBean;


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ProgressDialog dialog;
    private Handler handler = new Handler();
    private UserImageAdapter userImageAdapter;
    private int updateImagePos;
    private int REQUEST_ADD = 111;
    private int REQUEST_UPDATE = 222;
    private ArrayList<Media> select = new ArrayList<>();


    @OnClick({R.id.ic_finish, R.id.bt_add, R.id.bt_save})
    public void clidk(View view) {
        switch (view.getId()) {
            case R.id.ic_finish:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.bt_add:
                selectPic();
                break;
            case R.id.bt_save:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }


    /**
     * 选择图片
     */
    private void selectPic() {
        int size = 0;
        if (imageBean != null && !imageBean.getData().isEmpty()) {
            size = imageBean.getData().size();
        }
        size = 9 - size;
        if (size == 0) {
            Toasty.info(this, "最多上传9张").show();
        } else {
            Intent intent = new Intent(this, PickerActivity.class);
            intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
            intent.putExtra(PickerConfig.MAX_SELECT_COUNT, size);  //default 40 (Optional)

            startActivityForResult(intent, REQUEST_ADD);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD && resultCode == PickerConfig.RESULT_CODE) {
            select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (select != null && !select.isEmpty()) {
                //上传图片
                for (int i = 0; i < select.size(); i++) {
                    uplaodFile(select.remove(i).path);
                }
            }
        } else if (requestCode == REQUEST_UPDATE && resultCode == PickerConfig.RESULT_CODE) {
            ArrayList<Media> select = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (select != null && !select.isEmpty()) {
                //上传图片
                changeImage(select.get(0).path);
            }
        }
    }

    /**
     * 修改用户图
     *
     * @param path
     */
    private void changeImage(String path) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("上传图片中...");
        dialog.show();
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);
        apiService.uploadCommonImage(body).enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                if (response.body() != null && response.body().getCode() == 0) {
                    //修改
                    apiService.updateUserImage(String.valueOf(imageBean.getData().get(updateImagePos).getId()), String.valueOf(response.body().getImageid()),
                            AppCache.getUserBean().getUserid())
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {

                                    dismissDialog();
                                    Toasty.success(CcqApp.getAppContext(), "恭喜，修改成功！").show();
                                    updateImageList();
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    dismissDialog();
                                    Toasty.error(CcqApp.getAppContext(), t.getMessage()).show();
                                }
                            });
                } else {
                    dismissDialog();
                    Toasty.error(CcqApp.getAppContext(), "上传失败稍后再试").show();
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                Toasty.error(CcqApp.getAppContext(), t.getMessage()).show();
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


    private void uplaodFile(String path) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("上传图片中...");
        dialog.show();
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);
        apiService.uploadCommonImage(body).enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                if (response.body() != null && response.body().getCode() == 0) {
                    //保存
                    apiService.addUserImage(String.valueOf(response.body().getImageid()), AppCache.getUserBean().getUserid(), "0")
                            .enqueue(new Callback<Object>() {
                                @Override
                                public void onResponse(Call<Object> call, Response<Object> response) {
                                    if (select==null || select.isEmpty()){
                                        dismissDialog();
                                        Toasty.success(CcqApp.getAppContext(), "恭喜，保存成功！").show();
                                        updateImageList();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Object> call, Throwable t) {
                                    dismissDialog();
                                    Toasty.error(CcqApp.getAppContext(), t.getMessage()).show();
                                }
                            });
                } else {
                    dismissDialog();
                    Toasty.error(CcqApp.getAppContext(), "上传失败稍后再试").show();
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                Toasty.error(CcqApp.getAppContext(), t.getMessage()).show();
            }
        });
    }

    /**
     * 获取用户图片
     */
    void updateImageList() {
        apiService.getUserImageList(AppCache.getUserBean().getUserid())
                .enqueue(new Callback<UserImageBean>() {
                    @Override
                    public void onResponse(Call<UserImageBean> call, Response<UserImageBean> response) {
                        imageBean = response.body();
                        if (userImageAdapter == null) {
                            userImageAdapter = new UserImageAdapter(imageBean, EditUserImageActivity.this);
                            recyclerView.setAdapter(userImageAdapter);
                            userImageAdapter.setListener(new UserImageAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    //弹窗
                                    showOptionDialog(position);
                                }
                            });
                        } else {
                            userImageAdapter.refresh(imageBean);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserImageBean> call, Throwable t) {
                        Toasty.error(EditUserImageActivity.this, t.getMessage()).show();
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initView() {
        super.initView();
        select.clear();
        getToolBar().setVisibility(View.GONE);
        StatusBarUtils.setStatusBarColor(this, 0xffffffff);
        StatusBarUtils.statusBarLightMode(this);
        imageBean = (UserImageBean) getIntent().getSerializableExtra(KEY_IMAGE);

        if (imageBean != null && imageBean.getData() != null && imageBean.getData().size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            userImageAdapter = new UserImageAdapter(imageBean, this);
            userImageAdapter.setListener(new UserImageAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    //弹出
                    showOptionDialog(position);
                }
            });
            recyclerView.setAdapter(userImageAdapter);
            recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, R.drawable.shape_divide_line_1px));
        }
    }

    private void showOptionDialog(final int pos) {
        String[] items = {"上移", "下移", "修改", "删除"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        moveUp(pos);
                        break;
                    case 1:
                        moveDown(pos);
                        break;
                    case 2:
                        alter(pos);
                        break;
                    case 3:
                        delete(pos);
                        break;
                }
            }
        });
        listDialog.show();
    }

    /**
     * 上移
     *
     * @param pos
     */
    private void moveUp(int pos) {
        apiService.moveUp(String.valueOf(imageBean.getData().get(pos).getId()), AppCache.getUserBean().getUserid())
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        updateImageList();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    /**
     * 下移
     *
     * @param pos
     */
    private void moveDown(int pos) {
        apiService.moveDown(String.valueOf(imageBean.getData().get(pos).getId()), AppCache.getUserBean().getUserid())
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        updateImageList();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    /**
     * 修改
     *
     * @param pos
     */
    private void alter(int pos) {
        updateImagePos = pos;
        Intent intent = new Intent(this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1);  //default 40 (Optional)
        startActivityForResult(intent, REQUEST_UPDATE);
    }

    /**
     * 删除
     *
     * @param pos
     */
    private void delete(int pos) {
        UserImageBean.DataBean dataBean = imageBean.getData().get(pos);
        apiService.deleteUserImage(String.valueOf(dataBean.getId()), AppCache.getUserBean().getUserid())
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        updateImageList();
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    @Override
    protected int inflateContentView() {
        return R.layout.activity_edit_user_image_layout;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}

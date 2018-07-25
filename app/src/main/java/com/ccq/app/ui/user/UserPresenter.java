package com.ccq.app.ui.user;

import android.widget.Toast;

import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.SubscriberCountBean;
import com.ccq.app.entity.UserBanner;
import com.ccq.app.entity.UserBean;
import com.ccq.app.utils.AppCache;
import com.ccq.app.weidget.Toasty;
import com.google.gson.jpush.JsonObject;
import com.google.gson.jpush.JsonParser;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/15 16:04
 * 描述：
 * 版本：
 *
 **************************************************/

public class UserPresenter extends BasePresenter<IUserView> {
    public UserPresenter(IUserView view) {
        super(view);
    }


    public void getUserInfo(String userid) {

        apiService.getUser(userid).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (response.body() != null) {
                    mView.setUserView(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                Toasty.error(mView.getHostActivity(), t.getMessage()).show();
            }
        });
    }

    public void getUserBanner(String userid) {
        apiService.getUserBanner(userid)
                .enqueue(new Callback<UserBanner>() {
                    @Override
                    public void onResponse(Call<UserBanner> call, Response<UserBanner> response) {
                        if (response.body() != null) {
                            mView.setBanner(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserBanner> call, Throwable t) {
                        Toasty.error(mView.getHostActivity(), t.getMessage()).show();
                    }
                });
    }

    //是否订阅了查看的用户
    public void getSubscirberInfo(String userId) {
        if (AppCache.getUserBean() != null) {
            apiService.checkSubscribe(AppCache.getUserBean().getUserid(), userId).enqueue(new Callback<BaseBean>() {
                @Override
                public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                    //{"code":1,"message":"未订阅"}
                    // 0,已订阅，1未订阅
                    if (response.body() != null) {
                        mView.setSubscriber(response.body());
                    }
                }

                @Override
                public void onFailure(Call<BaseBean> call, Throwable t) {

                }
            });
        }
    }

    void getSubscribeCount(String userid) {
        apiService.getSubscribeCount(userid).enqueue(new Callback<SubscriberCountBean>() {
            @Override
            public void onResponse(Call<SubscriberCountBean> call, Response<SubscriberCountBean> response) {
                if (response.body() != null) {
                    mView.setSubCount(response.body());
                }
            }

            @Override
            public void onFailure(Call<SubscriberCountBean> call, Throwable t) {

            }
        });
    }

    public void changeBanner(String imagepath) {
        File file = new File(imagepath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("filename", file.getName(), requestFile);
        apiService.changeUserBanner(AppCache.getUserBean().getUserid(), body)
                .enqueue(new Callback<BaseBean>() {
                    @Override
                    public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                        if (response.body() != null) {
                            if (response.body().getCode() == 0) {
                                Toasty.success(mView.getHostActivity(), "修改成功！").show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseBean> call, Throwable t) {

                    }
                });

    }
}

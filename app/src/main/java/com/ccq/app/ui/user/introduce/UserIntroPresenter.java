package com.ccq.app.ui.user.introduce;

import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.UserBean;
import com.ccq.app.entity.UserLocationBean;
import com.ccq.app.utils.AppCache;
import com.ccq.app.weidget.Toasty;
import com.tencent.mapsdk.raster.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/22 13:55
 * 描述：
 * 版本：
 *
 **************************************************/

public class UserIntroPresenter extends BasePresenter<IUserIntroView> {
    public UserIntroPresenter(IUserIntroView view) {
        super(view);
    }

    public void getUserInfo(String userid) {

        apiService.getUser(userid).enqueue(new Callback<UserBean>() {
            @Override
            public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                if (response.body() != null) {
                    mView.setUserIntoduce(response.body());
                }
            }

            @Override
            public void onFailure(Call<UserBean> call, Throwable t) {
                Toasty.error(mView.getAty(), t.getMessage()).show();
            }
        });
    }

    public void queryLocation(final UserBean userBean) {
        apiService.getUserLocation(userBean.getUserid())
                .enqueue(new Callback<UserLocationBean>() {
                    @Override
                    public void onResponse(Call<UserLocationBean> call, Response<UserLocationBean> response) {
                        mView.setLocation(response.body());
                        //地址
                        apiService.getAddressByLgt(userBean.getMobile(), response.body().getLongitude(), response.body().getLatitude())
                                .enqueue(new Callback<BaseBean>() {
                                    @Override
                                    public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                                        BaseBean body = response.body();
                                        if (body != null) {
                                            mView.setAddress(body.getMessage());
                                        } else {
                                            mView.setAddress("获取失败");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseBean> call, Throwable t) {
                                        Toasty.warning(mView.getAty(), t.getMessage()).show();
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<UserLocationBean> call, Throwable t) {

                        Toasty.warning(mView.getAty(), t.getMessage()).show();
                    }
                });
    }

    void queryImageList(UserBean userBean) {
        if (AppCache.getUserBean() != null) {
            //图片
            apiService.getUserImageList(userBean.getUserid())
                    .enqueue(new Callback<UserImageBean>() {
                        @Override
                        public void onResponse(Call<UserImageBean> call, Response<UserImageBean> response) {
                            mView.setImageList(response.body());
                        }

                        @Override
                        public void onFailure(Call<UserImageBean> call, Throwable t) {
                            Toasty.error(mView.getAty(), t.getMessage()).show();
                        }
                    });
        }
    }

    public void updateLocation(LatLng point, String locAddress) {
        apiService.updateUserLocation(AppCache.getUserBean().getUserid(),
                String.valueOf(point.getLongitude()), String.valueOf(point.getLatitude()), locAddress)
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        queryLocation(AppCache.getUserBean());
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }
}

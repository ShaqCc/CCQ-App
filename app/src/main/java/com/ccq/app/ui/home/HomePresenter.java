package com.ccq.app.ui.home;

import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.Banner;
import com.ccq.app.entity.Car;
import com.ccq.app.http.ApiParams;
import com.ccq.app.utils.ToastUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/****************************************
 * 功能说明:主页控制器
 *
 * Author: Created by bayin on 2018/3/27.
 ****************************************/

public class HomePresenter extends BasePresenter<IHomeView> {
    public HomePresenter(IHomeView view) {
        super(view);
    }

    public void loadData(){
        mView.loading();

        apiService.getBanner().enqueue(new Callback<List<Banner>>() {
            @Override
            public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {
                if (response!=null && response.body()!=null)
                    mView.showBanner(response.body());
            }

            @Override
            public void onFailure(Call<List<Banner>> call, Throwable t) {
                mView.loadingError();
                ToastUtils.show(mView.get(),t.getMessage());
            }
        });


        apiService.getCarList(ApiParams.initCarMap())
                .enqueue(new Callback<List<Car>>() {
                    @Override
                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {

                    }

                    @Override
                    public void onFailure(Call<List<Car>> call, Throwable t) {

                    }
                });
    }
}

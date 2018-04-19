package com.ccq.app.ui.home;

import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.Car;
import com.ccq.app.entity.YearLimitBean;
import com.ccq.app.http.ApiParams;
import com.ccq.app.utils.ToastUtils;

import java.util.List;
import java.util.Map;

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

    public void loadData(boolean isinit) {
        if (!isinit)
            mView.loading();
        //获取banner
        apiService.getBanner().enqueue(new Callback<List<BannerBean>>() {
            @Override
            public void onResponse(Call<List<BannerBean>> call, Response<List<BannerBean>> response) {
                if (response != null && response.body() != null)
                    mView.showBanner(response.body());
            }

            @Override
            public void onFailure(Call<List<BannerBean>> call, Throwable t) {
                ToastUtils.show(mView.get(), t.getMessage());
            }
        });

        //获取车辆列表
        filterCar(ApiParams.getCarMap());

    }


    public void filterCar(Map<String, String> map) {
        apiService.getCarList(map)
                .enqueue(new Callback<List<Car>>() {
                    @Override
                    public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                        mView.loadingSuccess();
                        mView.showCarList(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Car>> call, Throwable t) {
                        mView.loadingError();
                        ToastUtils.show(mView.get(), t.getMessage());
                    }
                });
    }

    public void chooseBrand(){
        apiService.getBrandList().enqueue(new Callback<List<BrandBean>>() {
            @Override
            public void onResponse(Call<List<BrandBean>> call, Response<List<BrandBean>> response) {
                if (response!=null && response.body()!=null){
                    mView.showBrandList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BrandBean>> call, Throwable t) {

            }
        });
    }

    public void chooseType() {
        apiService.getCarTypeList().enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response!=null && response.body()!=null){
                    mView.showTypeList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {

            }
        });
    }

    public void chooseYear(){
        apiService.getCarYearList().enqueue(new Callback<List<YearLimitBean>>() {
            @Override
            public void onResponse(Call<List<YearLimitBean>> call, Response<List<YearLimitBean>> response) {
                if (response!=null && response.body()!=null){
                    mView.showYearList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<YearLimitBean>> call, Throwable t) {

            }
        });
    }

}

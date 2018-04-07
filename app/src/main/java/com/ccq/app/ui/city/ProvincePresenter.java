package com.ccq.app.ui.city;

import android.text.TextUtils;

import com.ccq.app.base.BasePresenter;
import com.ccq.app.entity.Province;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class ProvincePresenter extends BasePresenter<IProvinceView> {
    public ProvincePresenter(IProvinceView view) {
        super(view);
    }

    void getProvince() {
        apiService.getProvinceList().enqueue(new Callback<List<Province>>() {
            @Override
            public void onResponse(Call<List<Province>> call, Response<List<Province>> response) {
                mView.setProvinceData(response.body());
            }

            @Override
            public void onFailure(Call<List<Province>> call, Throwable t) {

            }
        });

    }


    void getCitys(String provinceId){
        apiService.getCityList(provinceId).enqueue(new Callback<List<Province.CityBean>>() {
            @Override
            public void onResponse(Call<List<Province.CityBean>> call, Response<List<Province.CityBean>> response) {
                mView.setCityData(response.body());
            }

            @Override
            public void onFailure(Call<List<Province.CityBean>> call, Throwable t) {

            }
        });
    }
}

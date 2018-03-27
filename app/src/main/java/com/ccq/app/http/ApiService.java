package com.ccq.app.http;

import com.ccq.app.entity.Banner;
import com.ccq.app.entity.Car;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 0:45
 * 描述：app的Url
 * 版本：1.0
 *
 **************************************************/

public interface ApiService {

    /**
     * 获取首页banner
     *
     * @return
     */
    @GET("/car/banner")
    retrofit2.Call<List<Banner>> getBanner();


    @POST("/car/list")
    Call<List<Car>> getCarList(@QueryMap Map<String, String> carParams);
}

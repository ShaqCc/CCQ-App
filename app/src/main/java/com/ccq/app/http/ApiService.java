package com.ccq.app.http;

import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.Car;
import com.ccq.app.entity.Province;
import com.ccq.app.entity.WxLoginResultBean;
import com.ccq.app.entity.WxUserInfo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

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
    retrofit2.Call<List<BannerBean>> getBanner();


    /**
     * 获取车辆列表
     *
     * @param carParams
     * @return
     */
    @POST("/car/list")
    Call<List<Car>> getCarList(@QueryMap Map<String, String> carParams);


    /**
     * @return 获取全国省份
     */
    @GET("/addr/province")
    Call<List<Province>> getProvinceList();


    @GET("/addr/city")
    Call<List<Province.CityBean>> getCityList(@Query("pid") String pid);


    @GET("/user/isbind")
    Call<Object> getUserInfo(@Query("unionid") String unionid);


    @GET("/mobile/code")//发送验证码
    Call<Object> sendMobileCode(@Query("phone") String phone);

    @GET("/mobile/check")//验证验证码
    Call<Object> checkMobilCode(@Query("phone") String phone, @Query("code") String code);

    @GET("/car/brand")
    Call<List<BrandBean>> getBrandList();


//    ------------------------------------微信登陆相关接口-----------------------------------------------

    /**
     * 获取微信登录的token
     *
     * @param url
     * @return
     */
    @GET
    Call<WxLoginResultBean> getAccessToken(@Url String url);

    @GET
//https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN
    Call<WxUserInfo> getWxUserInfo(@Url String url);
}

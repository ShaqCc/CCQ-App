package com.ccq.app.http;

import android.database.Observable;

import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.BindResultBaen;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.BrandModelBean;
import com.ccq.app.entity.Car;
import com.ccq.app.entity.CarInfo;
import com.ccq.app.entity.Province;
import com.ccq.app.entity.SubscribeUser;
import com.ccq.app.entity.UserBean;
import com.ccq.app.entity.WxLoginResultBean;
import com.ccq.app.entity.WxUserInfo;
import com.ccq.app.entity.YearLimitBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
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
    Call<UserBean> loginWithWeixin(@Query("unionid") String unionid);

    @GET("/user/bind")
    Call<BindResultBaen> bindUserInfo(@Query("unionid") String unionid, @Query("mobile")String mobile,
                                      @Query("code")String code, @Query("headimg")String headimg, @Query("longitude")String longtitude,
                                      @Query("latitude")String latitude, @Query("nickname")String nickName);

    @GET("/user/info")
    Call<UserBean> getUser(@Query("userid")String userid);


    @GET("/mobile/code")//发送验证码
    Call<BaseBean> sendMobileCode(@Query("phone") String phone);

    @GET("/mobile/check")//验证验证码
    Call<Object> checkMobilCode(@Query("phone") String phone, @Query("code") String code);

    @GET("/car/brand")
    Call<List<BrandBean>> getBrandList();

    @GET("/car/number")
    Call<List<Object>> getCarTypeList();

    @GET("/car/year")
    Call<List<YearLimitBean>> getCarYearList();


    @GET("/car/number")
    Call<List<BrandModelBean>> getBrandModelList(@Query("brandid") String brandid);


    /**
     * 车辆图片上传
     * @param file
     * @return
     */
    @Multipart
    @POST("/upload")
    Call<Object> uploadImages(@Part MultipartBody.Part file);


    @Multipart
    @POST("/uploadvideo")
    Call<Object> uploadVideos(@Part MultipartBody.Part file);

    @POST("/car/addcar")
    Call<Object> addCarInfo(@QueryMap Map<String, Object> carParams);

    /**
     * 修改车辆信息
     * @param user
     * @return
     */
    @POST("/car/edit")
    Call<Object> editCarInfo(@Body CarInfo user);

    @GET("/car/nianfen")
    Call<List<Object>> getCarNianFenList();

    /**
     * 获取车辆列表
     *
     * @param carParams
     * @return
     */
    @POST("/user/carlist")
    Call<List<Car>> getUserCarList(@QueryMap Map<String, String> carParams);

    @POST("/user/subscribe/list")
    Call<SubscribeUser> getUserSubscribe(@QueryMap Map<String, String> carParams);

    @POST("/user/subscribe/fans")
    Call<SubscribeUser> getUserFans(@QueryMap Map<String, String> carParams);

    @POST("/user/subscribe/remind")
    Call<Object> setUserSubRemind(@QueryMap Map<String, String> carParams);

    @POST("/user/subscribe/add")
    Call<Object> setUserSubAdd(@QueryMap Map<String, String> carParams);

    @POST("/user/subscribe/remove")
    Call<Object> setUserSubRemove(@QueryMap Map<String, String> carParams);



    @POST("/user/subscribe/count")
    Call<Object> getSubscribeCount(@Query("userid") String userid);

    /**
     * 发布信息前检测
     * @param userid
     * @return
     */
    @POST("/user/sendcheck")
    Call<Object> sendCheck(@Query("userid") String userid);

    /**
     * 修改车辆状态
     * @param userid
     * @return
     */
    @POST("/car/status")
    Call<Object> setCarStatus(@Query("userid") String userid,@Query("carid") String carid);

    /**
     * 删除车辆信息
     * @param userid
     * @return
     */
    @POST("/car/remove")
    Call<Object> deleteCar(@Query("userid") String userid,@Query("carid") String carid);

     /**
     * 刷新车辆信息
     * @param userid
     * @return
     */
    @POST("/car/ref")
    Call<Object> refreshCarInfo(@Query("userid") String userid,@Query("carid") String carid);


    /**
     * 修改个人简介
     * @param userid
     * @return
     */
    @POST("/user/info/setcontent")
    Call<Object> editUserIntro(@Query("userid") String userid,@Query("content") String content);


    /**
     * 生成分享二维码
     * @param userid
     * @return
     */
    @POST("/user/qrcode")
    Call<Object> getQrcode(@Query("userid") String userid);

    @GET
    Call<ResponseBody> downloadPic(@Url String url);

    //极光上传用户图片
    @POST
    Call<Object> jIMUpload(@Url String url, @Field("type")String type,@Part MultipartBody.Part file);


    @POST("/car/addr")
    Call<Object> getCarAddress(@Query("carid") String carid);


    @POST("/car/info")
    Call<Object> getCarInfo(@Query("userid") String userid,@Query("carid") String carid);


    @POST("/vip/money")
    Call<Object> getVipMoney(@Query("month") String month);


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

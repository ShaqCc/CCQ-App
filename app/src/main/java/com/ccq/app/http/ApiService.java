package com.ccq.app.http;

import com.ccq.app.base.BaseBean;
import com.ccq.app.entity.Banner;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.GET;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 0:45
 * 描述：app的Url
 * 版本：1.0
 *
 **************************************************/

public interface ApiService {
    @GET("/car/banner")
    retrofit2.Call<BaseBean<List<Banner>>> getBanner();
}

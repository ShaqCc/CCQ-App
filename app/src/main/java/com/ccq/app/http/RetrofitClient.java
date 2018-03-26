package com.ccq.app.http;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 0:26
 * 描述：网络请求客户端
 * 版本：1.0
 *
 **************************************************/

public class RetrofitClient {
    private RetrofitClient() {
    }

    private static RetrofitClient mInstance = new RetrofitClient();
    private Retrofit retrofit;
    private ApiService apiService;

    public static RetrofitClient getInstance() {
        return mInstance;
    }

    public ApiService getApiService() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor())
                    .addInterceptor(new HeaderInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiParams.BASEURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        apiService = retrofit.create(ApiService.class);
        return apiService;
    }
}

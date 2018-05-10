package com.ccq.app.base;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.ccq.app.entity.UserBean;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.service.LocationService;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.JmessageUtils;
import com.ccq.app.utils.SharedPreferencesUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jpush.im.android.api.JMessageClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/****************************************
 * 功能说明:Application
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class CcqApp extends MultiDexApplication {
    private static IWXAPI iwxapi;
    public LocationService locationService;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        //微信
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        iwxapi.registerApp(Constants.WX_APP_ID);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());
        //初始化极光IM
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);

        String userid = (String) SharedPreferencesUtils.getParam(this, Constants.USER_ID, "");
        if (!TextUtils.isEmpty(userid)) {
            RetrofitClient.getInstance().getApiService().getUser(userid)
                    .enqueue(new Callback<UserBean>() {
                        @Override
                        public void onResponse(Call<UserBean> call, Response<UserBean> response) {
                            if (response.isSuccessful() && response.body()!=null) {
                                AppCache.setUserBean(response.body());
                                //登录极光
                                JmessageUtils.registerIM(response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<UserBean> call, Throwable t) {

                        }
                    });
        }

    }

    public static Context getAppContext(){
        return sContext;
    }

    public static IWXAPI getWxApi() {
        return iwxapi;
    }

}

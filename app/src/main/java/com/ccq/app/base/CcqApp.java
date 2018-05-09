package com.ccq.app.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.ccq.app.entity.UserBean;
import com.ccq.app.service.LocationService;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.JmessageUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jpush.im.android.api.JMessageClient;


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
//        初始化屏幕适配设置
//        InflaterAuto.init(new InflaterAuto.Builder()
//                .width(720)
//                .height(1280)
//                .baseOnDirection(AutoBaseOn.Both)
//                .inflaterConvert(new InfAutoInflaterConvert())
//                .build()
//        );

        //微信
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        iwxapi.registerApp(Constants.WX_APP_ID);
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
//        SDKInitializer.initialize(getApplicationContext());
        //初始化极光IM
        JMessageClient.setDebugMode(true);
        JMessageClient.init(getApplicationContext(), true);

        UserBean userBean = AppCache.getUserBean();
        if (userBean !=null){
            JmessageUtils.registerIM(userBean);
        }

    }

    public static Context getAppContext(){
        return sContext;
    }

    public static IWXAPI getWxApi() {
        return iwxapi;
    }

}

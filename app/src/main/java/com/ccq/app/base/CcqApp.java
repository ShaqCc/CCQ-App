package com.ccq.app.base;

import android.app.Application;
import android.content.Context;

import com.ccq.app.adjust.InfAutoInflaterConvert;
import com.ccq.app.service.LocationService;
import com.ccq.app.utils.Constants;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yan.inflaterauto.AutoBaseOn;
import com.yan.inflaterauto.InflaterAuto;
import com.baidu.mapapi.SDKInitializer;


/****************************************
 * 功能说明:Application
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class CcqApp extends Application {
    private static IWXAPI iwxapi;
    public LocationService locationService;

    @Override
    public void onCreate() {
        super.onCreate();
//        初始化屏幕适配设置
        InflaterAuto.init(new InflaterAuto.Builder()
                .width(720)
                .height(1280)
                .baseOnDirection(AutoBaseOn.Both)
                .inflaterConvert(new InfAutoInflaterConvert())
                .build()
        );

        //微信
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        iwxapi.registerApp(Constants.WX_APP_ID);


        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());


    }

    public static IWXAPI getWxApi() {
        return iwxapi;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(InflaterAuto.wrap(base));
    }
}

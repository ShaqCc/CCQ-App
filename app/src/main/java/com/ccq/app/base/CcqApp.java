package com.ccq.app.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.ccq.app.entity.UserBean;
import com.ccq.app.http.RetrofitClient;
import com.ccq.app.utils.AppCache;
import com.ccq.app.utils.Constants;
import com.ccq.app.utils.CrashHandler;
import com.ccq.app.utils.JmessageUtils;
import com.ccq.app.utils.SharedPreferencesUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.ButterKnife;
import cn.jpush.im.android.api.JMessageClient;
import jiguang.chat.application.JGApplication;
import jiguang.chat.entity.NotificationClickEventReceiver;
import jiguang.chat.pickerimage.utils.ScreenUtil;
import jiguang.chat.pickerimage.utils.StorageUtil;
import jiguang.chat.utils.SharePreferenceManager;
import jiguang.chat.utils.imagepicker.GlideImageLoader;
import jiguang.chat.utils.imagepicker.ImagePicker;
import jiguang.chat.utils.imagepicker.view.CropImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static jiguang.chat.activity.LoginActivity.swapEnvironment;


/****************************************
 * 功能说明:Application
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class CcqApp extends com.activeandroid.app.Application {
    private static IWXAPI iwxapi;
    private static Context sContext;
    public static String jmappkey = "eb9dced406535d33bd0f637e";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        //微信
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        iwxapi.registerApp(Constants.WX_APP_ID);
        //初始化极光IM
        initJmIm();
        //测试环境
        swapEnvironment(sContext, true);
        //注入框架
        ButterKnife.setDebug(true);
        //崩溃日志抓取
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

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

    private void initJmIm() {
        JGApplication.initContent(sContext);
        StorageUtil.init(sContext, null);
        ScreenUtil.GetInfo(sContext);

        Fresco.initialize(getApplicationContext());

        JMessageClient.init(getApplicationContext(), true);
        JMessageClient.setDebugMode(true);
        SharePreferenceManager.init(getApplicationContext(), JGApplication.JCHAT_CONFIGS);
        //设置Notification的模式
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND | JMessageClient.FLAG_NOTIFY_WITH_LED | JMessageClient.FLAG_NOTIFY_WITH_VIBRATE);
        //注册Notification点击的接收器
        new NotificationClickEventReceiver(getApplicationContext());
        initImagePicker();
    }

    public static Context getAppContext(){
        return sContext;
    }

    public static IWXAPI getWxApi() {
        return iwxapi;
    }


    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(JGApplication.maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }



    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

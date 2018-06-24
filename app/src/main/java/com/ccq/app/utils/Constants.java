package com.ccq.app.utils;

import com.ccq.app.http.ApiParams;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/7 21:09
 * 描述：base常量
 * 版本：1.0
 *
 **************************************************/

public class Constants {
    public static String WX_APP_ID = "wx1a2af6a07fd055a2";
    public static String WX_MCH_ID = "1505799371"; //微信商户号
    public static String WX_APPSECRET = "6ab15d631852825bc373143c5a2283e7";
    public static String WX_STATE = "ccq_wx_login";

    public static String KEY_UNIONID = "key_unionid";
    public static String KEY_OPEN_ID = "key_openid";

    public static String JIGUANG_APP_KEY = "eb9dced406535d33bd0f637e";
    public static String JIGUANG_SECRET = "4678823d3cb258fd2539579a";

    public static String userAvatarFileName = "user_avatar.png";//用户头像图片名称

    public static String USER_ID = "user_id";

    public static String IS_REGISTER_JIM = "is_register_jim";//是否注册过极光im
    public static String IS_LOGIN_JIM = "is_login_jim";//是否登录了极光im
    public static String IS_UPDATE_USER_INFO = "IS_UPDATE_USER_INFO";//是否登更新了用户信息


    //event bus
    public static final int WX_LOGIN_SUCCESS = 20001;//微信登录成功事件码

    public static final int REFRESH_EVENT = 20002;//页面刷新事件


    /**
     * 微信回调地址
     */
    public static final String WX_PAY_CALLBACK_URL = ApiParams.BASEURL+"/weixin/notify";

    public static int PHOTO_WIDTH = 82;


    public final static int PAY_RESULT_SUCCESS = 66;//支付成功
    public final static int PAY_RESULT_CANCEL = -123;//支付取消
    public final static int PAY_RESULT_FAILURE = -444;//支付失败

}

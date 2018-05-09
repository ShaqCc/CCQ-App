package com.ccq.app.utils;

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
    public static String WX_APPSECRET = "9ab4beba012874e989a35cf2d79b54fa";
    public static String WX_STATE = "ccq_wx_login";

    public static String KEY_UNIONID = "key_unionid";

    public static String JIGUANG_APP_KEY = "eb9dced406535d33bd0f637e";
    public static String JIGUANG_SECRET = "4678823d3cb258fd2539579a";

    public static String userAvatarFileName = "user_avatar.png";//用户头像图片名称

    public static String USER_ID = "user_id";

    public static String IS_REGISTER_JIM = "is_register_jim";//是否注册过极光im
    public static String IS_LOGIN_JIM = "is_login_jim";//是否登录了极光im


    //event bus
    public static final int WX_LOGIN_SUCCESS = 20001;//微信登录成功事件码

    public static final int REFRESH_EVENT = 20002;//页面刷新事件



}

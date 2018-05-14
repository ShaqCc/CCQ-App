package com.ccq.app.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.UserBean;

import java.io.File;
import java.security.MessageDigest;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.api.options.RegisterOptionalUserInfo;
import cn.jpush.im.api.BasicCallback;
import jiguang.chat.activity.MainActivity;
import jiguang.chat.database.UserEntry;
import jiguang.chat.utils.SharePreferenceManager;
import jiguang.chat.utils.ToastUtil;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/5/5 20:11
 * 描述：极光IM相关的工具类，登录，注册，获取用户信息
 * 版本：
 *
 **************************************************/

public class JmessageUtils {

    private final static String TAG = "JmessageUtils";

    /**
     * @return 获取极光登录的userid，格式：chanche@userid;
     */
    public static String getJimUserName() {
        if (AppCache.getUserBean() != null) {
            return "chanche@" + AppCache.getUserBean().getUserid();
        }
        return "";
    }

    public static String getUserName(String userid){
        return "chanche@"+userid;
    }

    /**
     * @return 极光登录的密码
     */
    public static String getJimPassword() {
        if (AppCache.getUserBean() != null) {
            return MD5(AppCache.getUserBean().getUserid());
        }
        return "";
    }

    static String getPassword(String userid){
        return MD5(userid);
    }

    public static void registerIM(final UserBean userBean) {
        RegisterOptionalUserInfo info = new RegisterOptionalUserInfo();
        //设置昵称
        info.setNickname(AppCache.getUserBean().getNickname());
        //设置性别
        info.setGender(UserInfo.Gender.male);
        //设置头像
        info.setAvatar(Utils.getAvatarPath());
        String userName = getUserName(userBean.getUserid());
        String pwd = MD5(userBean.getUserid());
        registerIM(CcqApp.getAppContext(), userName,pwd , info);
    }

    private static void registerIM(final Context context, final String userName,
                                   final String password, RegisterOptionalUserInfo registerOptionalUserInfo) {
        JMessageClient.register(userName, password, registerOptionalUserInfo,new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String registerDesc) {
                //注册成功               已经注册过
                if (responseCode == 0 || responseCode==898001) {
                    Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtils.setParam(context,Constants.IS_REGISTER_JIM,true);
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                    loginIM(context, userName, password);
                } else {
                    Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtils.setParam(context,Constants.IS_REGISTER_JIM,false);
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                }
            }
        });
    }

    public static void loginIM(final Context context, String userName, final String password) {
//        JMessageClient.login(getUserName(userName), MD5(password), new BasicCallback() {
//            @Override
//            public void gotResult(int responseCode, String LoginDesc) {
//                if (responseCode == 0) {
//                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
//                    SharedPreferencesUtils.setParam(context,Constants.IS_LOGIN_JIM,true);
//                    Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
//                } else {
//                    Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
//                    SharedPreferencesUtils.setParam(context,Constants.IS_LOGIN_JIM,false);
//                    Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
//                }
//            }
//        });

        JMessageClient.login(userName, password, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String responseMessage) {
                if (responseCode == 0) {
                    SharePreferenceManager.setCachedPsw(password);
                    UserInfo myInfo = JMessageClient.getMyInfo();
                    File avatarFile = myInfo.getAvatarFile();
                    //登陆成功,如果用户有头像就把头像存起来,没有就设置null
                    if (avatarFile != null) {
                        SharePreferenceManager.setCachedAvatarPath(avatarFile.getAbsolutePath());
                    } else {
                        SharePreferenceManager.setCachedAvatarPath(null);
                    }
                    String username = myInfo.getUserName();
                    String appKey = myInfo.getAppKey();
                    UserEntry user = UserEntry.getUser(username, appKey);
                    if (null == user) {
                        user = new UserEntry(username, appKey);
                        user.save();
                    }
                    SharedPreferencesUtils.setParam(context,Constants.IS_LOGIN_JIM,true);
                    ToastUtil.shortToast(CcqApp.getAppContext(), "登陆成功");
                } else {
                    SharedPreferencesUtils.setParam(context,Constants.IS_LOGIN_JIM,false);
                    ToastUtil.shortToast(CcqApp.getAppContext(), "登陆失败" + responseMessage);
                }
            }
        });
    }

    private static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }
}

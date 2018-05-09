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

    static String getUserName(String userid){
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

    public static void registerIM(UserBean userBean) {
        RegisterOptionalUserInfo info = new RegisterOptionalUserInfo();
        info.setNickname(AppCache.getUserBean().getNickname());
        info.setGender(UserInfo.Gender.male);
        registerIM(CcqApp.getAppContext(), userBean.getUserid(), userBean.getUserid(), info);
    }

    private static void registerIM(final Context context, final String userName,
                                   final String password, RegisterOptionalUserInfo registerOptionalUserInfo) {
        JMessageClient.register(getUserName(userName), MD5(password), new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String registerDesc) {
                if (responseCode == 0) {
                    Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtils.setParam(context,Constants.IS_REGISTER_JIM,true);
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                    loginIM(context, userName, password);
                    //更新头像
                    upateHeadImg();
                } else {
                    Toast.makeText(context, "注册失败", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtils.setParam(context,Constants.IS_REGISTER_JIM,false);
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                }
            }
        });
    }


    private static void upateHeadImg() {
        JMessageClient.updateUserAvatar(new File(Utils.getAvatarPath()), new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                ToastUtils.show(CcqApp.getAppContext(), "更新用户头像结果：" + s);
            }
        });
    }


    public static void loginIM(final Context context, String userName, String password) {
        JMessageClient.login(getUserName(userName), MD5(password), new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String LoginDesc) {
                if (responseCode == 0) {
                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtils.setParam(context,Constants.IS_LOGIN_JIM,true);
                    Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
                } else {
                    Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                    SharedPreferencesUtils.setParam(context,Constants.IS_LOGIN_JIM,false);
                    Log.i(TAG, "JMessageClient.login" + ", responseCode = " + responseCode + " ; LoginDesc = " + LoginDesc);
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

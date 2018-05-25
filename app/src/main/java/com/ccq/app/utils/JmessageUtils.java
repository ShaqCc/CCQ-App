package com.ccq.app.utils;

import android.content.Context;
import android.util.Log;

import com.ccq.app.base.CcqApp;
import com.ccq.app.entity.UserBean;
import com.ccq.app.http.ApiService;
import com.ccq.app.http.RetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import jiguang.chat.database.UserEntry;
import jiguang.chat.utils.SharePreferenceManager;
import jiguang.chat.utils.ToastUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    public static String getUserName(String userid) {
        return "chanche@" + userid;
    }

    /**
     * 注册im
     * @param userBean
     */
    public static void registerIM(final UserBean userBean) {
        downImageJi(userBean);
    }

    /**
     * 分三步
     * 1、下载微信用户头像
     * 2、注册极光im
     * 3、登陆极光im
     * @param userBean
     */
    private static void downImageJi(final UserBean userBean) {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        Call<ResponseBody> responseBodyCall = apiService.downloadPic(userBean.getHeadimgurl());
        if (responseBodyCall.isExecuted()) return;
        responseBodyCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), Utils.getAvatarPath());
                    if (writtenToDisk){
                        regist(userBean);
                    }
                } else {
                    ToastUtils.show(CcqApp.getAppContext(), response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtils.show(CcqApp.getAppContext(), t.getMessage());
            }
        });
    }

    /**
     * 注册im
     * @param userBean
     */
    private static void regist(UserBean userBean) {
        final String userName = getUserName(userBean.getUserid());
        JMessageClient.register(userName, userName, new BasicCallback() {
            @Override
            public void gotResult(int responseCode, String registerDesc) {
                //注册成功
                if (responseCode == 0 || responseCode == 898001) {
                    SharedPreferencesUtils.setParam(CcqApp.getAppContext(), Constants.IS_REGISTER_JIM, true);
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                    loginIM(CcqApp.getAppContext(), userName, userName);
                } else {
                    SharedPreferencesUtils.setParam(CcqApp.getAppContext(), Constants.IS_REGISTER_JIM, false);
                    Log.i(TAG, "JMessageClient.register " + ", responseCode = " + responseCode + " ; registerDesc = " + registerDesc);
                }
            }
        });
    }

    /**
     *  登陆im
     * @param context
     * @param userName
     * @param password
     */
    public static void loginIM(final Context context, String userName, final String password) {
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
                        JmessageUtils.UpdateUserInfo();
                    }
                    String username = myInfo.getUserName();
                    String appKey = myInfo.getAppKey();
                    UserEntry user = UserEntry.getUser(username, appKey);
                    if (null == user) {
                        user = new UserEntry(username, appKey);
                        user.save();
                    }
                    SharedPreferencesUtils.setParam(context, Constants.IS_LOGIN_JIM, true);
                    ToastUtil.shortToast(CcqApp.getAppContext(), "登陆成功");
                } else {
                    SharedPreferencesUtils.setParam(context, Constants.IS_LOGIN_JIM, false);
                    ToastUtil.shortToast(CcqApp.getAppContext(), "登陆失败" + responseMessage);
                }
            }
        });
    }

    /**
     * 更新用户信息
     */
    public static void UpdateUserInfo() {
        UserInfo myInfo = JMessageClient.getMyInfo();
        if(myInfo.getAvatar()==null){
            myInfo.setNickname(AppCache.getUserBean().getNickname());
            JMessageClient.updateMyInfo(UserInfo.Field.nickname,myInfo,null);
            JMessageClient.updateUserAvatar(new File(Utils.getAvatarPath()), new BasicCallback() {
                @Override
                public void gotResult(int i, String s) {
                    UserInfo myInfo = JMessageClient.getMyInfo();
                    File avatarFile = myInfo.getAvatarFile();
                    //登陆成功,如果用户有头像就把头像存起来,没有就设置null
                    if (avatarFile != null) {
                        SharePreferenceManager.setCachedAvatarPath(avatarFile.getAbsolutePath());
                    }
                }
            });
        }
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


    private static boolean writeResponseBodyToDisk(ResponseBody body, String filePath) {
        try {
            File futureStudioIconFile = new File(filePath);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    Log.d("download", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}

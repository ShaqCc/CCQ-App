package com.ccq.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.ccq.app.base.CcqApp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.io.File;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class Utils {
    /**
     * 打电话
     * @param activity
     * @param phone /storage/sdcard0/user_avatar.png
     */
    public static void call(Context activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 微信登录
     * @param activity
     */
    public static void login(Activity activity){
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = Constants.WX_STATE;
        CcqApp.getWxApi().sendReq(req);
    }

    public static String getAvatarPath(){
        return Environment.getExternalStorageDirectory() + File.separator + "user_avatar.png";
    }
}

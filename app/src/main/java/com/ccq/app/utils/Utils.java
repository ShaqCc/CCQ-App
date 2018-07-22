package com.ccq.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import com.ccq.app.base.CcqApp;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import jiguang.chat.model.Constant;
import jiguang.chat.pickerimage.utils.MD5;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class Utils {
    /**
     * 打电话
     *
     * @param activity
     * @param phone    /storage/sdcard0/user_avatar.png
     */
    public static void call(Context activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /**
     * 微信登录
     *
     * @param activity
     */
    public static void login(Activity activity) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = Constants.WX_STATE;
        CcqApp.getWxApi().sendReq(req);
    }

    public static String getAvatarPath() {
        return Environment.getExternalStorageDirectory() + File.separator + "user_avatar.png";
    }

    /**
     * 微信支付签名算法sign
     *
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String createSign(JSONObject json) {
        List<String> signList = new ArrayList<String>();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            signList.add(key);
        }

        Collections.sort(signList);
        String sign = "";
        for (int i = 0; i < signList.size(); i++) {
            String key = signList.get(i);
            String value = json.optString(key);
            sign = sign + key + "=" + value + "&";
        }

        sign = sign + "key=" + Constants.WX_APP_ID;

        sign = MD5Util.MD5Encode(sign, "UTF-8").toUpperCase();

        return sign;
    }

    /**
     * 微信随机字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    public static void fitsSystemWindows(boolean isTranslucentStatus, View view) {
        if (isTranslucentStatus) {
            view.getLayoutParams().height = calcStatusBarHeight(view.getContext());
        }
    }

    public static int calcStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    //请求xml组装
//    public static String getRequestXml(SortedMap<String, Object> parameters) {
//
//        StringBuffer sb = new StringBuffer();
//        sb.append("<xml>");
//        Set es = parameters.entrySet();
//        Iterator it = es.iterator();
//        while (it.hasNext()) {
//            Map.Entry entry = (Map.Entry) it.next();
//            String key = (String) entry.getKey();
//            String value = (String) entry.getValue();
//            if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)) {
//                sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
//            } else {
//                sb.append("<" + key + ">" + value + "</" + key + ">");
//            }
//        }
//        sb.append("</xml>");
//        return sb.toString();
//    }


    public static String getAddress(TencentLocation loc) {
        if (loc == null) {
            return "";
        } else {
            if (loc.getPoiList() != null && !loc.getPoiList().isEmpty()) {
                String address = loc.getPoiList().get(0).getAddress();
                if (!TextUtils.isEmpty(address))
                    return address;
            }
            return loc.getCity() + loc.getStreet() + loc.getStreetNo();
        }
    }
}

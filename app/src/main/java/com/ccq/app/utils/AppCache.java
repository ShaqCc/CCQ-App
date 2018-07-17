package com.ccq.app.utils;

import com.ccq.app.entity.UserBean;
import com.tencent.map.geolocation.TencentLocation;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/22 21:27
 * 描述：app内的缓存
 * 版本：
 *
 **************************************************/

public class AppCache {
    private static UserBean sUserBean;

    public static void setUserBean(UserBean userBean){
        sUserBean = userBean;
    }

    public static UserBean getUserBean(){
        return sUserBean;
    }

    public static TencentLocation mLocation;
}

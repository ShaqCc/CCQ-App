package com.ccq.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public class Utils {
    public static void call(Context activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}

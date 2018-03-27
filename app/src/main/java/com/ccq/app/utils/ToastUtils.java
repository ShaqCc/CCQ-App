package com.ccq.app.utils;

import android.content.Context;
import android.widget.Toast;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/27.
 ****************************************/

public class ToastUtils {
    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}

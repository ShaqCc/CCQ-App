package com.ccq.app.utils.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by bayin on 2018/1/13.
 */

public class StatusBar {

    public static void setStatusBar(Activity activity) {
        Window window = activity.getWindow();
        View decorView = window.getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//大于4.4的
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0以上的，设置暗色模式
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {//4.4~5.1的设置沉浸式
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
        //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
        StatusBarUtils.MIUISetStatusBarLightMode(activity, true);
        StatusBarUtils.FlymeSetStatusBarLightMode(activity, true);
    }
}

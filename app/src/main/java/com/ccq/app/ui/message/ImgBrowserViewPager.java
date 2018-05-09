package com.ccq.app.ui.message;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/5/8 23:28
 * 描述：
 * 版本：
 *
 **************************************************/

public class ImgBrowserViewPager extends ViewPager {


    public ImgBrowserViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
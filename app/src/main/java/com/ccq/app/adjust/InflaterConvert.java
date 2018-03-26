package com.ccq.app.adjust;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.yan.inflaterauto.AutoConvert;
import com.yan.inflaterauto.annotation.Convert;

import java.util.HashMap;

/****************************************
 * 功能说明:  适配注解设置
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

@Convert({LinearLayout.class
        , FrameLayout.class
        , NestedScrollView.class
        , RecyclerView.class
        , ListView.class
        , ScrollView.class
        , CoordinatorLayout.class
        , ConstraintLayout.class
        , AHBottomNavigation.class
})
public class InflaterConvert implements AutoConvert {// 类名随便写

    @Override
    public HashMap<String, String> getConvertMap() {
        return null;// 添加映射
    }
}

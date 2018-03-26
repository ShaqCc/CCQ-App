package com.ccq.app.base;

import android.app.Application;

import com.ccq.app.adjust.InfAutoInflaterConvert;
import com.yan.inflaterauto.AutoBaseOn;
import com.yan.inflaterauto.InflaterAuto;

/****************************************
 * 功能说明:Application
 *
 * Author: Created by bayin on 2018/3/26.
 ****************************************/

public class CcqApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        初始化屏幕适配设置
        InflaterAuto.init(new InflaterAuto.Builder()
                .width(720)
                .height(1280)
                .baseOnDirection(AutoBaseOn.Both)
                .inflaterConvert(new InfAutoInflaterConvert())
                .build()
        );
    }
}

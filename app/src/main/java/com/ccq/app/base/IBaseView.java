package com.ccq.app.base;

import android.app.Activity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 20:42
 * 描述：视图基类接口，控制界面的显示状态，加载中；加载成功（有数据）；加载失败（网络错误）；加载结果为空；
 * 版本：v1.0
 *
 **************************************************/

public interface IBaseView {

    Activity get();

    void loading();

    void loadingSuccess();

    void loadingError();

    void loadingEmpty();
}

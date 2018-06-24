package com.ccq.app.ui.reprot;

import android.app.Activity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/6/24 17:44
 * 描述：
 * 版本：
 *
 **************************************************/

public interface IReportView {
    Activity get();

    void reportSuccess();

    void failure(String message);
}

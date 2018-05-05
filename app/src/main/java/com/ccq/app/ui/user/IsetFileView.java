package com.ccq.app.ui.user;

import android.app.Activity;

import com.ccq.app.entity.BaseBean;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/22 22:46
 * 描述：
 * 版本：
 *
 **************************************************/

public interface IsetFileView {
    Activity get();
    void bindSuccess();
    void bindFailuer();
    void sendCodeSuccess(BaseBean baseBean);
}

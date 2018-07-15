package com.ccq.app.ui.user;

import android.app.Activity;

import com.ccq.app.base.BaseActivity;
import com.ccq.app.entity.BaseBean;
import com.ccq.app.entity.SubscriberCountBean;
import com.ccq.app.entity.UserBanner;
import com.ccq.app.entity.UserBean;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/15 16:04
 * 描述：
 * 版本：
 *
 **************************************************/

public interface IUserView {
    BaseActivity getHostActivity();
    void setUserView(UserBean userBean);

    void setBanner(UserBanner banner);

    void setSubscriber(BaseBean baseBean);

    void setSubCount(SubscriberCountBean bean);

    void dismissProgress();
}

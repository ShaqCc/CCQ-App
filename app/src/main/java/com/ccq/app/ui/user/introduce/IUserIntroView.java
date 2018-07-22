package com.ccq.app.ui.user.introduce;

import com.ccq.app.base.BaseActivity;
import com.ccq.app.entity.UserLocationBean;

import java.util.List;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/22 13:54
 * 描述：
 * 版本：
 *
 **************************************************/

public interface IUserIntroView {

    BaseActivity getAty();

    void setLocation(UserLocationBean bean);

    void setAddress(String address);

    void setImageList(UserImageBean data);
}

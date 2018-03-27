package com.ccq.app.ui.home;

import com.ccq.app.base.IBaseView;
import com.ccq.app.entity.Banner;

import java.util.List;

/****************************************
 * 功能说明:  主页的视图接口
 *
 * Author: Created by bayin on 2018/3/27.
 ****************************************/

public interface IHomeView extends IBaseView{
    void showBanner(List<Banner> banners);
}

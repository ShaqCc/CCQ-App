package com.ccq.app.ui.home;

import com.ccq.app.base.IBaseView;
import com.ccq.app.entity.BannerBean;
import com.ccq.app.entity.BrandBean;
import com.ccq.app.entity.Car;

import java.util.List;

/****************************************
 * 功能说明:  主页的视图接口
 *
 * Author: Created by bayin on 2018/3/27.
 ****************************************/

public interface IHomeView extends IBaseView{
    void showBanner(List<BannerBean> banners);
    void showCarList(List<Car> cars);
    void showBrandList(List<BrandBean> list);
}

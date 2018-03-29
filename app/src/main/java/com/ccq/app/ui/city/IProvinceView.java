package com.ccq.app.ui.city;

import com.ccq.app.entity.Province;

import java.util.List;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/29.
 ****************************************/

public interface IProvinceView {
    void setProvinceData(List<Province> list);
    void setCityData(List<Province.CityBean> list);
}

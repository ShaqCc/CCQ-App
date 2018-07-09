package com.ccq.app.http;

import java.util.HashMap;
import java.util.Map;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/6/27 23:36
 * 描述：
 * 版本：
 *
 **************************************************/

public class HomeCarParams {
    /**
     * order     排序   int
     * type     0在售，1已售
     * brandid   品牌id
     * tonnageid  吨位id
     * numberid  型号id
     * yearid     年份id
     * provinceid 省id
     * cityid     市 id
     * keyword  关键词搜索
     * page    页码，默认1
     * size     每页多少个
     * ispage   是否返回分页（1分页，0无分页数据）
     */

    private static Map<String, String> params = new HashMap<>();

    private static HomeCarParams mInstance;

    private String carBrandName;//品牌名称
    private String carTypeName;//型号名称
    private String carUseTime;//使用年限
    private String cityName;
    private String orderName = "默认，时间排序";

    public synchronized static HomeCarParams getInstance(){
        if (mInstance == null){
            mInstance = new HomeCarParams();
        }
        return mInstance;
    }


    public void put(String key, String value) {
        params.put(key, value);
    }

    public void delete(String key) {
        params.remove(key);
    }

    public Map<String, String> getParams() {
        if (params.isEmpty())
            return resetParams();
        return params;
    }

    public Map<String, String> resetParams() {
        params.clear();
        params.put("page", "1");
        params.put("size", "20");
        params.put("type", "0");
        return params;
    }

    public void resetTags() {
        carBrandName = "";
        carTypeName = "";
        carUseTime = "";
        cityName = "";
    }


    public String getCarBrandName() {
        return carBrandName;
    }

    public void setCarBrandName(String naem) {
        carBrandName = naem;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String name) {
        this.carTypeName = name;
    }

    public String getCarUseTime() {
        return carUseTime;
    }

    public void setCarUseTime(String time) {
        this.carUseTime = time;
    }

    public String getValue(String key) {
        return params.get(key);
    }

    public void setCityName(String name) {
        cityName = name;
    }

    public String getCityName() {
        return cityName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderName() {
        return orderName;
    }
}

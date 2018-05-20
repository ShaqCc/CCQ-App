package com.ccq.app.http;

import java.util.HashMap;
import java.util.Map;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 0:46
 * 描述：App http请求的配置参数类
 * 版本：1.0
 *
 **************************************************/

public class ApiParams {
        public static String BASEURL = "http://apicheck.chanchequan.com";//测试环境
//    public static String BASEURL = "https://api.chanchequan.com";//正式环境


    private static Map<String, String> carMap;
    private static String cityName;//城市
    private static String brandName;//品牌名称
    private static String typeName;//型号
    private static String orderFunc;//排序方式
    private static String age;//车龄

    public static String getBrandName() {
        return brandName;
    }

    public static void setBrandName(String b) {
        brandName = b;
    }

    public static String getTypeName() {
        return typeName;
    }

    public static void setTypeName(String t) {
        typeName = t;
    }

    public static String getOrderFunc() {
        return orderFunc;
    }

    public static void setOrderFunc(String o) {
        orderFunc = o;
    }

    public static String getAge() {
        return age;
    }

    public static void setAge(String a) {
        age = a;
    }

    public static Map<String, String> getCarMap() {
        if (carMap == null) initCarMap();
        return carMap;
    }

    public static Map<String, String> initCarMap() {
        carMap = new HashMap<>();
        carMap.put("page", "1");
        carMap.put("size", "20");
        return carMap;
    }

    public static void setPage(int page){
        carMap.put("page",String.valueOf(page));
    }

    public static void setOrder(String order) {
        carMap.put("order", order);
    }

    public static void setType(String type) {
        carMap.put("type", type);
    }

    public static void setBrandid(String brandid) {
        carMap.put("brandid", brandid);
    }

    public static void setTon(String tonnageid) {
        carMap.put("tonnageid", tonnageid);
    }

    public static void setNumberid(String numberid) {
        carMap.put("numberid", numberid);
    }

    public static void setYearid(String yearid) {
        carMap.put("yearid", yearid);
    }

    public static void setProvinceid(String provinceid) {
        carMap.put("provinceid", provinceid);
    }

    public static void setCityid(String cityid) {
        carMap.put("cityid", cityid);
    }

    public static void setCityName(String cityName){
        carMap.put("cityName",cityName);
    }
    public static String getCityName(){
        return carMap.get("cityName");
    }

    public static void setKeyword(String keyword) {
        carMap.put("keyword", keyword);
    }

    public static void setIspage(String ispage) {
        carMap.put("ispage", ispage);
    }


    public static String getPage(){return carMap.get("page");}

    public static String getOrder() {
        return carMap.get("order");
    }

    public static String getType() {
        return carMap.get("type");
    }

    public static String getBrandid() {
        return carMap.get("brandid");
    }

    public static String getTon() {
        return carMap.get("tonnageid");
    }

    public static String getNumberid() {
        return carMap.get("numberid");
    }

    public static String getYearid() {
        return carMap.get("yearid");
    }

    public static String getProvinceid() {
        return carMap.get("provinceid");
    }

    public static String getCityid() {
        return carMap.get("cityid");
    }

    public static String getKeyword() {
        return carMap.get("keyword");
    }

    public static String getIspage() {
        return carMap.get("ispage");
    }

    public static void removeCity(){
        carMap.remove("cityid");
        carMap.remove("provinceid");
        carMap.remove("cityName");
    }

    public static void removeBrand(){
        carMap.remove("brandid");
        brandName = "";
    }

    public static void removeType(){
        carMap.remove("type");
        typeName = "";
    }

    public static void removeAge(){
        carMap.remove("yearid");
        age = "";
    }

    public static void removeOrderFunc(){
        carMap.remove("order");
        orderFunc = "";
    }

}

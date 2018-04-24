package com.ccq.app.entity;

import java.util.List;


/**
 * 发布车辆信息
 */
public class CarInfo {
    /**
     *     userid              int64
     pinpai              int    品牌id
     tonnage            int    吨位id
     number            int     型号id
     year               int     年份（2001）
     price               float   0面议
     imglist           string     图片的id 多图以,分隔如：1,2,3
     videolist          string    跟图片一样
     content         string     说明
     latitude         string       车辆位置经纬度
     longitude      string
     address          string       位置
     phone          string      手机号
     phoneCode       string     未登录用户发布时需要手机验证码
     typeid          int        0在售，1已售
     */
    private int userid;
    private int pinpai;
    private int number;
    private int year;
    private float price;

    private String imglist;//图片的id 多图以,分隔如：1,2,3
    private String videolist; //
    private String content; //

    private String latitude;
    private String longitude;
    private String address;
    private String phone;
    private String phoneCode;
    private int typeid;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getPinpai() {
        return pinpai;
    }

    public void setPinpai(int pinpai) {
        this.pinpai = pinpai;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImglist() {
        return imglist;
    }

    public void setImglist(String imglist) {
        this.imglist = imglist;
    }

    public String getVideolist() {
        return videolist;
    }

    public void setVideolist(String videolist) {
        this.videolist = videolist;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }
}

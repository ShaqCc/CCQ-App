package com.ccq.app.entity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/14 23:05
 * 描述：
 * 版本：
 *
 **************************************************/

public class UserLocationBean {
    private int code;
    private String message;
    private String name;
    private String address;
    private String longitude;
    private String latitude;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}

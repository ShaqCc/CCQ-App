package com.ccq.app.entity;

import java.io.Serializable;

/**
 * 作者： mly
 * 日期： 2018/4/22.
 */
public class BrandModelBean implements Serializable {

    private String id;
    private String name;
    private String pid;
    private String bid;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

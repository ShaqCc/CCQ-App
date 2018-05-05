package com.ccq.app.entity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/5/5 21:54
 * 描述：
 * 版本：
 *
 **************************************************/

public class BindResultBaen {

    /**
     * code : 0
     * message : 绑定成功
     * userid : 197
     */

    private int code;
    private String message;
    private String userid;

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

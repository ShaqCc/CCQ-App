package com.ccq.app.entity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/22 23:55
 * 描述：
 * 版本：
 *
 **************************************************/

public class BaseBean<T> {
    private int code;
    private String message;
    private T data;

    public BaseBean(int code, String message) {
        this.code = code;
        this.message = message;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

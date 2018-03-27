package com.ccq.app.base;

/****************************************
 * 功能说明:  基类javabean
 *
 * Author: Created by bayin on 2018/3/27.
 ****************************************/

public class BaseBean<D> {
    private String code;
    private String message;
    private D data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}

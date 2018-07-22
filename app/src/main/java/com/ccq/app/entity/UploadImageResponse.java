package com.ccq.app.entity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/7/22 15:26
 * 描述：
 * 版本：
 *
 **************************************************/

public class UploadImageResponse {

    /**
     * code : 0
     * message : http://img10.chanchequan.com/upload/2018/07/22/1532243693064979695.jpg
     * imageid : 96
     * sort : 0
     */

    private int code;
    private String message;
    private long imageid;
    private int sort;

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

    public long getImageid() {
        return imageid;
    }

    public void setImageid(long imageid) {
        this.imageid = imageid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}

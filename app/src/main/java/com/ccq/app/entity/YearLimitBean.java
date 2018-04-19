package com.ccq.app.entity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/19 23:32
 * 描述：
 * 版本：
 *
 **************************************************/

public class YearLimitBean {

    /**
     * code : year
     * id : 1
     * name : 3年内
     * shows : 1
     * sort : 0
     * value : null
     */

    private String code;
    private String id;
    private String name;
    private String shows;
    private String sort;
    private Object value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getShows() {
        return shows;
    }

    public void setShows(String shows) {
        this.shows = shows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

package com.ccq.app.entity;

import java.io.Serializable;

/**
 * 作者： 巴银
 * 日期： 2018/4/17.
 * 描述：
 */
public class BrandBean implements Serializable{


    /**
     * code : lin
     * hot : 1
     * id : 2
     * image : /Public/uploads/2017/01/20/588226e3ea700.png
     * name : 临工
     * paixu : 1
     * shows : 1
     * sort : L
     * tuijian : 0
     */

    private String code;
    private String hot;
    private String id;
    private String image;
    private String name;
    private String paixu;
    private String shows;
    private String sort;
    private String tuijian;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaixu() {
        return paixu;
    }

    public void setPaixu(String paixu) {
        this.paixu = paixu;
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

    public String getTuijian() {
        return tuijian;
    }

    public void setTuijian(String tuijian) {
        this.tuijian = tuijian;
    }
}

package com.ccq.app.http;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 22:26
 * 描述：查询车辆列表的参数
 * 版本：1.0
 *
 **************************************************/

public class CarParams {
    private int order;//排序
    private String type;//0在售，1已售
    private String brandid;//品牌
    private String tonnageid;  //吨位id
    private String numberid;  //型号id
    private String yearid;     //年份id
    private String provinceid; //省id
    private String cityid;     //市 id
    private String keyword;  //关键词搜索
    private int page; //   页码，默认1
    private int size;     //每页多少个
    private int ispage;   //是否返回分页（1分页，0无分页数据）


    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getTonnageid() {
        return tonnageid;
    }

    public void setTonnageid(String tonnageid) {
        this.tonnageid = tonnageid;
    }

    public String getNumberid() {
        return numberid;
    }

    public void setNumberid(String numberid) {
        this.numberid = numberid;
    }

    public String getYearid() {
        return yearid;
    }

    public void setYearid(String yearid) {
        this.yearid = yearid;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getIspage() {
        return ispage;
    }

    public void setIspage(int ispage) {
        this.ispage = ispage;
    }
}

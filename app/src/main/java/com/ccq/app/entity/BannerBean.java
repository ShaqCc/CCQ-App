package com.ccq.app.entity;

import com.ccq.app.base.BaseBean;

/****************************************
 * 功能说明:  
 *
 * Author: Created by bayin on 2018/3/27.
 ****************************************/

public class BannerBean extends BaseBean<BannerBean> {

    /**
     * href : www.chanchequan.com
     * id : 15
     * image : http://www.chanchequan.com/Public/thumupload/2017/03/01/00c3c2c46e37143.jpg
     * sortid : 0
     * status : 0
     * title : 幻灯
     */

    private String href;
    private int id;
    private String image;
    private int sortid;
    private int status;
    private String title;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSortid() {
        return sortid;
    }

    public void setSortid(int sortid) {
        this.sortid = sortid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

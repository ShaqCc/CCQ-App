package com.ccq.app.entity;

import java.util.List;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/6/3 16:09
 * 描述：
 * 版本：
 *
 **************************************************/

public class TypeBean {

    /**
     * code : 50
     * gong : 1
     * hot : 1
     * id : 1
     * name : 50系
     * paixu : 1
     * shows : 1
     * tuijian : 1
     * numberList : [{"bid":5,"code":"50C","hot":null,"id":10,"name":"50C","paixu":6,"shows":1,"tid":1},{"bid":5,"code":"50CN","hot":1,"id":11,"name":"50CN","paixu":7,"shows":1,"tid":1},{"bid":5,"code":"855","hot":1,"id":12,"name":"855","paixu":8,"shows":1,"tid":1},{"bid":5,"code":"855N","hot":1,"id":13,"name":"855N","paixu":9,"shows":1,"tid":1},{"bid":5,"code":"856","hot":null,"id":14,"name":"856","paixu":10,"shows":1,"tid":1}]
     */

    private String code;
    private String gong;
    private String hot;
    private String id;
    private String name;
    private String paixu;
    private String shows;
    private String tuijian;
    private List<NumberListBean> numberList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGong() {
        return gong;
    }

    public void setGong(String gong) {
        this.gong = gong;
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

    public String getTuijian() {
        return tuijian;
    }

    public void setTuijian(String tuijian) {
        this.tuijian = tuijian;
    }

    public List<NumberListBean> getNumberList() {
        return numberList;
    }

    public void setNumberList(List<NumberListBean> numberList) {
        this.numberList = numberList;
    }

    public static class NumberListBean {
        public boolean isSub() {
            return isSub;
        }

        public void setSub(boolean sub) {
            isSub = sub;
        }

        /**
         * bid : 5
         * code : 50C
         * hot : null
         * id : 10
         * name : 50C
         * paixu : 6
         * shows : 1
         * tid : 1
         */

        private boolean isSub;
        private String bid;
        private String code;
        private String hot;
        private String id;
        private String name;
        private String paixu;
        private String shows;
        private String tid;

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

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }
    }
}

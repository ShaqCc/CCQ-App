package com.ccq.app.entity;

import java.util.List;

/**
 * Created by littlemax on 2018/5/7.
 */

public class SubscribeUser {
    /**
     * {
     * code:0,
     * message:””,
     * data:[
     * {
     * nickname:”昵称”,
     * headimgurl:”头像”,
     * userid:0   关注人的id
     * sub_userid:0  关注的人id
     * istemplate:0   是否提醒，最多5个
     * },
     * ],
     * count:10          总数
     * }
     */
    private int count;

    private int code;

    private String message;

    private List<SubUsr> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public List<SubUsr> getData() {
        return data;
    }

    public void setData(List<SubUsr> data) {
        this.data = data;
    }

    public class SubUsr {
        private String nickname;
        private String headimgurl;
        private String userid;
        private String sub_userid;
        private int istemplate;
        private int onSaleCount;
        private int SaleOutCount;

        public int getOnSaleCount() {
            return onSaleCount;
        }

        public void setOnSaleCount(int onSaleCount) {
            this.onSaleCount = onSaleCount;
        }

        public int getSaleOutCount() {
            return SaleOutCount;
        }

        public void setSaleOutCount(int saleOutCount) {
            SaleOutCount = saleOutCount;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getSub_userid() {
            return sub_userid;
        }

        public void setSub_userid(String sub_userid) {
            this.sub_userid = sub_userid;
        }

        public int getIstemplate() {
            return istemplate;
        }

        public void setIstemplate(int istemplate) {
            this.istemplate = istemplate;
        }
    }


}


package com.ccq.app.entity;

import java.io.Serializable;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/25 0:26
 * 描述：铲车圈用户信息
 * 版本：
 *
 **************************************************/

public class UserBean implements Serializable {

    /**
     * addtime : 1522309141
     * carcount : null
     * dealer : 0
     * dealertype : 0
     * headimgurl : https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIQmzbrs8PDqIAbzj4bicDGia90ibkIqJ6DFeeZylYyJEuAsPBRmibNtXF0UM6AZ4pjqRjz2RK0eyKVLw/0
     * hot : 0
     * htime : 0
     * jinyanInfo : {"userid":0,"zaishou":true,"yishou":true,"qiugou":true,"center":true,"jxs":true,"type":0,"day_count":0,"jinyan_day":0}
     * jxsendtime : null
     * jxsktime : null
     * ktime : null
     * lastlogin : null
     * liulancount : null
     * logstr : null
     * logwxstr : null
     * mobile : 15286832261
     * nickname : Victory
     * password : 101632c647efaf6e96ff3e942da3abbe
     * refcount : 0
     * refdate : null
     * reminddate : 2018-03-29 15:39:01
     * showzhiding : 0
     * state : 0
     * times : null
     * tjadmin : 0
     * tjpower :
     * type : 0
     * userid : 11185
     * vip : 0
     * viptype : 0
     * zhiding : 0
     *
     *
     * "yishou_count":5,       发布的已售数量
     "zaishou_count":188,    发布的在售数量
     "about":"",
     "content":""            简介

     "province":1,                      省ID
     "city":1,                          市 ID
     "address":"",                      详细地址
     "jingdu":"116.39564503788",       经度
     "weidu":"39.92998577808",        纬度
     "provinceName":"北京市",           省名称
     "cityName":"北京市",                 市名称
     */

    private int addtime;
    private String carcount;//发布的车辆数量null或数值
    private int dealer; //0 不是经销商，1 未认证，2 已认证（经销商）
    private int dealertype;//不知道没用
    private String headimgurl;
    private int hot;//0,1,推荐热门（首页的热门经销商
    private int htime;//会员到期时间
    private JinyanInfoBean jinyanInfo;//禁言信息
    private String jxsendtime;//经销商到期时间
    private Object jxsktime;
    private Object ktime;
    private Object lastlogin;
    private Object liulancount;
    private Object logstr;
    private Object logwxstr;
    private String mobile;
    private String nickname;
    private String password;
    private String erweima;
    private int refcount;
    private Object refdate;
    private String reminddate;
    private String showzhiding;
    private String state;
    private Object times;
    private int tjadmin;
    private String tjpower;
    private int type;
    private String userid;
    private int vip;
    private int viptype;
    private int zhiding;
    private String jiguang_name;

    /**
     * 个人中心 （已shou数量，未售数量，简介,地区，经纬度）
     */
    private int yishou_count;
    private int zaishou_count;
    private String content;
    private String provinceName;
    private String cityName;
    private String jingdu;
    private String weidu;
    private boolean isBusiness;
    private boolean isMember;


    public String getErweima() {
        return erweima;
    }

    public void setErweima(String erweima) {
        this.erweima = erweima;
    }

    public String getJiguang_name() {
        return jiguang_name;
    }

    public void setJiguang_name(String jiguang_name) {
        this.jiguang_name = jiguang_name;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public void setBusiness(boolean business) {
        isBusiness = business;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setMember(boolean member) {
        isMember = member;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getJingdu() {
        return jingdu;
    }

    public void setJingdu(String jingdu) {
        this.jingdu = jingdu;
    }

    public String getWeidu() {
        return weidu;
    }

    public void setWeidu(String weidu) {
        this.weidu = weidu;
    }

    public int getYishou_count() {
        return yishou_count;
    }

    public void setYishou_count(int yishou_count) {
        this.yishou_count = yishou_count;
    }

    public int getZaishou_count() {
        return zaishou_count;
    }

    public void setZaishou_count(int zaishou_count) {
        this.zaishou_count = zaishou_count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public String getCarcount() {
        return carcount;
    }

    public void setCarcount(String carcount) {
        this.carcount = carcount;
    }

    public int getDealer() {
        return dealer;
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    public int getDealertype() {
        return dealertype;
    }

    public void setDealertype(int dealertype) {
        this.dealertype = dealertype;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getHtime() {
        return htime;
    }

    public void setHtime(int htime) {
        this.htime = htime;
    }

    public JinyanInfoBean getJinyanInfo() {
        return jinyanInfo;
    }

    public void setJinyanInfo(JinyanInfoBean jinyanInfo) {
        this.jinyanInfo = jinyanInfo;
    }

    public String getJxsendtime() {
        return jxsendtime;
    }

    public void setJxsendtime(String jxsendtime) {
        this.jxsendtime = jxsendtime;
    }

    public Object getJxsktime() {
        return jxsktime;
    }

    public void setJxsktime(Object jxsktime) {
        this.jxsktime = jxsktime;
    }

    public Object getKtime() {
        return ktime;
    }

    public void setKtime(Object ktime) {
        this.ktime = ktime;
    }

    public Object getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Object lastlogin) {
        this.lastlogin = lastlogin;
    }

    public Object getLiulancount() {
        return liulancount;
    }

    public void setLiulancount(Object liulancount) {
        this.liulancount = liulancount;
    }

    public Object getLogstr() {
        return logstr;
    }

    public void setLogstr(Object logstr) {
        this.logstr = logstr;
    }

    public Object getLogwxstr() {
        return logwxstr;
    }

    public void setLogwxstr(Object logwxstr) {
        this.logwxstr = logwxstr;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRefcount() {
        return refcount;
    }

    public void setRefcount(int refcount) {
        this.refcount = refcount;
    }

    public Object getRefdate() {
        return refdate;
    }

    public void setRefdate(Object refdate) {
        this.refdate = refdate;
    }

    public String getReminddate() {
        return reminddate;
    }

    public void setReminddate(String reminddate) {
        this.reminddate = reminddate;
    }

    public String getShowzhiding() {
        return showzhiding;
    }

    public void setShowzhiding(String showzhiding) {
        this.showzhiding = showzhiding;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Object getTimes() {
        return times;
    }

    public void setTimes(Object times) {
        this.times = times;
    }

    public int getTjadmin() {
        return tjadmin;
    }

    public void setTjadmin(int tjadmin) {
        this.tjadmin = tjadmin;
    }

    public String getTjpower() {
        return tjpower;
    }

    public void setTjpower(String tjpower) {
        this.tjpower = tjpower;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getViptype() {
        return viptype;
    }

    public void setViptype(int viptype) {
        this.viptype = viptype;
    }

    public int getZhiding() {
        return zhiding;
    }

    public void setZhiding(int zhiding) {
        this.zhiding = zhiding;
    }

    public static class JinyanInfoBean {
        /**
         * userid : 0
         * zaishou : true
         * yishou : true
         * qiugou : true
         * center : true
         * jxs : true
         * type : 0
         * day_count : 0
         * jinyan_day : 0
         */

        private int userid;
        private boolean zaishou;
        private boolean yishou;
        private boolean qiugou;
        private boolean center;
        private boolean jxs;
        private int type;
        private int day_count;
        private int jinyan_day;

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public boolean isZaishou() {
            return zaishou;
        }

        public void setZaishou(boolean zaishou) {
            this.zaishou = zaishou;
        }

        public boolean isYishou() {
            return yishou;
        }

        public void setYishou(boolean yishou) {
            this.yishou = yishou;
        }

        public boolean isQiugou() {
            return qiugou;
        }

        public void setQiugou(boolean qiugou) {
            this.qiugou = qiugou;
        }

        public boolean isCenter() {
            return center;
        }

        public void setCenter(boolean center) {
            this.center = center;
        }

        public boolean isJxs() {
            return jxs;
        }

        public void setJxs(boolean jxs) {
            this.jxs = jxs;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getDay_count() {
            return day_count;
        }

        public void setDay_count(int day_count) {
            this.day_count = day_count;
        }

        public int getJinyan_day() {
            return jinyan_day;
        }

        public void setJinyan_day(int jinyan_day) {
            this.jinyan_day = jinyan_day;
        }
    }
}

package com.ccq.app.entity;

import java.io.Serializable;
import java.util.List;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/3/27 21:41
 * 描述：首页车辆列表
 * 版本：v1.0
 *
 **************************************************/

public class Car implements Serializable{
    private String address;
    private int addtime;
    private String addtime_format;
    private String cityName;
    private String content;
    private int count;
    private int id;
    private String name;
    private String phone;
    private int pic_img_count;
    private String price;
    private String provinceName;
    private int type;
    private UserInfoBean userInfo;
    private int year;
    private String isshare;
    private java.util.List<PicImgBean> pic_img;
    /**
     *  获取车辆位置接口
     *  {
     code:0,
     message:””
     address:”详情地址”
     name :”标题”,
     “longitude”:””,
     “latitude”:””       经纬度
     }
     */
    private String longitude;
    private String latitude;
    private String detailAddress;


    /**
    /**
     * 修改信息 新增字段
     */
    private String BrandName;
    private String NumberName;
    private String brandId;
    private String numberId;
    private String pic;
    private String videoIds;
    private List<VideoBean> videoList;

    public String getVideoIds() {
        return videoIds;
    }

    public void setVideoIds(String videoIds) {
        this.videoIds = videoIds;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public List<VideoBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoBean> videoList) {
        this.videoList = videoList;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public String getNumberName() {
        return NumberName;
    }

    public void setNumberName(String numberName) {
        NumberName = numberName;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    private PicImgBean picfmid_img;

    public PicImgBean getPicfmid_img() {
        return picfmid_img;
    }

    public void setPicfmid_img(PicImgBean picfmid_img) {
        this.picfmid_img = picfmid_img;
    }

    public String getIsshare() {
        return isshare;
    }

    public void setIsshare(String isshare) {
        this.isshare = isshare;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAddtime() {
        return addtime;
    }

    public void setAddtime(int addtime) {
        this.addtime = addtime;
    }

    public String getAddtime_format() {
        return addtime_format;
    }

    public void setAddtime_format(String addtime_format) {
        this.addtime_format = addtime_format;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getPic_img_count() {
        return pic_img_count;
    }

    public void setPic_img_count(int pic_img_count) {
        this.pic_img_count = pic_img_count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<PicImgBean> getPic_img() {
        return pic_img;
    }

    public void setPic_img(List<PicImgBean> pic_img) {
        this.pic_img = pic_img;
    }

    public class UserInfoBean implements Serializable {
        /**
         * userid : 169
         * mobile : 18600175665
         * dealer : 0
         * vip : 0
         * htime : 0
         * jxsendtime : 0
         * province : 1
         * city : 1
         * address :
         * jingdu : 39.812614
         * weidu :
         * nickname : A0000000二手装载机
         * headimgurl : https://wx.qlogo.cn/mmopen/vi_32/FNaOpaf1tTjUW8GI4JicG892wE7ia1vPdHsrYFpaQEFfiakJ1BcAPjld8yMGvbWeEbo1u1olW51ESqmFVzGu3iapgA/0
         * refcount : 0
         * refdate : {}
         * provinceName : 北京市
         * cityName : 北京市
         * isBusiness : false
         * isAuthentication : false
         * isMember : false
         * openid : o7-IJwda3J_Gh16bK6ecvo2PlRkU
         * ktime : 0
         */

        private int userid;
        private String headimgurl;
        private String nickname;
        private String jiguang_name;


        public String getJiguang_name() {
            return jiguang_name;
        }

        public void setJiguang_name(String jiguang_name) {
            this.jiguang_name = jiguang_name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }
    }


    public class PicImgBean implements Serializable {

        /**
         * id : 5488
         * savename : http://img11.miheyingua.cn/2017/08/27/20170827163733385.jpg
         */

        private String id;
        private String savename;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSavename() {
            return savename;
        }

        public void setSavename(String savename) {
            this.savename = savename;
        }
    }

    public class VideoBean implements Serializable{

        private String id;
        private String name;
        private String osspath;

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

        public String getOsspath() {
            return osspath;
        }

        public void setOsspath(String osspath) {
            this.osspath = osspath;
        }
    }
}

package com.ccq.app.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/10 1:09
 * 描述：
 * 版本：
 *
 **************************************************/

public class WxUserInfo implements Parcelable{

    /**
     * openid : oUVvw1C3tmgCM2MNQWISiAmCSgy0
     * nickname : 奔跑吧
     * sex : 1
     * language : zh_CN
     * city :
     * province :
     * country : 中国
     * headimgurl : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI4ez1tMRjicCBQ3aEP32apHoFVLbubjOoxyjeKB8E5ibDMcibur7bPkuSJ7ibKWLseFkiaIsPhe29o0gg/132
     * privilege : []
     * unionid : oCvvP0ZYKZL1UEqJuMhYFgprtlsw
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    protected WxUserInfo(Parcel in) {
        openid = in.readString();
        nickname = in.readString();
        sex = in.readInt();
        language = in.readString();
        city = in.readString();
        province = in.readString();
        country = in.readString();
        headimgurl = in.readString();
        unionid = in.readString();
    }

    public static final Creator<WxUserInfo> CREATOR = new Creator<WxUserInfo>() {
        @Override
        public WxUserInfo createFromParcel(Parcel in) {
            return new WxUserInfo(in);
        }

        @Override
        public WxUserInfo[] newArray(int size) {
            return new WxUserInfo[size];
        }
    };

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(openid);
        parcel.writeString(nickname);
        parcel.writeInt(sex);
        parcel.writeString(language);
        parcel.writeString(city);
        parcel.writeString(province);
        parcel.writeString(country);
        parcel.writeString(headimgurl);
        parcel.writeString(unionid);
    }
}

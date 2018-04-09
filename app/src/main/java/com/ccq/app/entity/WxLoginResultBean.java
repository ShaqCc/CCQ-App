package com.ccq.app.entity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/4/10 0:06
 * 描述：微信登陆验证结果
 * 版本：
 *
 **************************************************/

public class WxLoginResultBean {

    /**
     * access_token : 8_EU-PsNN-jkBajPTybdRtDspkSu4B_7Ubvkn9q3jNml80hXY8fIqw7sYx1FpW9Raajch4l4xLwzD5FMiYI0sk9IsWzPOJY0kdzndXOUKi8cs
     * expires_in : 7200
     * refresh_token : 8_gPIoI06xBab6Q5IIOF68tH5zyKMRR_qBjdevf-ZdJuc-3BVTYVTJECiiZs8hfzGnf0LQBACshs5KP2UZjWm8W7X9t_xlE7Yx_W82E7zJLUM
     * openid : oUVvw1C3tmgCM2MNQWISiAmCSgy0
     * scope : snsapi_userinfo
     * unionid : oCvvP0ZYKZL1UEqJuMhYFgprtlsw
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}

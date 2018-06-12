package com.ccq.app.entity;

/**************************************************
 *
 * 作者：巴银
 * 时间：2018/6/10 18:28
 * 描述：
 * 版本：
 *
 **************************************************/

public class WeixinPayBean {

    /**
     * appId : wx1a2af6a07fd055a2
     * partnerId : 1505799371
     * prepayId : wx102119445987588984d6add92644555568
     * packageValue : Sign=WXPay
     * nonceStr : 9hwPF4gQ3z3iupuV
     * timeStamp : 1528636331
     * sign : 4F5AD18410C2DA8EF95973455E2F5AE3
     */

    private String appId;
    private String partnerId;
    private String prepayId;
    private String packageValue;
    private String nonceStr;
    private String timeStamp;
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

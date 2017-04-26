package com.hiden.biz.wechat.mp.bean.result;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;

public class WxMpUser implements Serializable {
    protected Boolean subscribe;
    protected String openId;
    protected String nickname;
    protected String sex;
    protected String language;
    protected String city;
    protected String province;
    protected String country;
    protected String headImgUrl;
    protected Long subscribeTime;
    protected String unionId;
    protected Integer sexId;

    public WxMpUser() {
    }

    public Boolean getSubscribe() {
        return this.subscribe;
    }

    public Boolean isSubscribe() {
        return this.subscribe;
    }

    public void setSubscribe(Boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenId() {
        return this.openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return this.headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Long getSubscribeTime() {
        return this.subscribeTime;
    }

    public void setSubscribeTime(Long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getUnionId() {
        return this.unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Integer getSexId() {
        return this.sexId;
    }

    public void setSexId(Integer sexId) {
        this.sexId = sexId;
    }

    public static WxMpUser fromJson(String json) {
        return (WxMpUser)WxMpGsonBuilder.INSTANCE.create().fromJson(json, WxMpUser.class);
    }

    public String toString() {
        return "WxMpUser{subscribe=" + this.subscribe + ", openId=\'" + this.openId + '\'' + ", nickname=\'" + this.nickname + '\'' + ", sex=\'" + this.sex + '\'' + ", language=\'" + this.language + '\'' + ", city=\'" + this.city + '\'' + ", province=\'" + this.province + '\'' + ", country=\'" + this.country + '\'' + ", headImgUrl=\'" + this.headImgUrl + '\'' + ", subscribeTime=" + this.subscribeTime + ", unionId=\'" + this.unionId + '\'' + '}';
    }
}

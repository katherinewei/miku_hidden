package com.hiden.biz.wechat.common.model.response;

import com.hiden.biz.wechat.common.util.json.WxGsonBuilder;

import java.io.Serializable;

public class WxAccessToken
        implements Serializable {
    private String accessToken;
    private int expiresIn = -1;

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return this.expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public static WxAccessToken fromJson(String json) {
        return (WxAccessToken) WxGsonBuilder.create().fromJson(json, WxAccessToken.class);
    }
}
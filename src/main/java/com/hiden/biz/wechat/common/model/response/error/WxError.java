package com.hiden.biz.wechat.common.model.response.error;

import com.hiden.biz.wechat.common.util.json.WxGsonBuilder;

import java.io.Serializable;


public class WxError
        implements Serializable {
    private int errorCode;
    private String errorMsg;
    private String json;

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static WxError fromJson(String json) {
        WxError error = (WxError) WxGsonBuilder.create().fromJson(json, WxError.class);
        return error;
    }

    public String toString() {
        return "微信错误 errcode=" + this.errorCode + ", errmsg=" + this.errorMsg + "\njson:" + this.json;
    }
}

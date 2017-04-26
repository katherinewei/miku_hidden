package com.hiden.biz.wechat.common.model.response;

import java.io.Serializable;


public class WxJsapiSignature
        implements Serializable {
    private String noncestr;
    private String jsapiTicket;
    private long timestamp;
    private String url;
    private String signature;

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNoncestr() {
        return this.noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getJsapiTicket() {
        return this.jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}



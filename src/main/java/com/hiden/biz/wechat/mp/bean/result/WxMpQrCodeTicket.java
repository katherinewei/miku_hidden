package com.hiden.biz.wechat.mp.bean.result;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;

public class WxMpQrCodeTicket implements Serializable {
    protected String ticket;
    protected int expire_seconds = -1;
    protected String url;

    public WxMpQrCodeTicket() {
    }

    public String getTicket() {
        return this.ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getExpire_seconds() {
        return this.expire_seconds;
    }

    public void setExpire_seconds(int expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static WxMpQrCodeTicket fromJson(String json) {
        return (WxMpQrCodeTicket)WxMpGsonBuilder.INSTANCE.create().fromJson(json, WxMpQrCodeTicket.class);
    }
}

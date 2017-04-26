package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMpTemplateMessage implements Serializable {
    private String toUser;
    private String templateId;
    private String url;
    private String topColor;
    private List<WxMpTemplateData> datas = new ArrayList();

    public WxMpTemplateMessage() {
    }

    public String getToUser() {
        return this.toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopColor() {
        return this.topColor;
    }

    public void setTopColor(String topColor) {
        this.topColor = topColor;
    }

    public List<WxMpTemplateData> getDatas() {
        return this.datas;
    }

    public void setDatas(List<WxMpTemplateData> datas) {
        this.datas = datas;
    }

    public String toJson() {
        return WxMpGsonBuilder.INSTANCE.create().toJson(this);
    }
}

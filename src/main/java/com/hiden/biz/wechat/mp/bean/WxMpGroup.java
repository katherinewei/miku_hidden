package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;

public class WxMpGroup implements Serializable {
    private long id = -1L;
    private String name;
    private long count;

    public WxMpGroup() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public static WxMpGroup fromJson(String json) {
        return (WxMpGroup)WxMpGsonBuilder.create().fromJson(json, WxMpGroup.class);
    }

    public String toJson() {
        return WxMpGsonBuilder.create().toJson(this);
    }

    public String toString() {
        return "WxMpGroup [id=" + this.id + ", name=" + this.name + ", count=" + this.count + "]";
    }
}

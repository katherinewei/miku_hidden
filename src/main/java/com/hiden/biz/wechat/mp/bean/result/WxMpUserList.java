package com.hiden.biz.wechat.mp.bean.result;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.util.ArrayList;
import java.util.List;

public class WxMpUserList {
    protected int total = -1;
    protected int count = -1;
    protected List<String> openIds = new ArrayList();
    protected String nextOpenId;

    public WxMpUserList() {
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getOpenIds() {
        return this.openIds;
    }

    public void setOpenIds(List<String> openIds) {
        this.openIds = openIds;
    }

    public String getNextOpenId() {
        return this.nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
    }

    public static WxMpUserList fromJson(String json) {
        return (WxMpUserList)WxMpGsonBuilder.INSTANCE.create().fromJson(json, WxMpUserList.class);
    }
}

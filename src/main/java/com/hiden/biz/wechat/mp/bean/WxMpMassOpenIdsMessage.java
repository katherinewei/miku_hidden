//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMpMassOpenIdsMessage implements Serializable {
    private List<String> toUsers = new ArrayList();
    private String msgType;
    private String content;
    private String mediaId;

    public WxMpMassOpenIdsMessage() {
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String toJson() {
        return WxMpGsonBuilder.INSTANCE.create().toJson(this);
    }

    public List<String> getToUsers() {
        return this.toUsers;
    }

    public void addUser(String openId) {
        this.toUsers.add(openId);
    }
}

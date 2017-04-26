package com.hiden.biz.wechat.mp.bean;
import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;

public class WxMpMassGroupMessage implements Serializable {
    private Long groupId;
    private String msgtype;
    private String content;
    private String mediaId;

    public WxMpMassGroupMessage() {
    }

    public String getMsgtype() {
        return this.msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
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

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}

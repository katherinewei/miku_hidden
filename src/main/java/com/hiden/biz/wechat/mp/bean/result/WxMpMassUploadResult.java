package com.hiden.biz.wechat.mp.bean.result;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;

public class WxMpMassUploadResult implements Serializable {
    private String type;
    private String mediaId;
    private long createdAt;

    public WxMpMassUploadResult() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public static WxMpMassUploadResult fromJson(String json) {
        return (WxMpMassUploadResult)WxMpGsonBuilder.create().fromJson(json, WxMpMassUploadResult.class);
    }

    public String toString() {
        return "WxUploadResult [type=" + this.type + ", media_id=" + this.mediaId + ", created_at=" + this.createdAt + "]";
    }
}

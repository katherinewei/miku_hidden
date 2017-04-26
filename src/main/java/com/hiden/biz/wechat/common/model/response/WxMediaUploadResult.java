package com.hiden.biz.wechat.common.model.response;

import com.hiden.biz.wechat.common.util.json.WxGsonBuilder;

import java.io.Serializable;


public class WxMediaUploadResult
        implements Serializable {
    private String type;
    private String mediaId;
    private String thumbMediaId;
    private long createdAt;

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

    public String getThumbMediaId() {
        return this.thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public static WxMediaUploadResult fromJson(String json) {
        return (WxMediaUploadResult) WxGsonBuilder.create().fromJson(json, WxMediaUploadResult.class);
    }

    public String toString() {
        return "WxUploadResult [type=" + this.type + ", media_id=" + this.mediaId + ", thumb_media_id=" + this.thumbMediaId + ", created_at=" + this.createdAt + "]";
    }
}

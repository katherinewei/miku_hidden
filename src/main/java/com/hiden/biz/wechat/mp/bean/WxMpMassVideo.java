
package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;

public class WxMpMassVideo implements Serializable {
    private String mediaId;
    private String title;
    private String description;

    public WxMpMassVideo() {
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

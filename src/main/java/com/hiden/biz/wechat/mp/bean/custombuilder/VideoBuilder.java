package com.hiden.biz.wechat.mp.bean.custombuilder;

import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage;

public final class VideoBuilder extends BaseBuilder<VideoBuilder> {
    private String mediaId;
    private String title;
    private String description;
    private String thumbMediaId;

    public VideoBuilder() {
        this.msgType = "video";
    }

    public VideoBuilder mediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public VideoBuilder title(String title) {
        this.title = title;
        return this;
    }

    public VideoBuilder description(String description) {
        this.description = description;
        return this;
    }

    public VideoBuilder thumbMediaId(String thumb_media_id) {
        this.thumbMediaId = thumb_media_id;
        return this;
    }

    public WxMpCustomMessage build() {
        WxMpCustomMessage m = super.build();
        m.setMediaId(this.mediaId);
        m.setTitle(this.title);
        m.setDescription(this.description);
        m.setThumbMediaId(this.thumbMediaId);
        return m;
    }
}

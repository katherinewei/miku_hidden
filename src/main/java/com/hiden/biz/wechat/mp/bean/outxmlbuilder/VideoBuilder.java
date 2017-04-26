
package com.hiden.biz.wechat.mp.bean.outxmlbuilder;

import com.hiden.biz.wechat.mp.bean.WxMpXmlOutVideoMessage;

public final class VideoBuilder extends BaseBuilder<VideoBuilder, WxMpXmlOutVideoMessage> {
    private String mediaId;
    private String title;
    private String description;

    public VideoBuilder() {
    }

    public VideoBuilder title(String title) {
        this.title = title;
        return this;
    }

    public VideoBuilder description(String description) {
        this.description = description;
        return this;
    }

    public VideoBuilder mediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public WxMpXmlOutVideoMessage build() {
        WxMpXmlOutVideoMessage m = new WxMpXmlOutVideoMessage();
        this.setCommon(m);
        m.setTitle(this.title);
        m.setDescription(this.description);
        m.setMediaId(this.mediaId);
        return m;
    }
}

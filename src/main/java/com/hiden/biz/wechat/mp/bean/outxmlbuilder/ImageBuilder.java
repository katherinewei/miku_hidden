
package com.hiden.biz.wechat.mp.bean.outxmlbuilder;

import com.hiden.biz.wechat.mp.bean.WxMpXmlOutImageMessage;

public final class ImageBuilder extends BaseBuilder<ImageBuilder, WxMpXmlOutImageMessage> {
    private String mediaId;

    public ImageBuilder() {
    }

    public ImageBuilder mediaId(String media_id) {
        this.mediaId = media_id;
        return this;
    }

    public WxMpXmlOutImageMessage build() {
        WxMpXmlOutImageMessage m = new WxMpXmlOutImageMessage();
        this.setCommon(m);
        m.setMediaId(this.mediaId);
        return m;
    }
}

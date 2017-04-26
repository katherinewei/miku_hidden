
package com.hiden.biz.wechat.mp.bean.custombuilder;

import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage;

public final class ImageBuilder extends BaseBuilder<ImageBuilder> {
    private String mediaId;

    public ImageBuilder() {
        this.msgType = "image";
    }

    public ImageBuilder mediaId(String media_id) {
        this.mediaId = media_id;
        return this;
    }

    public WxMpCustomMessage build() {
        WxMpCustomMessage m = super.build();
        m.setMediaId(this.mediaId);
        return m;
    }
}

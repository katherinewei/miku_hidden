package com.hiden.biz.wechat.mp.bean.custombuilder;

import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage;

public final class VoiceBuilder extends BaseBuilder<VoiceBuilder> {
    private String mediaId;

    public VoiceBuilder() {
        this.msgType = "voice";
    }

    public VoiceBuilder mediaId(String media_id) {
        this.mediaId = media_id;
        return this;
    }

    public WxMpCustomMessage build() {
        WxMpCustomMessage m = super.build();
        m.setMediaId(this.mediaId);
        return m;
    }
}

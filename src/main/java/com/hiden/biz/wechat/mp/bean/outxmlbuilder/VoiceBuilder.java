
package com.hiden.biz.wechat.mp.bean.outxmlbuilder;

import com.hiden.biz.wechat.mp.bean.WxMpXmlOutVoiceMessage;

public final class VoiceBuilder extends BaseBuilder<VoiceBuilder, WxMpXmlOutVoiceMessage> {
    private String mediaId;

    public VoiceBuilder() {
    }

    public VoiceBuilder mediaId(String mediaId) {
        this.mediaId = mediaId;
        return this;
    }

    public WxMpXmlOutVoiceMessage build() {
        WxMpXmlOutVoiceMessage m = new WxMpXmlOutVoiceMessage();
        this.setCommon(m);
        m.setMediaId(this.mediaId);
        return m;
    }
}

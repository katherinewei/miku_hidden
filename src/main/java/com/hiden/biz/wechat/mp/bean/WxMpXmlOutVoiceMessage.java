package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.common.util.xml.XStreamMediaIdConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("xml")
public class WxMpXmlOutVoiceMessage extends WxMpXmlOutMessage {
    @XStreamAlias("Voice")
    @XStreamConverter(XStreamMediaIdConverter.class)
    private String mediaId;

    public WxMpXmlOutVoiceMessage() {
        this.msgType = "voice";
    }

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}

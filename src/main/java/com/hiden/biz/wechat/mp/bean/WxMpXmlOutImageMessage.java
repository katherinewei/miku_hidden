package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.common.util.xml.XStreamMediaIdConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("xml")
public class WxMpXmlOutImageMessage extends WxMpXmlOutMessage {
    @XStreamAlias("Image")
    @XStreamConverter(XStreamMediaIdConverter.class)
    private String mediaId;

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public WxMpXmlOutImageMessage() {
        this.msgType = "image";
    }
}

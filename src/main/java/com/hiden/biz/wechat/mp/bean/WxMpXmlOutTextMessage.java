package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.common.util.xml.XStreamCDataConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("xml")
public class WxMpXmlOutTextMessage extends WxMpXmlOutMessage {
    @XStreamAlias("Content")
    @XStreamConverter(XStreamCDataConverter.class)
    private String content;

    public WxMpXmlOutTextMessage() {
        this.msgType = "text";
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.common.util.xml.XStreamCDataConverter;
import com.hiden.biz.wechat.mp.api.WxMpConfigStorage;
import com.hiden.biz.wechat.mp.bean.outxmlbuilder.ImageBuilder;
import com.hiden.biz.wechat.mp.bean.outxmlbuilder.MusicBuilder;
import com.hiden.biz.wechat.mp.bean.outxmlbuilder.NewsBuilder;
import com.hiden.biz.wechat.mp.bean.outxmlbuilder.TextBuilder;
import com.hiden.biz.wechat.mp.bean.outxmlbuilder.VideoBuilder;
import com.hiden.biz.wechat.mp.bean.outxmlbuilder.VoiceBuilder;
import com.hiden.biz.wechat.mp.util.crypto.WxMpCryptUtil;
import com.hiden.biz.wechat.mp.util.xml.XStreamTransformer;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import java.io.Serializable;

@XStreamAlias("xml")
public abstract class WxMpXmlOutMessage implements Serializable {
    @XStreamAlias("ToUserName")
    @XStreamConverter(XStreamCDataConverter.class)
    protected String toUserName;
    @XStreamAlias("FromUserName")
    @XStreamConverter(XStreamCDataConverter.class)
    protected String fromUserName;
    @XStreamAlias("CreateTime")
    protected Long createTime;
    @XStreamAlias("MsgType")
    @XStreamConverter(XStreamCDataConverter.class)
    protected String msgType;

    public WxMpXmlOutMessage() {
    }

    public String getToUserName() {
        return this.toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return this.fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String toXml() {
        return XStreamTransformer.toXml(this.getClass(), this);
    }

    public String toEncryptedXml(WxMpConfigStorage wxMpConfigStorage) {
        String plainXml = this.toXml();
        WxMpCryptUtil pc = new WxMpCryptUtil(wxMpConfigStorage);
        return pc.encrypt(plainXml);
    }

    public static TextBuilder TEXT() {
        return new TextBuilder();
    }

    public static ImageBuilder IMAGE() {
        return new ImageBuilder();
    }

    public static VoiceBuilder VOICE() {
        return new VoiceBuilder();
    }

    public static VideoBuilder VIDEO() {
        return new VideoBuilder();
    }

    public static MusicBuilder MUSIC() {
        return new MusicBuilder();
    }

    public static NewsBuilder NEWS() {
        return new NewsBuilder();
    }
}

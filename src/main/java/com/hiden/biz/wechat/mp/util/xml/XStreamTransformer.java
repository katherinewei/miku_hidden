package com.hiden.biz.wechat.mp.util.xml;

import com.hiden.biz.wechat.common.util.xml.XStreamInitializer;
import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutImageMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutMusicMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutNewsMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutTextMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutVideoMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutVoiceMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage.ScanCodeInfo;
import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage.SendLocationInfo;
import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage.SendPicsInfo;
import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage.SendPicsInfo.Item;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutMusicMessage.Music;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutVideoMessage.Video;
import com.thoughtworks.xstream.XStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XStreamTransformer {
    protected static final Map<Class, XStream> CLASS_2_XSTREAM_INSTANCE = configXStreamInstance();

    public XStreamTransformer() {
    }

    public static <T> T fromXml(Class<T> clazz, String xml) {
        Object object = ((XStream)CLASS_2_XSTREAM_INSTANCE.get(clazz)).fromXML(xml);
        return (T)object;
    }

    public static <T> T fromXml(Class<T> clazz, InputStream is) {
        Object object = ((XStream)CLASS_2_XSTREAM_INSTANCE.get(clazz)).fromXML(is);
        return (T)object;
    }

    public static String toXml(Class<? extends WxMpXmlOutMessage> clazz, WxMpXmlOutMessage object) {
        return ((XStream)CLASS_2_XSTREAM_INSTANCE.get(clazz)).toXML(object);
    }

    private static Map<Class, XStream> configXStreamInstance() {
        HashMap map = new HashMap();
        map.put(WxMpXmlMessage.class, config_WxMpXmlMessage());
        map.put(WxMpXmlOutMusicMessage.class, config_WxMpXmlOutMusicMessage());
        map.put(WxMpXmlOutNewsMessage.class, config_WxMpXmlOutNewsMessage());
        map.put(WxMpXmlOutTextMessage.class, config_WxMpXmlOutTextMessage());
        map.put(WxMpXmlOutImageMessage.class, config_WxMpXmlOutImageMessage());
        map.put(WxMpXmlOutVideoMessage.class, config_WxMpXmlOutVideoMessage());
        map.put(WxMpXmlOutVoiceMessage.class, config_WxMpXmlOutVoiceMessage());
        return map;
    }

    private static XStream config_WxMpXmlMessage() {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxMpXmlMessage.class);
        xstream.processAnnotations(ScanCodeInfo.class);
        xstream.processAnnotations(SendPicsInfo.class);
        xstream.processAnnotations(Item.class);
        xstream.processAnnotations(SendLocationInfo.class);
        xstream.aliasField("MsgID", WxMpXmlMessage.class, "msgId");
        return xstream;
    }

    private static XStream config_WxMpXmlOutImageMessage() {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxMpXmlOutMessage.class);
        xstream.processAnnotations(WxMpXmlOutImageMessage.class);
        return xstream;
    }

    private static XStream config_WxMpXmlOutNewsMessage() {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxMpXmlOutMessage.class);
        xstream.processAnnotations(WxMpXmlOutNewsMessage.class);
        xstream.processAnnotations(com.hiden.biz.wechat.mp.bean.WxMpXmlOutNewsMessage.Item.class);
        return xstream;
    }

    private static XStream config_WxMpXmlOutMusicMessage() {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxMpXmlOutMessage.class);
        xstream.processAnnotations(WxMpXmlOutMusicMessage.class);
        xstream.processAnnotations(Music.class);
        return xstream;
    }

    private static XStream config_WxMpXmlOutTextMessage() {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxMpXmlOutMessage.class);
        xstream.processAnnotations(WxMpXmlOutTextMessage.class);
        return xstream;
    }

    private static XStream config_WxMpXmlOutVideoMessage() {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxMpXmlOutMessage.class);
        xstream.processAnnotations(WxMpXmlOutVideoMessage.class);
        xstream.processAnnotations(Video.class);
        return xstream;
    }

    private static XStream config_WxMpXmlOutVoiceMessage() {
        XStream xstream = XStreamInitializer.getInstance();
        xstream.processAnnotations(WxMpXmlOutMessage.class);
        xstream.processAnnotations(WxMpXmlOutVoiceMessage.class);
        return xstream;
    }
}

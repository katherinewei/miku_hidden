package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.common.util.xml.XStreamCDataConverter;
import com.hiden.biz.wechat.mp.api.WxMpConfigStorage;
import com.hiden.biz.wechat.mp.util.crypto.WxMpCryptUtil;
import com.hiden.biz.wechat.mp.util.xml.XStreamTransformer;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;

@XStreamAlias("xml")
public class WxMpXmlMessage implements Serializable {
    @XStreamAlias("ToUserName")
    @XStreamConverter(XStreamCDataConverter.class)
    private String toUserName;
    @XStreamAlias("FromUserName")
    @XStreamConverter(XStreamCDataConverter.class)
    private String fromUserName;
    @XStreamAlias("CreateTime")
    private Long createTime;
    @XStreamAlias("MsgType")
    @XStreamConverter(XStreamCDataConverter.class)
    private String msgType;
    @XStreamAlias("Content")
    @XStreamConverter(XStreamCDataConverter.class)
    private String content;
    @XStreamAlias("MsgId")
    private Long msgId;
    @XStreamAlias("PicUrl")
    @XStreamConverter(XStreamCDataConverter.class)
    private String picUrl;
    @XStreamAlias("MediaId")
    @XStreamConverter(XStreamCDataConverter.class)
    private String mediaId;
    @XStreamAlias("Format")
    @XStreamConverter(XStreamCDataConverter.class)
    private String format;
    @XStreamAlias("ThumbMediaId")
    @XStreamConverter(XStreamCDataConverter.class)
    private String thumbMediaId;
    @XStreamAlias("Location_X")
    private Double locationX;
    @XStreamAlias("Location_Y")
    private Double locationY;
    @XStreamAlias("Scale")
    private Double scale;
    @XStreamAlias("Label")
    @XStreamConverter(XStreamCDataConverter.class)
    private String label;
    @XStreamAlias("Title")
    @XStreamConverter(XStreamCDataConverter.class)
    private String title;
    @XStreamAlias("Description")
    @XStreamConverter(XStreamCDataConverter.class)
    private String description;
    @XStreamAlias("Url")
    @XStreamConverter(XStreamCDataConverter.class)
    private String url;
    @XStreamAlias("Event")
    @XStreamConverter(XStreamCDataConverter.class)
    private String event;
    @XStreamAlias("EventKey")
    @XStreamConverter(XStreamCDataConverter.class)
    private String eventKey;
    @XStreamAlias("Ticket")
    @XStreamConverter(XStreamCDataConverter.class)
    private String ticket;
    @XStreamAlias("Latitude")
    private Double latitude;
    @XStreamAlias("Longitude")
    private Double longitude;
    @XStreamAlias("Precision")
    private Double precision;
    @XStreamAlias("Recognition")
    @XStreamConverter(XStreamCDataConverter.class)
    private String recognition;
    @XStreamAlias("Status")
    @XStreamConverter(XStreamCDataConverter.class)
    private String status;
    @XStreamAlias("TotalCount")
    private Integer totalCount;
    @XStreamAlias("FilterCount")
    private Integer filterCount;
    @XStreamAlias("SentCount")
    private Integer sentCount;
    @XStreamAlias("ErrorCount")
    private Integer errorCount;
    @XStreamAlias("ScanCodeInfo")
    private WxMpXmlMessage.ScanCodeInfo scanCodeInfo = new WxMpXmlMessage.ScanCodeInfo();
    @XStreamAlias("SendPicsInfo")
    private WxMpXmlMessage.SendPicsInfo sendPicsInfo = new WxMpXmlMessage.SendPicsInfo();
    @XStreamAlias("SendLocationInfo")
    private WxMpXmlMessage.SendLocationInfo sendLocationInfo = new WxMpXmlMessage.SendLocationInfo();

    public WxMpXmlMessage() {
    }

    public String getToUserName() {
        return this.toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
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

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMsgId() {
        return this.msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getThumbMediaId() {
        return this.thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public Double getLocationX() {
        return this.locationX;
    }

    public void setLocationX(Double locationX) {
        this.locationX = locationX;
    }

    public Double getLocationY() {
        return this.locationY;
    }

    public void setLocationY(Double locationY) {
        this.locationY = locationY;
    }

    public Double getScale() {
        return this.scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return this.eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getTicket() {
        return this.ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getPrecision() {
        return this.precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public String getRecognition() {
        return this.recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    public String getFromUserName() {
        return this.fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public static WxMpXmlMessage fromXml(String xml) {
        return (WxMpXmlMessage)XStreamTransformer.fromXml(WxMpXmlMessage.class, xml);
    }

    public static WxMpXmlMessage fromXml(InputStream is) {
        return (WxMpXmlMessage)XStreamTransformer.fromXml(WxMpXmlMessage.class, is);
    }

    public static WxMpXmlMessage fromEncryptedXml(String encryptedXml, WxMpConfigStorage wxMpConfigStorage, String timestamp, String nonce, String msgSignature) {
        WxMpCryptUtil cryptUtil = new WxMpCryptUtil(wxMpConfigStorage);
        String plainText = cryptUtil.decrypt(msgSignature, timestamp, nonce, encryptedXml);
        return fromXml(plainText);
    }

    public static synchronized WxMpXmlMessage fromEncryptedXml(String encryptedXml, WxMpConfigStorage wxMpConfigStorage, String timestamp, String nonce, String msgSignature, String mpTag) {
        WxMpCryptUtil cryptUtil = new WxMpCryptUtil(wxMpConfigStorage, mpTag);
        String plainText = cryptUtil.decrypt(msgSignature, timestamp, nonce, encryptedXml);
        return fromXml(plainText);
    }

    public static synchronized WxMpXmlMessage fromEncryptedXml(InputStream is, WxMpConfigStorage wxMpConfigStorage, String timestamp, String nonce, String msgSignature) {
        try {
            return fromEncryptedXml(IOUtils.toString(is, "UTF-8"), wxMpConfigStorage, timestamp, nonce, msgSignature);
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    public static WxMpXmlMessage fromEncryptedXml(InputStream is, WxMpConfigStorage wxMpConfigStorage, String timestamp, String nonce, String msgSignature, String mpTag) {
        try {
            return fromEncryptedXml(IOUtils.toString(is, "UTF-8"), wxMpConfigStorage, timestamp, nonce, msgSignature, mpTag);
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getFilterCount() {
        return this.filterCount;
    }

    public void setFilterCount(Integer filterCount) {
        this.filterCount = filterCount;
    }

    public Integer getSentCount() {
        return this.sentCount;
    }

    public void setSentCount(Integer sentCount) {
        this.sentCount = sentCount;
    }

    public Integer getErrorCount() {
        return this.errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public WxMpXmlMessage.ScanCodeInfo getScanCodeInfo() {
        return this.scanCodeInfo;
    }

    public void setScanCodeInfo(WxMpXmlMessage.ScanCodeInfo scanCodeInfo) {
        this.scanCodeInfo = scanCodeInfo;
    }

    public WxMpXmlMessage.SendPicsInfo getSendPicsInfo() {
        return this.sendPicsInfo;
    }

    public void setSendPicsInfo(WxMpXmlMessage.SendPicsInfo sendPicsInfo) {
        this.sendPicsInfo = sendPicsInfo;
    }

    public WxMpXmlMessage.SendLocationInfo getSendLocationInfo() {
        return this.sendLocationInfo;
    }

    public void setSendLocationInfo(WxMpXmlMessage.SendLocationInfo sendLocationInfo) {
        this.sendLocationInfo = sendLocationInfo;
    }

    public String toString() {
        return "WxMpXmlMessage{toUserName=\'" + this.toUserName + '\'' + ", fromUserName=\'" + this.fromUserName + '\'' + ", createTime=" + this.createTime + ", msgType=\'" + this.msgType + '\'' + ", content=\'" + this.content + '\'' + ", msgId=" + this.msgId + ", picUrl=\'" + this.picUrl + '\'' + ", mediaId=\'" + this.mediaId + '\'' + ", format=\'" + this.format + '\'' + ", thumbMediaId=\'" + this.thumbMediaId + '\'' + ", locationX=" + this.locationX + ", locationY=" + this.locationY + ", scale=" + this.scale + ", label=\'" + this.label + '\'' + ", title=\'" + this.title + '\'' + ", description=\'" + this.description + '\'' + ", url=\'" + this.url + '\'' + ", event=\'" + this.event + '\'' + ", eventKey=\'" + this.eventKey + '\'' + ", ticket=\'" + this.ticket + '\'' + ", latitude=" + this.latitude + ", longitude=" + this.longitude + ", precision=" + this.precision + ", recognition=\'" + this.recognition + '\'' + ", status=\'" + this.status + '\'' + ", totalCount=" + this.totalCount + ", filterCount=" + this.filterCount + ", sentCount=" + this.sentCount + ", errorCount=" + this.errorCount + ", scanCodeInfo=" + this.scanCodeInfo + ", sendPicsInfo=" + this.sendPicsInfo + ", sendLocationInfo=" + this.sendLocationInfo + '}';
    }

    @XStreamAlias("SendLocationInfo")
    public static class SendLocationInfo {
        @XStreamAlias("Location_X")
        @XStreamConverter(XStreamCDataConverter.class)
        private String locationX;
        @XStreamAlias("Location_Y")
        @XStreamConverter(XStreamCDataConverter.class)
        private String locationY;
        @XStreamAlias("Scale")
        @XStreamConverter(XStreamCDataConverter.class)
        private String scale;
        @XStreamAlias("Label")
        @XStreamConverter(XStreamCDataConverter.class)
        private String label;
        @XStreamAlias("Poiname")
        @XStreamConverter(XStreamCDataConverter.class)
        private String poiname;

        public SendLocationInfo() {
        }

        public String getLocationX() {
            return this.locationX;
        }

        public void setLocationX(String locationX) {
            this.locationX = locationX;
        }

        public String getLocationY() {
            return this.locationY;
        }

        public void setLocationY(String locationY) {
            this.locationY = locationY;
        }

        public String getScale() {
            return this.scale;
        }

        public void setScale(String scale) {
            this.scale = scale;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getPoiname() {
            return this.poiname;
        }

        public void setPoiname(String poiname) {
            this.poiname = poiname;
        }
    }

    @XStreamAlias("SendPicsInfo")
    public static class SendPicsInfo {
        @XStreamAlias("Count")
        private Long count;
        @XStreamAlias("PicList")
        protected final List<WxMpXmlMessage.SendPicsInfo.Item> picList = new ArrayList();

        public SendPicsInfo() {
        }

        public Long getCount() {
            return this.count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

        public List<WxMpXmlMessage.SendPicsInfo.Item> getPicList() {
            return this.picList;
        }

        @XStreamAlias("item")
        public static class Item {
            @XStreamAlias("PicMd5Sum")
            @XStreamConverter(XStreamCDataConverter.class)
            private String picMd5Sum;

            public Item() {
            }

            public String getPicMd5Sum() {
                return this.picMd5Sum;
            }

            public void setPicMd5Sum(String picMd5Sum) {
                this.picMd5Sum = picMd5Sum;
            }
        }
    }

    @XStreamAlias("ScanCodeInfo")
    public static class ScanCodeInfo {
        @XStreamAlias("ScanType")
        @XStreamConverter(XStreamCDataConverter.class)
        private String scanType;
        @XStreamAlias("ScanResult")
        @XStreamConverter(XStreamCDataConverter.class)
        private String scanResult;

        public ScanCodeInfo() {
        }

        public String getScanType() {
            return this.scanType;
        }

        public void setScanType(String scanType) {
            this.scanType = scanType;
        }

        public String getScanResult() {
            return this.scanResult;
        }

        public void setScanResult(String scanResult) {
            this.scanResult = scanResult;
        }
    }
}

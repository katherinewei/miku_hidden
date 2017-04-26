package com.hiden.biz.wechat.mp.bean;
import com.hiden.biz.wechat.common.util.xml.XStreamCDataConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

@XStreamAlias("xml")
public class WxMpXmlOutVideoMessage extends WxMpXmlOutMessage {
    @XStreamAlias("Video")
    protected final WxMpXmlOutVideoMessage.Video video = new WxMpXmlOutVideoMessage.Video();

    public WxMpXmlOutVideoMessage() {
        this.msgType = "video";
    }

    public String getMediaId() {
        return this.video.getMediaId();
    }

    public void setMediaId(String mediaId) {
        this.video.setMediaId(mediaId);
    }

    public String getTitle() {
        return this.video.getTitle();
    }

    public void setTitle(String title) {
        this.video.setTitle(title);
    }

    public String getDescription() {
        return this.video.getDescription();
    }

    public void setDescription(String description) {
        this.video.setDescription(description);
    }

    @XStreamAlias("Video")
    public static class Video {
        @XStreamAlias("MediaId")
        @XStreamConverter(XStreamCDataConverter.class)
        private String mediaId;
        @XStreamAlias("Title")
        @XStreamConverter(XStreamCDataConverter.class)
        private String title;
        @XStreamAlias("Description")
        @XStreamConverter(XStreamCDataConverter.class)
        private String description;

        public Video() {
        }

        public String getMediaId() {
            return this.mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
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
    }
}

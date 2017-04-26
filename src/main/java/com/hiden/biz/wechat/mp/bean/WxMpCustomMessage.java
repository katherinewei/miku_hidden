//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.mp.bean.custombuilder.ImageBuilder;
import com.hiden.biz.wechat.mp.bean.custombuilder.MusicBuilder;
import com.hiden.biz.wechat.mp.bean.custombuilder.NewsBuilder;
import com.hiden.biz.wechat.mp.bean.custombuilder.TextBuilder;
import com.hiden.biz.wechat.mp.bean.custombuilder.VideoBuilder;
import com.hiden.biz.wechat.mp.bean.custombuilder.VoiceBuilder;
import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMpCustomMessage implements Serializable {
    private String toUser;
    private String msgType;
    private String content;
    private String mediaId;
    private String thumbMediaId;
    private String title;
    private String description;
    private String musicUrl;
    private String hqMusicUrl;
    private List<WxMpCustomMessage.WxArticle> articles = new ArrayList();

    public WxMpCustomMessage() {
    }

    public String getToUser() {
        return this.toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
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

    public String getMediaId() {
        return this.mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbMediaId() {
        return this.thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
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

    public String getMusicUrl() {
        return this.musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getHqMusicUrl() {
        return this.hqMusicUrl;
    }

    public void setHqMusicUrl(String hqMusicUrl) {
        this.hqMusicUrl = hqMusicUrl;
    }

    public List<WxMpCustomMessage.WxArticle> getArticles() {
        return this.articles;
    }

    public void setArticles(List<WxMpCustomMessage.WxArticle> articles) {
        this.articles = articles;
    }

    public String toJson() {
        return WxMpGsonBuilder.INSTANCE.create().toJson(this);
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

    public static class WxArticle {
        private String title;
        private String description;
        private String url;
        private String picUrl;

        public WxArticle() {
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

        public String getPicUrl() {
            return this.picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }
}

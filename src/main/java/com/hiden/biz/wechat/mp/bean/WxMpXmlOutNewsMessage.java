package com.hiden.biz.wechat.mp.bean;
import com.hiden.biz.wechat.common.util.xml.XStreamCDataConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("xml")
public class WxMpXmlOutNewsMessage extends WxMpXmlOutMessage {
    @XStreamAlias("ArticleCount")
    protected int articleCount;
    @XStreamAlias("Articles")
    protected final List<WxMpXmlOutNewsMessage.Item> articles = new ArrayList();

    public WxMpXmlOutNewsMessage() {
        this.msgType = "news";
    }

    public int getArticleCount() {
        return this.articleCount;
    }

    public void addArticle(WxMpXmlOutNewsMessage.Item item) {
        this.articles.add(item);
        this.articleCount = this.articles.size();
    }

    public List<WxMpXmlOutNewsMessage.Item> getArticles() {
        return this.articles;
    }

    @XStreamAlias("item")
    public static class Item {
        @XStreamAlias("Title")
        @XStreamConverter(XStreamCDataConverter.class)
        private String Title;
        @XStreamAlias("Description")
        @XStreamConverter(XStreamCDataConverter.class)
        private String Description;
        @XStreamAlias("PicUrl")
        @XStreamConverter(XStreamCDataConverter.class)
        private String PicUrl;
        @XStreamAlias("Url")
        @XStreamConverter(XStreamCDataConverter.class)
        private String Url;

        public Item() {
        }

        public String getTitle() {
            return this.Title;
        }

        public void setTitle(String title) {
            this.Title = title;
        }

        public String getDescription() {
            return this.Description;
        }

        public void setDescription(String description) {
            this.Description = description;
        }

        public String getPicUrl() {
            return this.PicUrl;
        }

        public void setPicUrl(String picUrl) {
            this.PicUrl = picUrl;
        }

        public String getUrl() {
            return this.Url;
        }

        public void setUrl(String url) {
            this.Url = url;
        }
    }
}

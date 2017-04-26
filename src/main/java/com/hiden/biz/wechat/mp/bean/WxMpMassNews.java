//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hiden.biz.wechat.mp.bean;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMpMassNews implements Serializable {
    private List<WxMpMassNews.WxMpMassNewsArticle> articles = new ArrayList();

    public WxMpMassNews() {
    }

    public List<WxMpMassNews.WxMpMassNewsArticle> getArticles() {
        return this.articles;
    }

    public void addArticle(WxMpMassNews.WxMpMassNewsArticle article) {
        this.articles.add(article);
    }

    public String toJson() {
        return WxMpGsonBuilder.INSTANCE.create().toJson(this);
    }

    public static class WxMpMassNewsArticle {
        private String thumbMediaId;
        private String author;
        private String title;
        private String contentSourceUrl;
        private String content;
        private String digest;
        private boolean showCoverPic;

        public WxMpMassNewsArticle() {
        }

        public String getThumbMediaId() {
            return this.thumbMediaId;
        }

        public void setThumbMediaId(String thumbMediaId) {
            this.thumbMediaId = thumbMediaId;
        }

        public String getAuthor() {
            return this.author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContentSourceUrl() {
            return this.contentSourceUrl;
        }

        public void setContentSourceUrl(String contentSourceUrl) {
            this.contentSourceUrl = contentSourceUrl;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDigest() {
            return this.digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public boolean isShowCoverPic() {
            return this.showCoverPic;
        }

        public void setShowCoverPic(boolean showCoverPic) {
            this.showCoverPic = showCoverPic;
        }
    }
}

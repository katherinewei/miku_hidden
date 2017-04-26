package com.hiden.biz.wechat.mp.bean.custombuilder;

import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage;
import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage.WxArticle;

import java.util.ArrayList;
import java.util.List;

public final class NewsBuilder extends BaseBuilder<NewsBuilder> {
    private List<WxArticle> articles = new ArrayList();

    public NewsBuilder() {
        this.msgType = "news";
    }

    public NewsBuilder addArticle(WxArticle article) {
        this.articles.add(article);
        return this;
    }

    public WxMpCustomMessage build() {
        WxMpCustomMessage m = super.build();
        m.setArticles(this.articles);
        return m;
    }
}

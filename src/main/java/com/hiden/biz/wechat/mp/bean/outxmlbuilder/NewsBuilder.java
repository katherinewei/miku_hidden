
package com.hiden.biz.wechat.mp.bean.outxmlbuilder;

import com.hiden.biz.wechat.mp.bean.WxMpXmlOutNewsMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutNewsMessage.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class NewsBuilder extends BaseBuilder<NewsBuilder, WxMpXmlOutNewsMessage> {
    protected final List<Item> articles = new ArrayList();

    public NewsBuilder() {
    }

    public NewsBuilder addArticle(Item item) {
        this.articles.add(item);
        return this;
    }

    public WxMpXmlOutNewsMessage build() {
        WxMpXmlOutNewsMessage m = new WxMpXmlOutNewsMessage();
        Iterator i$ = this.articles.iterator();

        while(i$.hasNext()) {
            Item item = (Item)i$.next();
            m.addArticle(item);
        }

        this.setCommon(m);
        return m;
    }
}

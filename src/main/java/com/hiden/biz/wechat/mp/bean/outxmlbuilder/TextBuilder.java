
package com.hiden.biz.wechat.mp.bean.outxmlbuilder;

import com.hiden.biz.wechat.mp.bean.WxMpXmlOutTextMessage;

public final class TextBuilder extends BaseBuilder<TextBuilder, WxMpXmlOutTextMessage> {
    private String content;

    public TextBuilder() {
    }

    public TextBuilder content(String content) {
        this.content = content;
        return this;
    }

    public WxMpXmlOutTextMessage build() {
        WxMpXmlOutTextMessage m = new WxMpXmlOutTextMessage();
        this.setCommon(m);
        m.setContent(this.content);
        return m;
    }
}

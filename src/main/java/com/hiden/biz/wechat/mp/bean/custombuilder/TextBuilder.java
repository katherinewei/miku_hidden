package com.hiden.biz.wechat.mp.bean.custombuilder;

import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage;

public final class TextBuilder extends BaseBuilder<TextBuilder> {
    private String content;

    public TextBuilder() {
        this.msgType = "text";
    }

    public TextBuilder content(String content) {
        this.content = content;
        return this;
    }

    public WxMpCustomMessage build() {
        WxMpCustomMessage m = super.build();
        m.setContent(this.content);
        return m;
    }
}
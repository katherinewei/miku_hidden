
package com.hiden.biz.wechat.mp.bean.custombuilder;

import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage;

public class BaseBuilder<T> {
    protected String msgType;
    protected String toUser;

    public BaseBuilder() {
    }

    public BaseBuilder<T> toUser(String toUser) {
        this.toUser = toUser;
        return this;
    }

    public WxMpCustomMessage build() {
        WxMpCustomMessage m = new WxMpCustomMessage();
        m.setMsgType(this.msgType);
        m.setToUser(this.toUser);
        return m;
    }
}

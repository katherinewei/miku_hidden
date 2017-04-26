
package com.hiden.biz.wechat.mp.bean.outxmlbuilder;

import com.hiden.biz.wechat.mp.bean.WxMpXmlOutMessage;

public abstract class BaseBuilder<BuilderType, ValueType> {
    protected String toUserName;
    protected String fromUserName;

    public BaseBuilder() {
    }

    public BaseBuilder<BuilderType, ValueType> toUser(String touser) {
        this.toUserName = touser;
        return this;
    }

    public BaseBuilder<BuilderType, ValueType> fromUser(String fromusername) {
        this.fromUserName = fromusername;
        return this;
    }

    public abstract ValueType build();

    public void setCommon(WxMpXmlOutMessage m) {
        m.setToUserName(this.toUserName);
        m.setFromUserName(this.fromUserName);
        m.setCreateTime(Long.valueOf(System.currentTimeMillis() / 1000L));
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hiden.biz.wechat.common.session;

import java.util.Enumeration;

public class StandardSessionFacade implements WxSession {
    private WxSession session = null;

    public StandardSessionFacade(StandardSession session) {
        this.session = session;
    }

    public InternalSession getInternalSession() {
        return (InternalSession)this.session;
    }

    public Object getAttribute(String name) {
        return this.session.getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return this.session.getAttributeNames();
    }

    public void setAttribute(String name, Object value) {
        this.session.setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        this.session.removeAttribute(name);
    }

    public void invalidate() {
        this.session.invalidate();
    }
}

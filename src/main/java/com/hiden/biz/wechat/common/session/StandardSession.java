
package com.hiden.biz.wechat.common.session;

import com.hiden.biz.wechat.common.util.res.StringManager;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StandardSession implements WxSession, InternalSession {
    protected static final StringManager sm = StringManager.getManager("com.daniel.weixin.common.session");
    protected Map<String, Object> attributes = new ConcurrentHashMap();
    protected String id = null;
    protected volatile boolean isValid = false;
    protected transient volatile boolean expiring = false;
    protected transient InternalSessionManager manager = null;
    protected static final String[] EMPTY_ARRAY = new String[0];
    protected long creationTime = 0L;
    protected volatile long thisAccessedTime;
    protected int maxInactiveInterval;
    protected transient StandardSessionFacade facade;
    protected transient AtomicInteger accessCount;

    public Object getAttribute(String name) {
        if (!this.isValidInternal()) {
            throw new IllegalStateException(sm.getString("sessionImpl.getAttribute.ise"));
        } else {
            return name == null ? null : this.attributes.get(name);
        }
    }

    public Enumeration<String> getAttributeNames() {
        if (!this.isValidInternal()) {
            throw new IllegalStateException(sm.getString("sessionImpl.getAttributeNames.ise"));
        } else {
            HashSet names = new HashSet();
            names.addAll(this.attributes.keySet());
            return Collections.enumeration(names);
        }
    }

    public void setAttribute(String name, Object value) {
        if (name == null) {
            throw new IllegalArgumentException(sm.getString("sessionImpl.setAttribute.namenull"));
        } else if (value == null) {
            this.removeAttribute(name);
        } else if (!this.isValidInternal()) {
            throw new IllegalStateException(sm.getString("sessionImpl.setAttribute.ise", new Object[]{this.getIdInternal()}));
        } else {
            this.attributes.put(name, value);
        }
    }

    public void removeAttribute(String name) {
        this.removeAttributeInternal(name);
    }

    public void invalidate() {
        if (!this.isValidInternal()) {
            throw new IllegalStateException(sm.getString("sessionImpl.invalidate.ise"));
        } else {
            this.expire();
        }
    }

    public StandardSession(InternalSessionManager manager) {
        this.thisAccessedTime = this.creationTime;
        this.maxInactiveInterval = 1800;
        this.facade = null;
        this.accessCount = null;
        this.manager = manager;
        this.accessCount = new AtomicInteger();
    }

    public WxSession getSession() {
        if (this.facade == null) {
            this.facade = new StandardSessionFacade(this);
        }

        return this.facade;
    }

    protected boolean isValidInternal() {
        return this.isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean isValid() {
        if (!this.isValid) {
            return false;
        } else if (this.expiring) {
            return true;
        } else if (this.accessCount.get() > 0) {
            return true;
        } else {
            if (this.maxInactiveInterval > 0) {
                long timeNow = System.currentTimeMillis();
                int timeIdle = (int) ((timeNow - this.thisAccessedTime) / 1000L);
                if (timeIdle >= this.maxInactiveInterval) {
                    this.expire();
                }
            }

            return this.isValid;
        }
    }

    public String getIdInternal() {
        return this.id;
    }

    protected void removeAttributeInternal(String name) {
        if (name != null) {
            this.attributes.remove(name);
        }
    }

    public void expire() {
        if (this.isValid) {
            synchronized (this) {
                if (!this.expiring && this.isValid) {
                    if (this.manager != null) {
                        this.expiring = true;
                        this.accessCount.set(0);
                        this.manager.remove(this, true);
                        this.setValid(false);
                        this.expiring = false;
                        String[] keys = this.keys();

                        for (int i = 0; i < keys.length; ++i) {
                            this.removeAttributeInternal(keys[i]);
                        }

                    }
                }
            }
        }
    }

    public void access() {
        this.thisAccessedTime = System.currentTimeMillis();
        this.accessCount.incrementAndGet();
    }

    public void endAccess() {
        this.thisAccessedTime = System.currentTimeMillis();
        this.accessCount.decrementAndGet();
    }

    public void setCreationTime(long time) {
        this.creationTime = time;
        this.thisAccessedTime = time;
    }

    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    public void setId(String id) {
        if (this.id != null && this.manager != null) {
            this.manager.remove(this);
        }

        this.id = id;
        if (this.manager != null) {
            this.manager.add(this);
        }

    }

    protected String[] keys() {
        return (String[]) this.attributes.keySet().toArray(EMPTY_ARRAY);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof StandardSession)) {
            return false;
        } else {
            StandardSession session = (StandardSession) o;
            return this.creationTime != session.creationTime ? false : (this.expiring != session.expiring ? false : (this.isValid != session.isValid ? false : (this.maxInactiveInterval != session.maxInactiveInterval ? false : (this.thisAccessedTime != session.thisAccessedTime ? false : (!this.accessCount.equals(session.accessCount) ? false : (!this.attributes.equals(session.attributes) ? false : (!this.facade.equals(session.facade) ? false : (!this.id.equals(session.id) ? false : this.manager.equals(session.manager)))))))));
        }
    }

    public int hashCode() {
        int result = this.attributes.hashCode();
        result = 31 * result + this.id.hashCode();
        result = 31 * result + (this.isValid ? 1 : 0);
        result = 31 * result + (this.expiring ? 1 : 0);
        result = 31 * result + this.manager.hashCode();
        result = 31 * result + (int) (this.creationTime ^ this.creationTime >>> 32);
        result = 31 * result + (int) (this.thisAccessedTime ^ this.thisAccessedTime >>> 32);
        result = 31 * result + this.maxInactiveInterval;
        result = 31 * result + this.facade.hashCode();
        result = 31 * result + this.accessCount.hashCode();
        return result;
    }
}

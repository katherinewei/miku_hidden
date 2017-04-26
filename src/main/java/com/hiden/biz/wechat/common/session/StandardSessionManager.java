
package com.hiden.biz.wechat.common.session;

import com.hiden.biz.wechat.common.util.res.StringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class StandardSessionManager implements WxSessionManager, InternalSessionManager {
    protected final Logger log = LoggerFactory.getLogger(StandardSessionManager.class);
    protected static final StringManager sm = StringManager.getManager("com.daniel.weixin.common.session");
    protected Map<String, InternalSession> sessions = new ConcurrentHashMap();
    private static final String name = "SessionManagerImpl";
    protected int maxActiveSessions = -1;
    protected int rejectedSessions = 0;
    protected int maxInactiveInterval = 1800;
    protected long sessionCounter = 0L;
    protected volatile int maxActive = 0;
    private final Object maxActiveUpdateLock = new Object();
    protected long processingTime = 0L;
    private int count = 0;
    protected int processExpiresFrequency = 6;
    protected int backgroundProcessorDelay = 10;
    private final AtomicBoolean backgroundProcessStarted = new AtomicBoolean(false);

    public StandardSessionManager() {
    }

    public WxSession getSession(String sessionId) {
        return this.getSession(sessionId, true);
    }

    public WxSession getSession(String sessionId, boolean create) {
        if (sessionId == null) {
            throw new IllegalStateException(sm.getString("sessionManagerImpl.getSession.ise"));
        } else {
            InternalSession session = this.findSession(sessionId);
            if (session != null && !session.isValid()) {
                session = null;
            }

            if (session != null) {
                session.access();
                return session.getSession();
            } else if (!create) {
                return null;
            } else {
                session = this.createSession(sessionId);
                if (session == null) {
                    return null;
                } else {
                    session.access();
                    return session.getSession();
                }
            }
        }
    }

    public void remove(InternalSession session) {
        this.remove(session, false);
    }

    public void remove(InternalSession session, boolean update) {
        if (session.getIdInternal() != null) {
            this.sessions.remove(session.getIdInternal());
        }

    }

    public InternalSession findSession(String id) {
        return id == null ? null : (InternalSession) this.sessions.get(id);
    }

    public InternalSession createSession(String sessionId) {
        if (sessionId == null) {
            throw new IllegalStateException(sm.getString("sessionManagerImpl.createSession.ise"));
        } else if (this.maxActiveSessions >= 0 && this.getActiveSessions() >= this.maxActiveSessions) {
            ++this.rejectedSessions;
            throw new TooManyActiveSessionsException(sm.getString("sessionManagerImpl.createSession.tmase"), this.maxActiveSessions);
        } else {
            InternalSession session = this.createEmptySession();
            session.setValid(true);
            session.setCreationTime(System.currentTimeMillis());
            session.setMaxInactiveInterval(this.maxInactiveInterval);
            session.setId(sessionId);
            ++this.sessionCounter;
            return session;
        }
    }

    public int getActiveSessions() {
        return this.sessions.size();
    }

    public InternalSession createEmptySession() {
        return this.getNewSession();
    }

    protected InternalSession getNewSession() {
        return new StandardSession(this);
    }

    public void add(InternalSession session) {
        if (!this.backgroundProcessStarted.getAndSet(true)) {
            Thread size = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep((long) StandardSessionManager.this.backgroundProcessorDelay * 1000L);
                            StandardSessionManager.this.backgroundProcess();
                        } catch (InterruptedException var2) {
                            StandardSessionManager.this.log.error("SessionManagerImpl.backgroundProcess error", var2);
                        }
                    }
                }
            });
            size.setDaemon(true);
            size.start();
        }

        this.sessions.put(session.getIdInternal(), session);
        int size1 = this.getActiveSessions();
        if (size1 > this.maxActive) {
            Object var3 = this.maxActiveUpdateLock;
            synchronized (this.maxActiveUpdateLock) {
                if (size1 > this.maxActive) {
                    this.maxActive = size1;
                }
            }
        }

    }

    public InternalSession[] findSessions() {
        return (InternalSession[]) this.sessions.values().toArray(new InternalSession[0]);
    }

    public void backgroundProcess() {
        this.count = (this.count + 1) % this.processExpiresFrequency;
        if (this.count == 0) {
            this.processExpires();
        }

    }

    public void processExpires() {
        long timeNow = System.currentTimeMillis();
        InternalSession[] sessions = this.findSessions();
        int expireHere = 0;
        if (this.log.isDebugEnabled()) {
            this.log.debug("Start expire sessions {} at {} sessioncount {}", new Object[]{this.getName(), Long.valueOf(timeNow), Integer.valueOf(sessions.length)});
        }

        for (int timeEnd = 0; timeEnd < sessions.length; ++timeEnd) {
            if (sessions[timeEnd] != null && !sessions[timeEnd].isValid()) {
                ++expireHere;
            }
        }

        long var7 = System.currentTimeMillis();
        if (this.log.isDebugEnabled()) {
            this.log.debug("End expire sessions {} processingTime {} expired sessions: {}", new Object[]{this.getName(), Long.valueOf(var7 - timeNow), Integer.valueOf(expireHere)});
        }

        this.processingTime += var7 - timeNow;
    }

    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    public void setProcessExpiresFrequency(int processExpiresFrequency) {
        if (processExpiresFrequency > 0) {
            this.processExpiresFrequency = processExpiresFrequency;
        }
    }

    public void setBackgroundProcessorDelay(int backgroundProcessorDelay) {
        this.backgroundProcessorDelay = backgroundProcessorDelay;
    }

    public String getName() {
        return "SessionManagerImpl";
    }

    public void setMaxActiveSessions(int max) {
        this.maxActiveSessions = max;
    }
}

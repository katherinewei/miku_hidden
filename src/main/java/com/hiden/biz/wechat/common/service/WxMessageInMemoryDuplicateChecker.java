
package com.hiden.biz.wechat.common.service;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class WxMessageInMemoryDuplicateChecker implements WxMessageDuplicateChecker {
    private final Long timeToLive;
    private final Long clearPeriod;
    private final ConcurrentHashMap<String, Long> msgId2Timestamp = new ConcurrentHashMap();
    private static AtomicBoolean backgroundProcessStarted = new AtomicBoolean(false);

    public WxMessageInMemoryDuplicateChecker() {
        this.timeToLive = Long.valueOf(15000L);
        this.clearPeriod = Long.valueOf(5000L);
    }

    public WxMessageInMemoryDuplicateChecker(Long timeToLive, Long clearPeriod) {
        this.timeToLive = timeToLive;
        this.clearPeriod = clearPeriod;
    }

    protected void checkBackgroundProcessStarted() {
        if (!backgroundProcessStarted.get()) {
            backgroundProcessStarted.set(true);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (true) {
                            Thread.sleep(WxMessageInMemoryDuplicateChecker.this.clearPeriod.longValue());
                            Long e = Long.valueOf(System.currentTimeMillis());
                            Iterator i$ = WxMessageInMemoryDuplicateChecker.this.msgId2Timestamp.entrySet().iterator();

                            while (i$.hasNext()) {
                                Entry entry = (Entry) i$.next();
                                if (e.longValue() - ((Long) entry.getValue()).longValue() > WxMessageInMemoryDuplicateChecker.this.timeToLive.longValue()) {
                                    WxMessageInMemoryDuplicateChecker.this.msgId2Timestamp.entrySet().remove(entry);
                                }
                            }
                        }
                    } catch (InterruptedException var4) {
                        var4.printStackTrace();
                    }
                }
            });
            t.setDaemon(true);
            t.start();
        }
    }

    public boolean isDuplicate(String messageId) {
        if (messageId == null) {
            return false;
        } else {
            this.checkBackgroundProcessStarted();
            Long timestamp = (Long) this.msgId2Timestamp.putIfAbsent(messageId, Long.valueOf(System.currentTimeMillis()));
            return timestamp != null;
        }
    }
}

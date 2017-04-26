
package com.hiden.biz.wechat.mp.api;

import com.hiden.biz.wechat.common.service.WxErrorExceptionHandler;
import com.hiden.biz.wechat.common.service.WxMessageDuplicateChecker;
import com.hiden.biz.wechat.common.service.WxMessageInMemoryDuplicateChecker;
import com.hiden.biz.wechat.common.session.InternalSession;
import com.hiden.biz.wechat.common.session.InternalSessionManager;
import com.hiden.biz.wechat.common.session.StandardSessionManager;
import com.hiden.biz.wechat.common.session.WxSessionManager;
import com.hiden.biz.wechat.common.util.LogExceptionHandler;
import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WxMpMessageRouter {
    protected final Logger log = LoggerFactory.getLogger(WxMpMessageRouter.class);
    private static final int DEFAULT_THREAD_POOL_SIZE = 100;
    private final List<WxMpMessageRouterRule> rules = new ArrayList();
    private final WxMpService wxMpService;
    private ExecutorService executorService;
    private WxMessageDuplicateChecker messageDuplicateChecker;
    private WxSessionManager sessionManager;
    private WxErrorExceptionHandler exceptionHandler;

    public WxMpMessageRouter(WxMpService wxMpService) {
        this.wxMpService = wxMpService;
        this.executorService = Executors.newFixedThreadPool(100);
        this.messageDuplicateChecker = new WxMessageInMemoryDuplicateChecker();
        this.sessionManager = new StandardSessionManager();
        this.exceptionHandler = new LogExceptionHandler();
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setMessageDuplicateChecker(WxMessageDuplicateChecker messageDuplicateChecker) {
        this.messageDuplicateChecker = messageDuplicateChecker;
    }

    public void setSessionManager(WxSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setExceptionHandler(WxErrorExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    List<WxMpMessageRouterRule> getRules() {
        return this.rules;
    }

    public WxMpMessageRouterRule rule() {
        return new WxMpMessageRouterRule(this);
    }

    public WxMpXmlOutMessage route(final WxMpXmlMessage wxMessage) {
        if(this.isDuplicateMessage(wxMessage)) {
            return null;
        } else {
            ArrayList matchRules = new ArrayList();
            Iterator res = this.rules.iterator();

            while(res.hasNext()) {
                WxMpMessageRouterRule futures = (WxMpMessageRouterRule)res.next();
                if(futures.test(wxMessage)) {
                    matchRules.add(futures);
                    if(!futures.isReEnter()) {
                        break;
                    }
                }
            }

            if(matchRules.size() == 0) {
                return null;
            } else {
                WxMpXmlOutMessage res1 = null;
                final ArrayList futures1 = new ArrayList();
                Iterator i$ = matchRules.iterator();

                while(i$.hasNext()) {
                    final WxMpMessageRouterRule rule = (WxMpMessageRouterRule)i$.next();
                    if(rule.isAsync()) {
                        futures1.add(this.executorService.submit(new Runnable() {
                            public void run() {
                                rule.service(wxMessage, WxMpMessageRouter.this.wxMpService, WxMpMessageRouter.this.sessionManager, WxMpMessageRouter.this.exceptionHandler);
                            }
                        }));
                    } else {
                        res1 = rule.service(wxMessage, this.wxMpService, this.sessionManager, this.exceptionHandler);
                        this.log.debug("End session access: async=false, sessionId={}", wxMessage.getFromUserName());
                        this.sessionEndAccess(wxMessage);
                    }
                }

                if(futures1.size() > 0) {
                    this.executorService.submit(new Runnable() {
                        public void run() {
                            Iterator i$ = futures1.iterator();

                            while(i$.hasNext()) {
                                Future future = (Future)i$.next();

                                try {
                                    future.get();
                                    WxMpMessageRouter.this.log.debug("End session access: async=true, sessionId={}", wxMessage.getFromUserName());
                                    WxMpMessageRouter.this.sessionEndAccess(wxMessage);
                                } catch (InterruptedException var4) {
                                    WxMpMessageRouter.this.log.error("Error happened when wait task finish", var4);
                                } catch (ExecutionException var5) {
                                    WxMpMessageRouter.this.log.error("Error happened when wait task finish", var5);
                                }
                            }

                        }
                    });
                }

                return res1;
            }
        }
    }

    protected boolean isDuplicateMessage(WxMpXmlMessage wxMessage) {
        String messageId = "";
        if(wxMessage.getMsgId() == null) {
            messageId = wxMessage.getCreateTime() + "-" + wxMessage.getFromUserName() + "-" + (wxMessage.getEventKey() == null?"":wxMessage.getEventKey()) + "-" + (wxMessage.getEvent() == null?"":wxMessage.getEvent());
        } else {
            messageId = String.valueOf(wxMessage.getMsgId());
        }

        return this.messageDuplicateChecker.isDuplicate(messageId);
    }

    protected void sessionEndAccess(WxMpXmlMessage wxMessage) {
        InternalSession session = ((InternalSessionManager)this.sessionManager).findSession(wxMessage.getFromUserName());
        if(session != null) {
            session.endAccess();
        }

    }
}


package com.hiden.biz.wechat.mp.api;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.service.WxErrorExceptionHandler;
import com.hiden.biz.wechat.common.session.WxSessionManager;
import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class WxMpMessageRouterRule {
    private final WxMpMessageRouter routerBuilder;
    private boolean async = true;
    private String fromUser;
    private String msgType;
    private String event;
    private String eventKey;
    private String content;
    private String rContent;
    private WxMpMessageMatcher matcher;
    private boolean reEnter = false;
    private List<WxMpMessageHandler> handlers = new ArrayList();
    private List<WxMpMessageInterceptor> interceptors = new ArrayList();

    public WxMpMessageRouterRule(WxMpMessageRouter routerBuilder) {
        this.routerBuilder = routerBuilder;
    }

    public WxMpMessageRouterRule async(boolean async) {
        this.async = async;
        return this;
    }

    public WxMpMessageRouterRule msgType(String msgType) {
        this.msgType = msgType;
        return this;
    }

    public WxMpMessageRouterRule event(String event) {
        this.event = event;
        return this;
    }

    public WxMpMessageRouterRule eventKey(String eventKey) {
        this.eventKey = eventKey;
        return this;
    }

    public WxMpMessageRouterRule content(String content) {
        this.content = content;
        return this;
    }

    public WxMpMessageRouterRule rContent(String regex) {
        this.rContent = regex;
        return this;
    }

    public WxMpMessageRouterRule fromUser(String fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    public WxMpMessageRouterRule matcher(WxMpMessageMatcher matcher) {
        this.matcher = matcher;
        return this;
    }

    public WxMpMessageRouterRule interceptor(WxMpMessageInterceptor interceptor) {
        return this.interceptor(interceptor, (WxMpMessageInterceptor[]) null);
    }

    public WxMpMessageRouterRule interceptor(WxMpMessageInterceptor interceptor, WxMpMessageInterceptor... otherInterceptors) {
        this.interceptors.add(interceptor);
        if(otherInterceptors != null && otherInterceptors.length > 0) {
            WxMpMessageInterceptor[] arr$ = otherInterceptors;
            int len$ = otherInterceptors.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                WxMpMessageInterceptor i = arr$[i$];
                this.interceptors.add(i);
            }
        }

        return this;
    }

    public WxMpMessageRouterRule handler(WxMpMessageHandler handler) {
        return this.handler(handler, (WxMpMessageHandler[]) null);
    }

    public WxMpMessageRouterRule handler(WxMpMessageHandler handler, WxMpMessageHandler... otherHandlers) {
        this.handlers.add(handler);
        if(otherHandlers != null && otherHandlers.length > 0) {
            WxMpMessageHandler[] arr$ = otherHandlers;
            int len$ = otherHandlers.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                WxMpMessageHandler i = arr$[i$];
                this.handlers.add(i);
            }
        }

        return this;
    }

    public WxMpMessageRouter end() {
        this.routerBuilder.getRules().add(this);
        return this.routerBuilder;
    }

    public WxMpMessageRouter next() {
        this.reEnter = true;
        return this.end();
    }

    protected boolean test(WxMpXmlMessage wxMessage) {
        return (this.fromUser == null || this.fromUser.equals(wxMessage.getFromUserName())) && (this.msgType == null || this.msgType.equals(wxMessage.getMsgType())) && (this.event == null || this.event.equals(wxMessage.getEvent())) && (this.eventKey == null || this.eventKey.equals(wxMessage.getEventKey())) && (this.content == null || this.content.equals(wxMessage.getContent() == null?null:wxMessage.getContent().trim())) && (this.rContent == null || Pattern.matches(this.rContent, wxMessage.getContent() == null?"":wxMessage.getContent().trim())) && (this.matcher == null || this.matcher.match(wxMessage));
    }

    protected WxMpXmlOutMessage service(WxMpXmlMessage wxMessage, WxMpService wxMpService, WxSessionManager sessionManager, WxErrorExceptionHandler exceptionHandler) {
        try {
            HashMap e = new HashMap();
            Iterator res = this.interceptors.iterator();

            while(res.hasNext()) {
                WxMpMessageInterceptor i$ = (WxMpMessageInterceptor)res.next();
                if(!i$.intercept(wxMessage, e, wxMpService, sessionManager)) {
                    return null;
                }
            }

            WxMpXmlOutMessage res1 = null;

            WxMpMessageHandler handler;
            for(Iterator i$1 = this.handlers.iterator(); i$1.hasNext(); res1 = handler.handle(wxMessage, e, wxMpService, sessionManager)) {
                handler = (WxMpMessageHandler)i$1.next();
            }

            return res1;
        } catch (WxErrorException var9) {
            exceptionHandler.handle(var9);
            return null;
        }
    }

    public WxMpMessageRouter getRouterBuilder() {
        return this.routerBuilder;
    }

    public boolean isAsync() {
        return this.async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getFromUser() {
        return this.fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getMsgType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return this.eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getrContent() {
        return this.rContent;
    }

    public void setrContent(String rContent) {
        this.rContent = rContent;
    }

    public WxMpMessageMatcher getMatcher() {
        return this.matcher;
    }

    public void setMatcher(WxMpMessageMatcher matcher) {
        this.matcher = matcher;
    }

    public boolean isReEnter() {
        return this.reEnter;
    }

    public void setReEnter(boolean reEnter) {
        this.reEnter = reEnter;
    }

    public List<WxMpMessageHandler> getHandlers() {
        return this.handlers;
    }

    public void setHandlers(List<WxMpMessageHandler> handlers) {
        this.handlers = handlers;
    }

    public List<WxMpMessageInterceptor> getInterceptors() {
        return this.interceptors;
    }

    public void setInterceptors(List<WxMpMessageInterceptor> interceptors) {
        this.interceptors = interceptors;
    }
}

package com.hiden.biz.wechat.mp.api;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.session.WxSessionManager;
import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage;
import com.hiden.biz.wechat.mp.bean.WxMpXmlOutMessage;
import java.util.Map;

public abstract interface WxMpMessageHandler
{
  public abstract WxMpXmlOutMessage handle(WxMpXmlMessage paramWxMpXmlMessage, Map<String, Object> paramMap, WxMpService paramWxMpService, WxSessionManager paramWxSessionManager)
    throws WxErrorException;
}
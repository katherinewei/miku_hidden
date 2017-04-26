package com.hiden.biz.wechat.mp.api;

import com.hiden.biz.wechat.mp.bean.WxMpXmlMessage;

public abstract interface WxMpMessageMatcher
{
  public abstract boolean match(WxMpXmlMessage paramWxMpXmlMessage);
}
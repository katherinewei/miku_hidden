package com.hiden.biz.wechat.common.session;

public abstract interface WxSessionManager
{
  public abstract WxSession getSession(String paramString);
  
  public abstract WxSession getSession(String paramString, boolean paramBoolean);
}
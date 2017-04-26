package com.hiden.biz.wechat.common.session;

import java.util.Enumeration;

public abstract interface WxSession
{
  public abstract Object getAttribute(String paramString);
  
  public abstract Enumeration<String> getAttributeNames();
  
  public abstract void setAttribute(String paramString, Object paramObject);
  
  public abstract void removeAttribute(String paramString);
  
  public abstract void invalidate();
}
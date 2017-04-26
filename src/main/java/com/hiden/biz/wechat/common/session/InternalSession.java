package com.hiden.biz.wechat.common.session;

public abstract interface InternalSession
{
  public abstract WxSession getSession();
  
  public abstract void setValid(boolean paramBoolean);
  
  public abstract boolean isValid();
  
  public abstract String getIdInternal();
  
  public abstract void expire();
  
  public abstract void access();
  
  public abstract void endAccess();
  
  public abstract void setCreationTime(long paramLong);
  
  public abstract void setMaxInactiveInterval(int paramInt);
  
  public abstract void setId(String paramString);
}
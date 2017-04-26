package com.hiden.biz.wechat.common.session;

public abstract interface InternalSessionManager
{
  public abstract InternalSession findSession(String paramString);
  
  public abstract InternalSession createSession(String paramString);
  
  public abstract void remove(InternalSession paramInternalSession);
  
  public abstract void remove(InternalSession paramInternalSession, boolean paramBoolean);
  
  public abstract void add(InternalSession paramInternalSession);
  
  public abstract int getActiveSessions();
  
  public abstract InternalSession createEmptySession();
  
  public abstract InternalSession[] findSessions();
  
  public abstract void backgroundProcess();
  
  public abstract void setMaxInactiveInterval(int paramInt);
  
  public abstract void setProcessExpiresFrequency(int paramInt);
  
  public abstract void setBackgroundProcessorDelay(int paramInt);
  
  public abstract void setMaxActiveSessions(int paramInt);
}
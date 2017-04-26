package com.hiden.biz.wechat.common.service;

import com.hiden.biz.wechat.common.exception.WxErrorException;

public abstract interface WxErrorExceptionHandler
{
  public abstract void handle(WxErrorException paramWxErrorException);
}

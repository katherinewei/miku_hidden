package com.hiden.biz.wechat.common.service;

public abstract interface WxMessageDuplicateChecker {
    public abstract boolean isDuplicate(String paramString);
}

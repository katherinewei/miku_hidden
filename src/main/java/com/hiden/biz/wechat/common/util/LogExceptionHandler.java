package com.hiden.biz.wechat.common.util;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.service.WxErrorExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogExceptionHandler
        implements WxErrorExceptionHandler {
    private Logger log = LoggerFactory.getLogger(WxErrorExceptionHandler.class);


    public void handle(WxErrorException e) {
        this.log.error("Error happens", e);
    }
}
package com.hiden.biz.wechat.mp.bean.result;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;

public class WxMpMassSendResult implements Serializable {
    private String errorCode;
    private String errorMsg;
    private String msgId;

    public WxMpMassSendResult() {
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public static WxMpMassSendResult fromJson(String json) {
        return (WxMpMassSendResult)WxMpGsonBuilder.create().fromJson(json, WxMpMassSendResult.class);
    }

    public String toString() {
        return "WxMassSendResult [errcode=" + this.errorCode + ", errmsg=" + this.errorMsg + ", msg_id=" + this.msgId + "]";
    }
}

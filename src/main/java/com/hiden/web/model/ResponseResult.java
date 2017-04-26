package com.hiden.web.model;

/**
 * Created by myron on 16-9-30.
 */
public class ResponseResult {

    int status = 0;

    String msg = "";

    int errorCode = -1;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

package com.hiden.web.common;

/**
 * Created by myron on 16-9-30.
 */
public enum ResponseStatusEnum {

    SUCCESS((byte) 1, "成功"),

    FAILED((byte) 0, "系统繁忙，请稍后再试");

    // 成员变量
    private byte code;

    private String msg;

    // 构造方法
    private ResponseStatusEnum(byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // get set 方法
    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

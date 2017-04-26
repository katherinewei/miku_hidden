package com.hiden.web.model;

public class WxNumVo {
	Long userId;
	Long wxId;
	String wxNum;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getWxId() {
		return wxId;
	}
	public void setWxId(Long wxId) {
		this.wxId = wxId;
	}
	public String getWxNum() {
		return wxNum;
	}
	public void setWxNum(String wxNum) {
		this.wxNum = wxNum;
	}
	@Override
	public String toString() {
		return "WxNumVo [userId=" + userId + ", wxId=" + wxId + ", wxNum="
				+ wxNum + "]";
	}
}

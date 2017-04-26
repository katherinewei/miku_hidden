package com.hiden.web.model;

public class SocketVo {
	private Long userId;
	private String info;
	private String sessionId;
	private String deviceInfo;
	
	
	
	public SocketVo(Long userId, String info, String sessionId,
			String deviceInfo) {
		super();
		this.userId = userId;
		this.info = info;
		this.sessionId = sessionId;
		this.deviceInfo = deviceInfo;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "SocketVo [userId=" + userId + ", info=" + info + ", sessionId="
				+ sessionId + "]";
	}
}

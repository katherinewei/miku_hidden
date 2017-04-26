package com.hiden.web.model;

import java.util.Date;

public class WxInfoVo {
	
	private Long id;
	private Long userId;
	private Date dateCreated;
	private Date lastUpdated;
	private String deviceInfo;
	private Long wxid;
	private Date onlineTime;
	private String deviceName;
	private String wxLemonName;
	private String wxOpendId;
	private String wxNum;
	private String wxPic;
	
	//设备对通讯录的标示
	//是否自动的标示[0不添加对应的通讯录  1添加对应的通讯录]
	private Byte contactFlag;
	//是否自动互聊的操作
	private Byte autoTalk;
	//互聊微信号的数组内容
	private String wxNoList;
	
	
	
	//是否有自动回复的功能
	//true有自动回复的状态   false是没有自动回复标示
	private boolean autoStatus;
	
	
	
	public String getWxNoList() {
		return wxNoList;
	}
	public void setWxNoList(String wxNoList) {
		this.wxNoList = wxNoList;
	}
	public Byte getAutoTalk() {
		return autoTalk;
	}
	public void setAutoTalk(Byte autoTalk) {
		this.autoTalk = autoTalk;
	}
	public Byte getContactFlag() {
		return contactFlag;
	}
	public void setContactFlag(Byte contactFlag) {
		this.contactFlag = contactFlag;
	}
	public boolean isAutoStatus() {
		return autoStatus;
	}
	public void setAutoStatus(boolean autoStatus) {
		this.autoStatus = autoStatus;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public Long getWxid() {
		return wxid;
	}
	public void setWxid(Long wxid) {
		this.wxid = wxid;
	}
	public Date getOnlineTime() {
		return onlineTime;
	}
	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getWxLemonName() {
		return wxLemonName;
	}
	public void setWxLemonName(String wxLemonName) {
		this.wxLemonName = wxLemonName;
	}
	public String getWxOpendId() {
		return wxOpendId;
	}
	public void setWxOpendId(String wxOpendId) {
		this.wxOpendId = wxOpendId;
	}
	public String getWxNum() {
		return wxNum;
	}
	public void setWxNum(String wxNum) {
		this.wxNum = wxNum;
	}
	public String getWxPic() {
		return wxPic;
	}
	public void setWxPic(String wxPic) {
		this.wxPic = wxPic;
	}
	@Override
	public String toString() {
		return "WxInfoVo [id=" + id + ", userId=" + userId + ", dateCreated="
				+ dateCreated + ", lastUpdated=" + lastUpdated
				+ ", deviceInfo=" + deviceInfo + ", wxid=" + wxid
				+ ", onlineTime=" + onlineTime + ", deviceName=" + deviceName
				+ ", wxLemonName=" + wxLemonName + ", wxOpendId=" + wxOpendId
				+ ", wxNum=" + wxNum + ", wxPic=" + wxPic + ", contactFlag="
				+ contactFlag + ", autoTalk=" + autoTalk + ", wxNoList="
				+ wxNoList + ", autoStatus=" + autoStatus + "]";
	}
	
}

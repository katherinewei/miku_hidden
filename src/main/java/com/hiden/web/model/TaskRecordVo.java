package com.hiden.web.model;

import java.util.Date;

public class TaskRecordVo {
	
    private Long id;
    private Long userId;
    private Date dateCreated;
    private Date lastUpdated;
    private String taskName;
    private Long version;
    private Byte status;
    private Byte type;
    private String taskValue;
    private String taskPic;
    private Byte deviceStatus;
    private String remark;
    private String comment;
    private Date startTime;
    private String address;
    
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getTaskValue() {
		return taskValue;
	}
	public void setTaskValue(String taskValue) {
		this.taskValue = taskValue;
	}
	public String getTaskPic() {
		return taskPic;
	}
	public void setTaskPic(String taskPic) {
		this.taskPic = taskPic;
	}
	public Byte getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(Byte deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "TaskRecordVo [id=" + id + ", userId=" + userId
				+ ", dateCreated=" + dateCreated + ", lastUpdated="
				+ lastUpdated + ", taskName=" + taskName + ", version="
				+ version + ", status=" + status + ", type=" + type
				+ ", taskValue=" + taskValue + ", taskPic=" + taskPic
				+ ", deviceStatus=" + deviceStatus + ", remark=" + remark
				+ ", comment=" + comment + ", startTime=" + startTime
				+ ", address=" + address + "]";
	}
    
    
}

package com.hiden.web.model;

public class PhoneInfoVo {
	private Long id;
	private String phoneNum;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	@Override
	public String toString() {
		return "PhoneInfoVo [id=" + id + ", phoneNum=" + phoneNum + "]";
	}
}

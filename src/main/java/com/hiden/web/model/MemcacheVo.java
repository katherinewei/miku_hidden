package com.hiden.web.model;

public class MemcacheVo {
	
	//自动回复的标示
	private int status;
	//任务个数
	private int tasknum;
	//自动回复的内容个数
	private int statusnum;
	//不更改的标示
	private int statementflag;
	//设备的唯一标示
	private String info;
	//电话簿的获取的标示
	private int phoneflag;
	//自动谈话的标示
	private int talkflag;
	//自动互聊的对象的个数标示
	private int talksum;
	
	
	
	@Override
	public String toString() {
		return "MemcacheVo [status=" + status + ", tasknum=" + tasknum
				+ ", statusnum=" + statusnum + ", statementflag="
				+ statementflag + ", info=" + info + ", phoneflag=" + phoneflag
				+ ", talkflag=" + talkflag + ", talksum=" + talksum + "]";
	}
	public int getTalksum() {
		return talksum;
	}
	public void setTalksum(int talksum) {
		this.talksum = talksum;
	}
	
	public int getTalkflag() {
		return talkflag;
	}
	public void setTalkflag(int talkflag) {
		this.talkflag = talkflag;
	}
	public int getPhoneflag() {
		return phoneflag;
	}
	public void setPhoneflag(int phoneflag) {
		this.phoneflag = phoneflag;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getStatementflag() {
		return statementflag;
	}
	public void setStatementflag(int statementflag) {
		this.statementflag = statementflag;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTasknum() {
		return tasknum;
	}
	public void setTasknum(int tasknum) {
		this.tasknum = tasknum;
	}
	public int getStatusnum() {
		return statusnum;
	}
	public void setStatusnum(int statusnum) {
		this.statusnum = statusnum;
	}

}

package com.hiden.web.model;

import java.util.List;

import com.hiden.persistence.domain.WxDeviceTaskJobDo;

public class PcJobListVo {
	WxDeviceTaskJobDo job;
	List<PcTaskRecordVo> list;
	public WxDeviceTaskJobDo getJob() {
		return job;
	}
	public void setJob(WxDeviceTaskJobDo job) {
		this.job = job;
	}
	public List<PcTaskRecordVo> getList() {
		return list;
	}
	public void setList(List<PcTaskRecordVo> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "PcJobListVo [job=" + job + ", list=" + list + "]";
	}
}

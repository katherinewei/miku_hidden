package com.hiden.biz.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hiden.persistence.DeviceInfoDOMapper;
import com.hiden.persistence.JobCacheDoMapper;
import com.hiden.persistence.domain.DeviceInfoDO;
import com.hiden.persistence.domain.JobCacheDo;
import com.hiden.persistence.domain.TaskAndDeviceDo;


@Service
public class JobCacheService {
	
	
	@Resource
	private JobCacheDoMapper jobCacheDoMapper;
	@Resource
	private DeviceInfoDOMapper deviceInfoDOMapper;
	
	
	
	//批量操作对应的内容值
	public void insertOneJobData(TaskAndDeviceDo taskAndDevice){
		DeviceInfoDO deviceInfoDO=deviceInfoDOMapper.selectByPrimaryKey(taskAndDevice.getDeviceId());
		if(null!=deviceInfoDO)
			addOneJobCached(taskAndDevice.getUserId(),taskAndDevice.getUserId(),taskAndDevice.getDeviceId(),deviceInfoDO.getDeviceInfo());
	}
	
	
	
	//单单新增一个job的缓冲的数据内容
	public void addOneJobCached(Long profileId,Long jobId,Long deviceId,String deviceInfo){
		JobCacheDo model=new JobCacheDo();
		model.setDeviceId(deviceId);
		model.setDeviceInfo(deviceInfo);
		model.setDoStatus((byte)0);
		model.setReadStatus((byte)0);
		model.setJobId(jobId);
		model.setStatus((byte)1);
		jobCacheDoMapper.insert(model);
	}

}

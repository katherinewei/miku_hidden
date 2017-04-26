package com.hiden.biz.service;

import com.alibaba.fastjson.JSON;
import com.hiden.biz.cache.CheckNOGenerator;
import com.hiden.biz.common.BizConstants;
import com.hiden.biz.model.AlipayInfoModel;
import com.hiden.biz.model.UserInfo;
import com.hiden.biz.model.WxInfoModel;
import com.hiden.biz.wechat.mp.bean.result.WxMpOAuth2AccessToken;
import com.hiden.biz.wechat.mp.bean.result.WxMpUser;
import com.hiden.persistence.*;
import com.hiden.persistence.domain.*;
import com.hiden.utils.EmojiFilter;
import com.hiden.web.model.PcTaskRecordVo;
import com.hiden.web.model.TaskRecordVo;
import com.hiden.web.model.pcPersonStatement;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by myron on 16-9-30.
 */
@Service
public class DeviceUserService {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(DeviceUserService.class);

    @Resource
    private ProfileDOMapper profileDOMapper;
    @Resource
    private DeviceInfoDOMapper deviceInfoDOMapper;
    @Resource
    private WxContactInfoDOMapper wxContactInfoDOMapper;
    @Resource
    private TaskAndDeviceDoMapper taskAndDeviceDoMapper;
    @Resource
    private WxDeviceTaskJobDoMapper wxDeviceTaskJobDoMapper;
    @Resource
    private ProfileStatementDoMapper profileStatementDoMapper;
    
    
    /**
     * 进行用户进行初始化的时候终端
     * @param profileId 用户的id
     * @param deviceInfo 终端的唯一标示
     * @param wxNum 微信号
     * @param deviceName 设备名称
     * @param openId 微信的唯一标示
     * @param lemonName 微信名称
     * @param wxPic 微信的头像
     */
    public Map doDeviceInfoOperate(Long profileId,String deviceInfo,String wxNum,String deviceName,String openId,String lemonName,String wxPic){
    	Map map=new HashMap<>();
    	List<WxContactInfoDO> wxlist=isHaveThisWxContent(profileId,openId);
    	List<DeviceInfoDO> dlist=isHaveThisDevice(profileId,deviceInfo);
    	
    	WxContactInfoDO wxContactInfoDO=new WxContactInfoDO();
    	DeviceInfoDO deviceModel=new DeviceInfoDO();
    	//是否有对应的用户的微信的账号
    	if(wxlist.size()>0){
    		wxContactInfoDO=wxlist.get(0);
    		wxContactInfoDO.setWxNum(wxNum);
    		wxContactInfoDOMapper.updateByPrimaryKey(wxContactInfoDO);
    	}else{
    		//没有账号直接的插入对应的相关的信息内容
    		wxContactInfoDO=InsertOneWxData(profileId,openId,lemonName,wxNum,wxPic);
    	}
    	
    	//具有对应的设备
    	if(dlist.size()>0){
    		deviceModel=dlist.get(0);
    		deviceModel.setLastUpdated(new Date());
    		deviceModel.setOnlineTime(new Date());
    		deviceModel.setWxid(wxContactInfoDO.getId());
    		deviceInfoDOMapper.updateByPrimaryKey(deviceModel);
    	}
    	else{
    		deviceModel=InsertOneDeviceData(deviceName,deviceInfo,profileId,wxContactInfoDO.getId());
    	}
    	
    	//查找终端的任务信息内容
    	List<TaskRecordVo> list= getJobList(deviceModel.getDeviceInfo(),profileId);
    	map.put("job", list);
    	map.put("jobSize", list.size());
    	map.put("deviceInfo", deviceModel);
    	map.put("wxInfo", wxContactInfoDO);
    	map.put("userId", profileId);
    	map.put("talkFlag", (null!=deviceModel.getAutoTalk())?deviceModel.getAutoTalk():(byte)0);
    	return map;
    }
    
    
    //根据电话号码来查找对应的用户内容
    public List<ProfileDO>  selectProfileBymobile(String mobile){
    	ProfileDOExample profileDOExample = new ProfileDOExample();
        profileDOExample.createCriteria().andMobileEqualTo(mobile).andStatusEqualTo((byte) 1);
        List<ProfileDO> list=profileDOMapper.selectByExample(profileDOExample);
        return list;
    }
    
    //判断是否有对应的微信信息内容
    public List<WxContactInfoDO> isHaveThisWxContent(Long profileId,String openId){
    	WxContactInfoDOExample example=new WxContactInfoDOExample();
    	example.createCriteria().andWxOpendIdEqualTo(openId).andUserIdEqualTo(profileId);
    	List<WxContactInfoDO> list=wxContactInfoDOMapper.selectByExample(example);
    	return list;
    }
    //判断是否用户有对应的设备内容
    public List<DeviceInfoDO> isHaveThisDevice(Long profileId,String deviceInfo){
    	DeviceInfoDOExample example=new DeviceInfoDOExample();
    	example.createCriteria().andUserIdEqualTo(profileId).andDeviceInfoEqualTo(deviceInfo);
    	List<DeviceInfoDO> list=deviceInfoDOMapper.selectByExample(example);
    	return list;
    }
    //进行插入一条设备信息与用户的关联
    public DeviceInfoDO InsertOneDeviceData(String deviceName,String deviceInfo,Long profileId,Long wxId){
    	DeviceInfoDO deviceInfoDO=new DeviceInfoDO();
    	deviceInfoDO.setDateCreated(new Date());
    	deviceInfoDO.setOnlineTime(new Date());
    	deviceInfoDO.setDeviceName(deviceName);
    	deviceInfoDO.setDeviceInfo(deviceInfo);
    	deviceInfoDO.setLastUpdated(new Date());
    	deviceInfoDO.setUserId(profileId);
    	deviceInfoDO.setWxid(wxId);
    	deviceInfoDO.setVersion(1L);
    	deviceInfoDO.setContactFlag((byte)0);
    	deviceInfoDO.setAutoTalk((byte)0);
    	deviceInfoDOMapper.insert(deviceInfoDO);
    	return deviceInfoDO;
    }
    //进行插入一条微信信息与用户的关联
    public WxContactInfoDO InsertOneWxData(Long profileId,String openId,String lemonName,String wxNum,String wxPic){
    	WxContactInfoDO wxModeContactInfoDO=new WxContactInfoDO();
    	wxModeContactInfoDO.setDateCreated(new Date());
    	wxModeContactInfoDO.setLastUpdated(new Date());
    	wxModeContactInfoDO.setWxLemonName(lemonName);
    	wxModeContactInfoDO.setWxOpendId(openId);
    	wxModeContactInfoDO.setUserId(profileId);
    	wxModeContactInfoDO.setWxNum(wxNum);
    	wxModeContactInfoDO.setWxPic(wxPic);
    	wxModeContactInfoDO.setVersion(1L);
    	wxContactInfoDOMapper.insert(wxModeContactInfoDO);
    	return wxModeContactInfoDO;
    }
    //查找自动回复的规则
    public List<ProfileStatementDo> getListInfoBystatement(Long profileId,Long deviceId){
    	ProfileStatementDoExample example=new ProfileStatementDoExample();
    	example.createCriteria().andUserIdEqualTo(profileId).andStatusEqualTo((byte)1).andDeviceIdEqualTo(deviceId);
    	List<ProfileStatementDo> list=profileStatementDoMapper.selectByExample(example);
    	return list;
    }
    
    
    //根据对应的userId与deviceInfo信息来获取对应的job的列表内容
    public List<TaskRecordVo> getJobList(String deviceInfo,Long profileId){
    	Map map=new HashMap<>();
    	map.put("info", deviceInfo);
    	map.put("userId", profileId);
    	//查找终端的任务信息内容
    	List<TaskRecordVo> tlis= wxDeviceTaskJobDoMapper.selectTaskInfoByDeviceInf(map);
//    	List<WxDeviceTaskJobDo> list= wxDeviceTaskJobDoMapper.selectTaskByDeviceInfo(map);
    	return tlis;
    }
    
    
    
    
    //添加一条任务信息内容 wx_device_task_job
    public WxDeviceTaskJobDo InsertOneTaskJob(int flag,Long userId,String taskValue,String taskPic,Date startTime,String comment,String address,int totalSize){
    	WxDeviceTaskJobDo wxDeviceTaskJobDo=new WxDeviceTaskJobDo();
    	wxDeviceTaskJobDo.setDateCreated(new Date());
    	wxDeviceTaskJobDo.setLastUpdated(new Date());
    	wxDeviceTaskJobDo.setVersion(1L);
    	wxDeviceTaskJobDo.setStatus((byte)1);
    	wxDeviceTaskJobDo.setType((byte)flag);
    	wxDeviceTaskJobDo.setUserId(userId);
    	wxDeviceTaskJobDo.setAddress(address);
    	wxDeviceTaskJobDo.setTaskValue(taskValue);
    	wxDeviceTaskJobDo.setTaskPic(taskPic);
    	wxDeviceTaskJobDo.setStartTime(startTime);
    	wxDeviceTaskJobDo.setComment(comment);
    	wxDeviceTaskJobDo.setSuccessNum(0);
    	wxDeviceTaskJobDo.setErrorNum(0);
    	wxDeviceTaskJobDo.setDeleteNum(0);
    	wxDeviceTaskJobDo.setNodoNum(totalSize);
    	wxDeviceTaskJobDo.setFailNum(0);
    	//对个数进行修改对应的数量内容
    	return wxDeviceTaskJobDo;
    }
    
    
    //添加一条关联设备与微信内容的数据task_and_device
    public TaskAndDeviceDo InsertOneTaskAndDeviceData(WxDeviceTaskJobDo model,Long deviceId){
    	//status 执行状态:-1失败 0=未开始 1成功 2异常 -2删掉
    	TaskAndDeviceDo taskAndDeviceDo=new TaskAndDeviceDo();
    	taskAndDeviceDo.setDateCreated(new Date());
    	taskAndDeviceDo.setLastUpdated(new Date());
    	taskAndDeviceDo.setVersion(1L);
    	taskAndDeviceDo.setJobId(model.getId());
    	taskAndDeviceDo.setUserId(model.getUserId());
    	taskAndDeviceDo.setDeviceId(deviceId);
    	taskAndDeviceDo.setStatus((byte)0);
    	taskAndDeviceDo.setRemark(model.getTaskName());
    	taskAndDeviceDoMapper.insert(taskAndDeviceDo);
    	return taskAndDeviceDo;
    }
    
    
    //添加一条自动回复的规则内容值
    public ProfileStatementDo addProStatemt(Long pId,Long deviceId,String data,String keyword){
    	ProfileStatementDo profileStatementDo=new ProfileStatementDo();
    	profileStatementDo.setDateCreated(new Date());
    	profileStatementDo.setLastUpdated(new Date());
    	profileStatementDo.setVersion(1L);
    	profileStatementDo.setKeyword(keyword);
    	profileStatementDo.setDeviceId(deviceId);
    	profileStatementDo.setReplyContent(data);
    	profileStatementDo.setUserId(pId);
    	profileStatementDo.setStatus((byte)1);
    	profileStatementDoMapper.insert(profileStatementDo);
    	return profileStatementDo;
    }
    
    
    
    
    //查询对应的规则内容 
    public List<ProfileStatementDo> getListStatement(Long pId,Long deviceId,int flag, int pageNo, int pageSize){
    	ProfileStatementDoExample example=new ProfileStatementDoExample();
    	example.setOrderByClause("last_updated  desc");
    	example.setLimit(pageSize);
    	example.setOffset(pageNo*pageSize);
    	if(flag==1){
    		example.createCriteria().andUserIdEqualTo(pId).andStatusEqualTo((byte)1);
    	}else{
    		example.createCriteria().andUserIdEqualTo(pId).andDeviceIdEqualTo(deviceId).andStatusEqualTo((byte)1);
    	}
    	List<ProfileStatementDo> list=profileStatementDoMapper.selectByExample(example);
    	return list;
    }
    
    
    
    //根据对应的数据内容过滤重复的内容
    public List<pcPersonStatement> getqcStatementContent(List<ProfileStatementDo> list,List<ProfileStatementDo> contetntlist,int[] intids){
    	List<pcPersonStatement> pclist=new LinkedList<pcPersonStatement>();
    	//内容值的遍历
    	for(ProfileStatementDo content:contetntlist){
    		int oneSize=0;
    		List<Long> idlist=new LinkedList<Long>();
    		for(ProfileStatementDo lmodel:list){
    			if(lmodel.getKeyword().equals(content.getKeyword()) && lmodel.getKeyword().equals(content.getKeyword())){
    				oneSize++;
    				idlist.add(lmodel.getId());
    			}
    		}
    		if(oneSize==intids.length && oneSize>0){
    			pcPersonStatement data=getpcPersonStatement(content);
    			data.setIds(getIdListStr(idlist));
    			pclist.add(data);
    		}
    	}
    	return pclist;
    }
    
    public String getIdListStr(List<Long> idlist){
    	StringBuffer stringBuffer=new StringBuffer();
    	for(int i=0,len=idlist.size();i<len;i++){
    		stringBuffer.append(idlist.get(i));
    		if(i!=(len-1))
    			stringBuffer.append(";");
    	}
    	return stringBuffer.toString();
    }
    
    
    public String getIdStrListStr(List<String> idlist){
    	StringBuffer stringBuffer=new StringBuffer();
    	for(int i=0,len=idlist.size();i<len;i++){
    		stringBuffer.append(idlist.get(i));
    		if(i!=(len-1))
    			stringBuffer.append(";");
    	}
    	return stringBuffer.toString();
    }
    
    //由ProfileStatementDo  --->  pcPersonStatement
    public pcPersonStatement getpcPersonStatement(ProfileStatementDo p){
    	pcPersonStatement data=new pcPersonStatement();
		data.setKeyword(p.getKeyword());
		data.setReplyContent(p.getReplyContent());
		return data;
    }
    
    
    //去重的内容列表内容值
    public List<Long> getOneListData(List<Long> list){
    	 List newList=new ArrayList<Long>();
    	 List<Long> listTemp= new ArrayList<Long>();  
    	 Iterator<Long> it=list.iterator();  
    	 while(it.hasNext()){  
    	  Long a=it.next();  
    	  if(listTemp.contains(a)){  
    	   newList.add(a);
    	   it.remove();  
    	  }  
    	  else{  
    	   listTemp.add(a);  
    	  }  
    	 }  
    	return list;
    }
    
    
    
    //进行把重复的内容进行整合起来
    public List<PcTaskRecordVo> getFinalTaskRecordList(List<PcTaskRecordVo> list){
    	List<Long> li2=new LinkedList<>();
    	for(PcTaskRecordVo data:list){
    		li2.add(data.getJobId());
    	}
    	li2=getOneListData(li2);
    	List<PcTaskRecordVo> nList=new LinkedList<PcTaskRecordVo>();
    	  for(Long mun:li2){
    		 PcTaskRecordVo model=new PcTaskRecordVo();
    		 List<String> strlist=new LinkedList<String>();
    		 List<Long> idsstrlist=new LinkedList<Long>();
    		 int size=0;
    		 Byte status=0;
    		 for(int i=0,len=list.size();i<len;i++){
    			 size++;
    			 PcTaskRecordVo numdata=list.get(i);
    			 if(String.valueOf(79648).equals(numdata.getJobId().toString())){
        			 System.out.println();
        		 }
    			 if(numdata.getJobId().toString().equals(mun.toString()) ){
    				 status=numdata.getJobStatus();
//    				 System.out.println("Status值内容:"+status+"   id内容："+mun.toString());
    				 System.out.println();
    				 model=numdata;
    				 strlist.add(numdata.getWxLemonName());
    				 idsstrlist.add(numdata.getTaskId());
    			 }
    		 }
    		 model.setdNames(getIdStrListStr(strlist));
    		 model.setTaskIds(getIdListStr(idsstrlist));
    		 nList.add(model);
    	}   
    	return nList;
    }
    
    
    
    //根据statement数据集合来查出对应的唯一的deviceId内容
    public String getDeviceIdsByStatmentIds(int[] tarr){
    	  Map tmap=new HashMap();
          tmap.put("ids", tarr);
          String str=profileStatementDoMapper.selectDeviceIdsStrByStatementIds(tmap);
          String[] arr=str.split(",");
          List<String> list=getOnlyOneStr(arr);
          String tstr=getIdStrListStr(list);
          return tstr;
    }
    
    
    
    public  List<String> getOnlyOneStr(String[] arr){
    	 List<String> list=new ArrayList<String>();
         for(String s:arr){
        	 if(!list.contains(s)){
        		 list.add(s);
        	 }
         }
         return list;
    }
    
    
    //将list有重复的内容的链表变成去重的字符串并;隔开
    public String getFinalDevides(List<String> strlist){
    	 String[] arr=strlist.toArray(new String[strlist.size()]); 
    	//去重
         List<String> slist=getOnlyOneStr(arr);
         //获取对应的deviceIds
         String tstr=getIdStrListStr(slist);
         return tstr;
    }
    
    
    
    //根据对应的job的id来查找对应的devices来内容
    public String getDevicesIdByTaskId(Long taskId){
    	TaskAndDeviceDoExample example=new TaskAndDeviceDoExample();
    	example.createCriteria().andJobIdEqualTo(taskId).andStatusEqualTo((byte)0);
    	List<TaskAndDeviceDo> tasklist=taskAndDeviceDoMapper.selectByExample(example);
    	List<String> strList=new LinkedList<String>();
    	for(TaskAndDeviceDo data:tasklist){
    		strList.add(String.valueOf(data.getDeviceId()));
    	}
    	return getIdStrListStr(strList);
    }
    
    
    //根据对应的jobId来获取对应的
    public void doChangeStatusToJob(Long taskId){
    	WxDeviceTaskJobDo wxDeviceTaskJobDo=wxDeviceTaskJobDoMapper.selectByPrimaryKey(taskId);
    	//当他是朋友圈的内容的时候
    	if(((byte)2==wxDeviceTaskJobDo.getType()) && (null!=wxDeviceTaskJobDo.getStartTime())){
    		wxDeviceTaskJobDo.setLastUpdated(new Date());
    		wxDeviceTaskJobDo.setStartTime(null);
    		wxDeviceTaskJobDoMapper.updateByPrimaryKey(wxDeviceTaskJobDo);
    	}
    }
    
    public WxDeviceTaskJobDo getTargetJobStatus(byte status,WxDeviceTaskJobDo job,int flag,int size){
//    	-1失败 0=未开始 1成功 2异常 -2删掉
    	switch (status) {
		case (byte)-1:
			if(null!=job.getFailNum()){
				job.setFailNum(job.getFailNum()+(flag)*(size));
			}
			break;
		case (byte)0:
			if(null!=job.getNodoNum()){
				job.setNodoNum(job.getNodoNum()+(flag)*(size));
			}
			break;
		case (byte)1:
			if(null!=job.getSuccessNum()){
				job.setSuccessNum(job.getSuccessNum()+(flag)*(size));
			}
			break;
		case (byte)2:
			if(null!=job.getErrorNum()){
				job.setErrorNum(job.getErrorNum()+(flag)*(size));
			}
			break;
		case (byte)-2:
			if(null!=job.getDeleteNum()){
				job.setDeleteNum(job.getDeleteNum()+(flag)*(size));
			}
			break;
		}
    	return job;
    }
    
    
    
    //进行实时查询对应的任务次数统计
    public void updateRealJobNum(Long jobId){
    	wxDeviceTaskJobDoMapper.updateJobNums(jobId);
    }

    
    //根据对应原先的状态内容进行切换到对应的状态值
    public void changeJobNum(Long jobId,byte oldStatus,byte nowStatus,int size){
    	WxDeviceTaskJobDo wxDeviceTaskJobDo=wxDeviceTaskJobDoMapper.selectByPrimaryKey(jobId);
    	if(null!=wxDeviceTaskJobDo){
    		wxDeviceTaskJobDo=getTargetJobStatus(oldStatus,wxDeviceTaskJobDo,(-1),size);
    		wxDeviceTaskJobDo=getTargetJobStatus(nowStatus,wxDeviceTaskJobDo,1,size);
    		wxDeviceTaskJobDoMapper.updateByPrimaryKey(wxDeviceTaskJobDo);
    	}
    }
    
    
    
    
    //根据对应的状态值来获取返回对应的Map值
    public Map getTargetMapValue(Byte status,Map map){
    	if(null==status){
    		map.put("allNum", 1);
    		return map;
    	}
//    	-1失败 0=未开始 1成功 2异常 -2删掉
    	switch (status) {
		case (byte)-1:
			map.put("failNum", 1);
			break;
		case (byte)0:
			map.put("nodoNum", 1);
			break;
		case (byte)1:
			map.put("successNum", 1);
			break;
		case (byte)2:
			map.put("errorNum", 1);
			break;
		}
    	return map;
    }
    
    
    //从对应的job来找对应的列表集合
//    public 
    
    
   
    
}

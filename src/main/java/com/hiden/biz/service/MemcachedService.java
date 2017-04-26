package com.hiden.biz.service;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;

import net.spy.memcached.MemcachedClient;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hiden.persistence.DeviceInfoDOMapper;
import com.hiden.persistence.JobCacheDoMapper;
import com.hiden.persistence.ProfileStatementDoMapper;
import com.hiden.persistence.TaskAndDeviceDoMapper;
import com.hiden.persistence.domain.DeviceInfoDO;
import com.hiden.persistence.domain.DeviceInfoDOExample;
import com.hiden.persistence.domain.TaskAndDeviceDo;
import com.hiden.web.model.MemcacheVo;

@Service
public class MemcachedService {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(MemcachedService.class);
	@Resource
    private MemcachedClient memcachedClient;
	@Resource 
	private TaskAndDeviceDoMapper taskAndDeviceDoMapper;
	@Resource 
	private DeviceInfoDOMapper deviceInfoDOMapper;
	@Resource 
	private ProfileStatementDoMapper profileStatementDoMapper;
	@Resource
	private JobCacheDoMapper jobCacheDoMapper;
	
	public final static int MCTime=86400;
	//memached中的对应的状态值
	public final static int STATUS=1;
	public final static int TASKNUM=2;
	public final static int STATEMENTNUM=3;
	public final static int STATEMENTFLAG=4;
	public final static int PHONEFLAG=5;
	public final static int TALKFLAG=6;
	
	
	//memached对应的key值
	//自动对应话标示key值,value-->0已读不处理  -1是未读  
	public final static int TALKSUM=7;
	public final static String TALKNUM="-talk-num";
    
    
    
	//**************************************专门测试使用的*********************************************/
	public List<MemcacheVo> getValueByUserIdAndInfo(Long userId,List<String> infolist){
		List<MemcacheVo> list=new LinkedList<MemcacheVo>();
		for(String info:infolist){
			String key=userId+"-"+info;
			int status=(int) ((memcachedClient.get(key+"-status")!=null)?(memcachedClient.get(key+"-status")):-1);
			int tasknum=(int) ((memcachedClient.get(key+"-num")!=null)?(memcachedClient.get(key+"-num")):-1);
			int statusnum=(int) ((memcachedClient.get(key+"-statment-status")!=null)?(memcachedClient.get(key+"-statment-status")):-1);
			int statementflag=(int) ((memcachedClient.get(key+"-statment-status-flag")!=null)?(memcachedClient.get(key+"-statment-status-flag")):-1);
			int phoneflag=(int) ((memcachedClient.get(key+"-phone-flag")!=null)?(memcachedClient.get(key+"-phone-flag")):-1);
			int talkflag=(int) ((memcachedClient.get(key+"-talk-flag")!=null)?(memcachedClient.get(key+"-talk-flag")):-1);
			int talknum=(int) ((memcachedClient.get(key+TALKNUM)!=null)?(memcachedClient.get(key+TALKNUM)):-1);
			if(!(status==(-1) && tasknum==(-1) && statusnum==(-1) && statementflag==(-1))){
				MemcacheVo memcacheVo=new MemcacheVo();
				memcacheVo.setStatus(status);
				memcacheVo.setStatementflag(statementflag);
				memcacheVo.setTasknum(tasknum);
				memcacheVo.setStatusnum(statusnum);
				memcacheVo.setInfo(info);
				memcacheVo.setPhoneflag(phoneflag);
				memcacheVo.setTalkflag(talkflag);
				memcacheVo.setTalksum(talknum);
				list.add(memcacheVo);
			}
		}
		return list;
	}
	
	//**************************************专门给PC查找socket使用的*********************************************/
	
	
    //**************************************专门给PC使用的*********************************************/
	//单单新增任务与自动回复语的设置
    public void setDevicesMemcachedValue(Long profileId,String deviceIds,int flag){
    	if(deviceIds==null){
    		return;
    	}
    	String[] strArr=deviceIds.split(";");
     	for(String str:strArr){
     		DeviceInfoDO device=deviceInfoDOMapper.selectByPrimaryKey(Long.parseLong(str));
     		int size=0;
     		switch (flag) {
			case TASKNUM:
				size=taskAndDeviceDoMapper.selectTotalSizeByDeviceId(device.getId());
				setOneTargetMemecachedValue(profileId,device.getDeviceInfo(),TASKNUM,size);
				break;
			case STATEMENTNUM:
				//根据deviceId来查找对应的数量值
				size=profileStatementDoMapper.selectStamentFlag(device.getId());
				setOneTargetMemecachedValue(profileId,device.getDeviceInfo(),STATEMENTNUM,size);
				setOneTargetMemecachedValue(profileId,device.getDeviceInfo(),STATEMENTFLAG,size);
				break;	
				
			}
     	}
    }
    
   //单单自动回复语的状态值内的设置
   public void setDevicesAutoFlagMemcachedValue(Long profileId,String deviceIds,byte autoStatus){
	   if(deviceIds==null){
   		return;
   	   }
	   String[] strArr=deviceIds.split(";");
       for(String str:strArr){
    	    DeviceInfoDO device=deviceInfoDOMapper.selectByPrimaryKey(Long.parseLong(str));
    	    setOneTargetMemecachedValue(profileId,device.getDeviceInfo(),STATUS,autoStatus);	
    	   
    	    //根据deviceId来查找对应的数量值
    	    int size=profileStatementDoMapper.selectStamentFlag(device.getId());
			setOneTargetMemecachedValue(profileId,device.getDeviceInfo(),STATEMENTNUM,size);
			setOneTargetMemecachedValue(profileId,device.getDeviceInfo(),STATEMENTFLAG,size);
       }
   }
   
   //单单给添加电话簿进行刷新缓存内容
   public void SetPhoneListCache(Long profileId,int size){
	   List<DeviceInfoDO> list=getDeviceList(profileId);
	   for(DeviceInfoDO device:list){
		   setOneTargetMemecachedValue(profileId,device.getDeviceInfo(),PHONEFLAG,size);
	   }
   }
   
   public List<DeviceInfoDO> getDeviceList(Long profileId){
	   DeviceInfoDOExample example=new DeviceInfoDOExample();
	   example.createCriteria().andUserIdEqualTo(profileId).andContactFlagEqualTo((byte)1);
	   List<DeviceInfoDO> list=deviceInfoDOMapper.selectByExample(example);
	   return list;
   }
   
   
   //删除对应的Memcached的内存的信息
   public void deleteMemcacheContent(Long userId,List<String> deviceInfoList){
	   for(String info:deviceInfoList){
		    String key=userId+"-"+info;
		    memcachedClient.delete(key+"-status");
		    memcachedClient.delete(key+"-num");
		    memcachedClient.delete(key+"-statment-status");
		    memcachedClient.delete(key+"-statment-status-flag");
		    memcachedClient.delete(key+"-phone-flag");
		    memcachedClient.delete(key+"-talk-flag");
		    memcachedClient.delete(key+TALKNUM);
	   }
   }
    
    
    
    
    
    
    //**************************************专门给APP使用的*********************************************//
    //专门给Memcached进行设置值的操作
    public void setTargetMemecachedValue(Long userId,String info,int status,int size,int statementSize,Byte contactFlag,Byte autoTalk){
    	String key=userId+"-"+info;
    	//自动回复的状态语内容
        memcachedClient.set(key+"-status",MCTime, status);
        //任务的数量
        memcachedClient.set(key+"-num",MCTime, size);
        //自动回复的状态是否接收的状态
        memcachedClient.set(key+"-statment-status",MCTime, statementSize);
        //自动回复的数量的状态：1代表有值  0代表无值 
		memcachedClient.set(key+"-statment-status-flag",MCTime, (statementSize>0)?1:0);
		//电话簿的信息内容标示
		memcachedClient.set(key+"-phone-flag",MCTime, (contactFlag==(byte)1)?1:0);
		//自动对话的标示
		memcachedClient.set(key+"-talk-flag",MCTime, (autoTalk==(byte)1)?1:0);
		//自动对话的数量标示
		memcachedClient.set(key+TALKNUM,MCTime, (autoTalk==(byte)1)?-1:0);
    }
    
    //专门给某个Memcached值进行设置操作
    public void setOneTargetMemecachedValue(Long userId,String info,int flag,int size){
    	String key=userId+"-"+info;
    	switch (flag) {
    	//1.自动回复的状态
		case STATUS:
			 memcachedClient.set(key+"-status",MCTime, size);
			break;
		//2.任务的数量
		case TASKNUM:
			 memcachedClient.set(key+"-num",MCTime, size);
		    break;
		//3.自动回复的状态是否接收的状态
		case STATEMENTNUM:
			memcachedClient.set(key+"-statment-status",MCTime, size);
			break;
		//4.自动回复的数目的状态标示
		case STATEMENTFLAG:
			//自动回复的数量的状态：1代表有值  0代表无值 
			memcachedClient.set(key+"-statment-status-flag",MCTime, (size>0)?1:0);
		//5.电话簿的信息内容标示
		case PHONEFLAG:
			memcachedClient.set(key+"-phone-flag",MCTime, (size>0)?1:0);
			break;
		//6.自动对话
		case TALKFLAG:
			memcachedClient.set(key+"-talk-flag",MCTime, (size>0)?1:0);
			break;
		//7.自动对话的对象个数的标示	
		case TALKSUM:
			memcachedClient.set(key+TALKNUM,MCTime, size);
			break;
		}
    }
    
    
    //获取对应Memecached进行状态值内容
    public MemcacheVo getMemechachedValue(Long userId,String info){
    	MemcacheVo model=new MemcacheVo();
    	String key=userId+"-"+info;
    	model.setInfo(info);
    	for(int i=1;i<=7;i++){
    		switch (i) {
        	//1.自动回复的状态
    		case STATUS:
    			model.setStatus((int) ((memcachedClient.get(key+"-status")!=null)?(memcachedClient.get(key+"-status")):-1));
    			break;
    		//2.任务的数量
    		case TASKNUM:
    			model.setTasknum((int) ((memcachedClient.get(key+"-num")!=null)?(memcachedClient.get(key+"-num")):-1));
    		    break;
    		//3.自动回复的状态是否接收的状态
    		case STATEMENTNUM:
    			model.setStatusnum((int) ((memcachedClient.get(key+"-statment-status")!=null)?(memcachedClient.get(key+"-statment-status")):-1));
    			break;
    		//4.自动回复的数目的状态标示
    		case STATEMENTFLAG:
    			model.setStatementflag((int) ((memcachedClient.get(key+"-statment-status-flag")!=null)?(memcachedClient.get(key+"-statment-status-flag")):-1));
    			break;
    		//5.电话簿的信息内容标示
    		case PHONEFLAG:
    			model.setPhoneflag((int) ((memcachedClient.get(key+"-phone-flag")!=null)?(memcachedClient.get(key+"-phone-flag")):-1));
    			break;
    		//6.自动对话
    		case TALKFLAG:
    			model.setTalkflag((int) ((memcachedClient.get(key+"-talk-flag")!=null)?(memcachedClient.get(key+"-talk-flag")):-1));
    			break;
    		//7.自动对话的对象个数的标示	
    		case TALKSUM:
    			model.setTalksum((int)((memcachedClient.get(key+TALKNUM)!=null)?(memcachedClient.get(key+TALKNUM)):-1));
    			break;	
    		}
    	}
    	return model;
    }
    
}

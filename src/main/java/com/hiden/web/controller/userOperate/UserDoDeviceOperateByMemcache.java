package com.hiden.web.controller.userOperate;

import com.alibaba.fastjson.JSON;
import com.hiden.biz.cache.CheckNOGenerator;
import com.hiden.biz.cache.CheckNOValidator;
import com.hiden.biz.common.BizConstants;
import com.hiden.biz.common.BizErrorEnum;
import com.hiden.biz.common.ProfileStatusEnum;
import com.hiden.biz.security.BCrypt;
import com.hiden.biz.security.PasswordParser;
import com.hiden.biz.security.RSAEncrypt;
import com.hiden.biz.service.DeviceUserService;
import com.hiden.biz.service.DoPhoneListService;
import com.hiden.biz.service.MemcachedService;
import com.hiden.biz.service.UserService;
import com.hiden.persistence.DeviceInfoDOMapper;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.ProfileStatementDoMapper;
import com.hiden.persistence.ProfileWechatDOMapper;
import com.hiden.persistence.TaskAndDeviceDoMapper;
import com.hiden.persistence.WxDeviceTaskJobDoMapper;
import com.hiden.persistence.domain.DeviceInfoDO;
import com.hiden.persistence.domain.DeviceInfoDOExample;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileDOExample;
import com.hiden.persistence.domain.ProfileStatementDo;
import com.hiden.persistence.domain.TaskAndDeviceDo;
import com.hiden.persistence.domain.WxDeviceTaskJobDo;
import com.hiden.persistence.domain.WxFriendList;
import com.hiden.tacker.EventTracker;
import com.hiden.utils.PhenixUserHander;
import com.hiden.utils.TimeUtils;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.MemcacheVo;
import com.hiden.web.model.PhoneInfoVo;
import com.hiden.web.model.ResponseResult;
import com.hiden.web.model.TaskRecordVo;
import com.hiden.web.model.UserAgent;
import com.hiden.web.model.WxNumVo;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 
 * @author 11-21-2016 You
 *
 */
@RestController
public class UserDoDeviceOperateByMemcache {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(UserDoDeviceOperateByMemcache.class);
    @Resource
    private ProfileDOMapper profileDOMapper;
    @Resource
    private DeviceUserService deviceUserService;
    @Resource
    private MemcachedService memcachedService;
    @Resource
    private ProfileStatementDoMapper profileStatementDoMapper;
    @Resource
    private WxDeviceTaskJobDoMapper wxDeviceTaskJobDoMapper;
    @Resource
    private TaskAndDeviceDoMapper taskAndDeviceDoMapper;
    @Resource
    private DeviceInfoDOMapper deviceInfoDOMapper;
    @Resource
    private MemcachedClient memcachedClient;
    @Resource
    private DoPhoneListService doPhoneListService;
    
    public static int STATUS=1;
    public static int TASKNUM=2;
    public static int STATEMENTNUM=3;
	public final static int STATEMENTFLAG=4;
	public final static int PHONEFLAG=5;
	public final static int TALKFLAG=6;
	public final static int TALKSUM=7;
   
    //轮训获取对应的job列表信息内容
    //进行操作对应的设备内容操作：用户id+设备基本信息
    @RequestMapping(value = {"/api/m/1.0/MemgetJobListData.json", "/api/h/1.0/MemgetJobListData.json"}, produces = "application/json;charset=utf-8")
    public String MemgetJobListData(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="tflag", required=false,defaultValue="-1") int tflag,
    		@RequestParam(value="deviceInfo", required=false) String deviceInfo) throws Exception {
    	HidenVO hidenVO = new HidenVO();
    	Map map=new HashMap<>();
   	    hidenVO.setStatus(1);
   	   
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Object pstr= session.getAttribute("profileId");
        if(null==pstr){
        	return getSessionTimeOutStr(1);
        }
        Long profileId = Long.parseLong(pstr.toString());
        if (profileId < 0) {
        	return getSessionTimeOutStr(0);
        }
        ProfileDO profileDO = profileDOMapper.selectByPrimaryKey(profileId);
        //不经过，额外进行Memcache进行辅助轮询获取job列表信息内容
        if(tflag==1){
        	Byte status=profileDO.getAutoReply();
            map.put("userStatus", status);
            List<TaskRecordVo> jobList=deviceUserService.getJobList(deviceInfo,profileId);
            map.put("job", jobList);
            if((byte)1==profileDO.getAutoReply()){
            	List<DeviceInfoDO> dlist= deviceUserService.isHaveThisDevice(profileId,deviceInfo);
            	if(dlist.size()>0){
            		DeviceInfoDO oneModel=dlist.get(0);
            		List<ProfileStatementDo> list= deviceUserService.getListInfoBystatement(profileId,oneModel.getId());
                	map.put("statement", list);
                	map.put("flag",(list.size()>0)?1:0);
                	map.put("contactFlag", oneModel.getContactFlag());
                	//对应设备是可以获取对应的电话簿的标示
            		if(null!=oneModel.getContactFlag() && (byte)1==oneModel.getContactFlag()){
            			int size=doPhoneListService.getTodayCountByDeviceInfo(oneModel.getDeviceInfo(),profileId,oneModel.getWxid());
            			//进行超量的处理
            			if(size<70){
            				List<PhoneInfoVo> phoneList=doPhoneListService.selectPhoneListDataByUserId(profileId,(70-size));
            				map.put("phoneList",phoneList);
            			}
            		}
            	    //信息互聊
            		map.put("talkFlag", (null!=oneModel.getAutoTalk())?(oneModel.getAutoTalk()):(byte)0);
            		if((byte)1 == oneModel.getAutoTalk()){
            			//信息互聊的微信号列表:查找条件是--->根据userId来查找用户列表信息再join微信表内容
                		List<WxNumVo> wxlist=deviceInfoDOMapper.selectWxNumByUserId(profileId);
                		String wxListstr="";
                		if(wxlist.size()>0){
                			for(int k=0;k<wxlist.size();k++){
                    			if(k!=(wxlist.size()-1)){
                    				wxListstr+=(wxlist.get(k).getWxNum()+";");
                    			}else{
                    				wxListstr+=(wxlist.get(k).getWxNum());
                    			}
                    		}
                			//微信集合的添加
                			map.put("wxListstr",wxListstr);
                		}
            		}
            	}
            }
            map.put("nowTime", new Date().getTime());
            //默认添加好友的信息内容
            map.put("defaultName", profileDO.getRealName());
            hidenVO.setMsg("不经过，额外进行Memcache进行辅助轮询获取job列表信息内容");
            hidenVO.setResult(map);
            System.out.println(JSON.toJSONString(hidenVO));
            return JSON.toJSONString(hidenVO);
        }
        
        
        hidenVO.setMsg("Memcache进行辅助轮询获取job列表信息内容");
        Byte ustatus=(byte)0;
        //先判断是否有对应的memcache
        MemcacheVo model=memcachedService.getMemechachedValue(profileId, deviceInfo);
        //1.自动回复的状态
        if(model.getStatus()!=-1){
        	 map.put("userStatus", model.getStatus());
        	 ustatus=(byte)(model.getStatus());
        }else{
        	Byte status=profileDO.getAutoReply();
            map.put("userStatus", status);
            ustatus=status;
            memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,STATUS,status);
        }
        
        //2.任务的数量
        if(model.getTasknum()!=0){
        	List<TaskRecordVo> jobList=deviceUserService.getJobList(deviceInfo,profileId);
            map.put("job", jobList);
            memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,TASKNUM,jobList.size());
        }
        
        //3.statement的回复内容值
        if(model.getStatusnum()!=0){
        	if((byte)1==ustatus){
            	List<DeviceInfoDO> dlist= deviceUserService.isHaveThisDevice(profileId,deviceInfo);
            	if(dlist.size()>0){
            		DeviceInfoDO oneModel=dlist.get(0);
            		List<ProfileStatementDo> list= deviceUserService.getListInfoBystatement(profileId,oneModel.getId());
                	map.put("statement", list);
                	memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,STATEMENTNUM,list.size());
                	memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,STATEMENTFLAG,list.size());
            	}
            }
        }
        //4.状态执行内容
        model=memcachedService.getMemechachedValue(profileId, deviceInfo);
        int flag=model.getStatementflag();
        map.put("nowTime", new Date().getTime());
        map.put("flag",flag);
        map.put("contactFlag", model.getPhoneflag());
        //5.获取对应的电话本信息内容
        if(model.getPhoneflag()!=0){
        	List<DeviceInfoDO> dlist= deviceUserService.isHaveThisDevice(profileId,deviceInfo);
        	if(dlist.size()>0){
        		DeviceInfoDO oneModel=dlist.get(0);
        		//对应设备是可以获取对应的电话簿的标示
        		if((byte)1==oneModel.getContactFlag()){
        			int size=doPhoneListService.getTodayCountByDeviceInfo(oneModel.getDeviceInfo(),profileId,oneModel.getWxid());
        			memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,PHONEFLAG,size);
        			//进行超量的处理
        			if(size<70){
        				List<PhoneInfoVo> phoneList=doPhoneListService.selectPhoneListDataByUserId(profileId,(70-size));
        				map.put("phoneList",phoneList);
        				//当电话话本没有对应的值的时候就再进行设置的缓存内容值
//        				if(phoneList.size()==0){
//        					//特殊的处理
//        					memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,PHONEFLAG,0);
//        				}
        			}
        		}
        	}	
        }
        //6.信息互聊
        map.put("talkFlag", model.getTalkflag());
        int wxhlFlag=model.getTalkflag();
        //7.信息互聊微信号列表：前提是talkFlag=1  && (0已读不处理  -1是未读 ) 未读重新查询
        if(model.getTalkflag() == (-1) || model.getTalkflag() == (0)){
        	//需要先查设备的auto_talk的状态
        	List<DeviceInfoDO> dlist= deviceUserService.isHaveThisDevice(profileId,deviceInfo);
        	DeviceInfoDO oneModel=(dlist.size()>0)?dlist.get(0):null;
        	if(null!=oneModel){
        		map.put("talkFlag", (null!=oneModel.getAutoTalk())?(oneModel.getAutoTalk()):(byte)0);
    			//重新查询是否有对应互聊的标示
    			wxhlFlag=(null!=oneModel.getAutoTalk())?((oneModel.getAutoTalk()==(byte)1)?1:0):0;
    			if(wxhlFlag == 1){
    				//信息互聊的微信号列表:查找条件是--->根据userId来查找用户列表信息再join微信表内容
    	    		List<WxNumVo> wxlist=deviceInfoDOMapper.selectWxNumByUserId(profileId);
    	    		System.out.println(wxlist);
    	    		String wxListstr="";
    	    		if(wxlist.size()>0){
    	    			for(int k=0;k<wxlist.size();k++){
    	    				System.out.println(wxlist.get(k).getWxNum()+"<-------------------->"+wxlist.get(k).getWxId());
    	    				if(null!=wxlist.get(k).getWxNum() && !(Objects.equals("", wxlist.get(k).getWxNum()))){
    	    					if(k!=(wxlist.size()-1)){
    	            				wxListstr+=(wxlist.get(k).getWxNum()+";");
    	            			}else{
    	            				wxListstr+=(wxlist.get(k).getWxNum());
    	            			}
    	    				}
    	        		}
    	    			//微信集合的添加
    	    			map.put("wxListstr",wxListstr);
    	    		}
    			}
        	}
        }
        
        //默认添加好友的信息内容
        map.put("defaultName", profileDO.getRealName());
        hidenVO.setResult(map);
        System.out.println(JSON.toJSONString(hidenVO));
        return JSON.toJSONString(hidenVO);
    }

    
    
    
    
    //进行返回修改对应的Memcached值的状态值的修改
    //再进行添加是否已经接收到微信号列表内容的
    @RequestMapping(value = {"/api/m/1.0/ChangeMemcacheStatus.json"}, produces = "application/json;charset=utf-8")
    public String ChangeMemcacheStatus(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="deviceInfo", required=false) String deviceInfo,
    		@RequestParam(value="ids", required=false) String ids,
    		@RequestParam(value="wxListFlag", required=false,defaultValue="-1") int wxListFlag) throws Exception {
    	HidenVO hidenVO = new HidenVO();
    	Map map=new HashMap<>();
   	    hidenVO.setStatus(1);
   	    hidenVO.setMsg("修改对应的Memcache的值内容");
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Object pstr= session.getAttribute("profileId");
        if(null==pstr){
        	return getSessionTimeOutStr(1);
        }
        Long profileId = Long.parseLong(pstr.toString());
        if (profileId < 0) {
        	return getSessionTimeOutStr(0);
        }
        //获取的id值集合内容[进行把对应的电话簿进行变成已读状态]
        if(null!=ids){
        	String[] idArr=ids.split(";");
            for(String str:idArr){
            	doPhoneListService.updatePhoneList(str);
            }
        }
        //仅仅修改对应的statement与taskNum值
        memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,TASKNUM,0);
        memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,STATEMENTNUM,0);
        memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,PHONEFLAG,0);
        
        //将获取微信号列表进行置为0处理，表示已经处理，则不需要再获取微信号的信息内容
        if(wxListFlag>-1){
        	 memcachedService.setOneTargetMemecachedValue(profileId, deviceInfo,TALKFLAG,1);
        	 System.out.println("互聊返回值为1.....");
        }
        
        return JSON.toJSONString(hidenVO);
    }
    
    
    
    
    //测试获取的memcached的缓存内容
    @RequestMapping(value = {"/test/getMemcacheContent.json"}, produces = "application/json;charset=utf-8")
    public String getMemcacheContent(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="userId", required=false,defaultValue="-1") Long userId) throws Exception {
    	DeviceInfoDOExample example=new DeviceInfoDOExample();
    	example.createCriteria().andUserIdEqualTo(userId);
    	List<DeviceInfoDO> list=deviceInfoDOMapper.selectByExample(example);
    	List<String> slit=new LinkedList<String>();
    	for(DeviceInfoDO de:list){
    		slit.add(de.getDeviceInfo());
    	}
    	List<MemcacheVo> meList= memcachedService.getValueByUserIdAndInfo(userId,slit);
    	Map map=new HashMap();
    	map.put("size", "有效个数为"+meList.size());
    	map.put("data", meList);
    	return JSON.toJSONString(map);
    }
    
    
    
    public String getSessionTimeOutStr(int flag){
    	HidenVO hidenVO = new HidenVO();
    	hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
        hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
        if(flag==1){
        	hidenVO.setMsg("session 失效啦啦啦~");
        }else{
        	hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
        }
        System.out.println(JSON.toJSONString(hidenVO));
        return JSON.toJSONString(hidenVO);
    }

}

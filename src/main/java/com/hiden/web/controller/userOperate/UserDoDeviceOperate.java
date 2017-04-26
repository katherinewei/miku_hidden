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
import com.hiden.biz.service.MemcachedService;
import com.hiden.biz.service.UserService;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.ProfileStatementDoMapper;
import com.hiden.persistence.ProfileWechatDOMapper;
import com.hiden.persistence.TaskAndDeviceDoMapper;
import com.hiden.persistence.WxDeviceTaskJobDoMapper;
import com.hiden.persistence.domain.DeviceInfoDO;
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
import com.hiden.web.model.ResponseResult;
import com.hiden.web.model.TaskRecordVo;
import com.hiden.web.model.UserAgent;

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
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 11-21-2016 You
 *
 */
@RestController
public class UserDoDeviceOperate {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(UserDoDeviceOperate.class);
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
   
    
    //进行操作对应的设备内容操作：微信号+设备基本信息
    @RequestMapping(value = {"/api/m/1.0/addDeviceInfoContent.json", "/api/h/1.0/addDeviceInfoContent.json"}, produces = "application/json;charset=utf-8")
    public String addDeviceInfoContent(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="deviceInfo", required=false) String deviceInfo,
    		@RequestParam(value="deviceName", required=false) String deviceName,
    		@RequestParam(value="lemonName", required=false) String lemonName,
    		@RequestParam(value="wxPic", required=false) String wxPic,
    		@RequestParam(value="openId", required=false) String openId,
    		@RequestParam(value="wxNum", required=false) String wxNum) throws Exception {
    	HidenVO hidenVO = new HidenVO();
   	    hidenVO.setStatus(1);
   	    hidenVO.setMsg("终端登录发起请求.");
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
        Map map=deviceUserService.doDeviceInfoOperate(profileId,deviceInfo,wxNum,deviceName,openId,lemonName,wxPic);
        //进行添加对应的是否状态
        Byte status=profileDO.getAutoReply();
        map.put("userStatus", status);
        int statementSize=0;
        Byte contactFlag=(byte)0;
        Byte talkFlag=(Byte) map.get("talkFlag");
        if((byte)1==status){
        	List<DeviceInfoDO> dlist= deviceUserService.isHaveThisDevice(profileId,deviceInfo);
        	if(dlist.size()>0){
        		DeviceInfoDO oneModel=dlist.get(0);
        		List<ProfileStatementDo> list= deviceUserService.getListInfoBystatement(profileId,oneModel.getId());
            	map.put("statement", list);
            	statementSize=list.size();
            	//进行对电话簿的传值
            	contactFlag=oneModel.getContactFlag();
            	map.put("contactFlag", contactFlag);
        	}
        }
        int jobsize=(int) map.get("jobSize");
        //Memcached设计值内容
        memcachedService.setTargetMemecachedValue(profileId, deviceInfo, (int)status, jobsize,statementSize,contactFlag,talkFlag);
        hidenVO.setResult(map);
        System.out.println(JSON.toJSONString(hidenVO));
        return JSON.toJSONString(hidenVO);
    }
    
    
    
    //轮训获取对应的job列表信息内容
    //进行操作对应的设备内容操作：用户id+设备基本信息
    @RequestMapping(value = {"/api/m/1.0/getJobListData.json", "/api/h/1.0/getJobListData.json"}, produces = "application/json;charset=utf-8")
    public String getJobListData(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="deviceInfo", required=false) String deviceInfo) throws Exception {
    	HidenVO hidenVO = new HidenVO();
   	    hidenVO.setStatus(1);
   	    hidenVO.setMsg("轮询获取job列表信息内容");
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
        Map map=new HashMap<>();
        List<TaskRecordVo> jobList=deviceUserService.getJobList(deviceInfo,profileId);
        map.put("job", jobList);
        Byte status=profileDO.getAutoReply();
        map.put("userStatus", status);
        if((byte)1==status){
        	List<DeviceInfoDO> dlist= deviceUserService.isHaveThisDevice(profileId,deviceInfo);
        	if(dlist.size()>0){
        		DeviceInfoDO oneModel=dlist.get(0);
        		List<ProfileStatementDo> list= deviceUserService.getListInfoBystatement(profileId,oneModel.getId());
            	map.put("statement", list);
        	}
        }
        hidenVO.setResult(map);
        System.out.println(JSON.toJSONString(hidenVO));
        return JSON.toJSONString(hidenVO);
    }

    
    
    //获取对应的用户的记录表内容
    //进行操作对应的设备内容操作：用户id+设备基本信息
    @RequestMapping(value = {"/api/m/1.0/getJobListDataRecord.json", "/api/h/1.0/getJobListDataRecord.json"}, produces = "application/json;charset=utf-8")
    public String getJobListDataRecord(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="pageNo", required=false,defaultValue="0") int pageNo,
    		@RequestParam(value="pageSize", required=false,defaultValue="10") int pageSize,
    		@RequestParam(value="deviceInfo", required=false) String deviceInfo) throws Exception {
    	HidenVO hidenVO = new HidenVO();
   	    hidenVO.setStatus(1);
   	    hidenVO.setMsg("查询job列表信息内容记录列表info");
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Object pstr=session.getAttribute("profileId");
        if(null==pstr){
        	return getSessionTimeOutStr(1);
        }
        Long profileId = Long.parseLong(pstr.toString());
        if (profileId < 0) {
        	return getSessionTimeOutStr(0);
        }
        ProfileDO profileDO = profileDOMapper.selectByPrimaryKey(profileId);
        Map map=new HashMap<>();
        Map tjmap=new HashedMap();
        tjmap.put("info", deviceInfo);
        tjmap.put("userId", profileId);
        tjmap.put("limit", pageSize);
        tjmap.put("offset", (pageSize*pageNo));
        List<TaskRecordVo> list= wxDeviceTaskJobDoMapper.selectTaskListRecordByDeviceInfo(tjmap);
        map.put("recordList", list);
        hidenVO.setResult(map);
        System.out.println(JSON.toJSONString(hidenVO));
        return JSON.toJSONString(hidenVO);
    }
    
    
    
    
    
    //修改对应终端task任务的状态值
    @RequestMapping(value = {"/api/m/1.0/doOneTaskStatus.json", "/api/h/1.0/doOneTaskStatus.json"}, produces = "application/json;charset=utf-8")
    public String doOneTaskStatus(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="taskId", required=false,defaultValue="-1") Long taskId,
    		@RequestParam(value="status", required=false,defaultValue="-3") byte status) throws Exception {
    	HidenVO hidenVO = new HidenVO();
   	    hidenVO.setStatus(1);
   	    hidenVO.setMsg("修改对应终端task任务的状态值");
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Object pstr=session.getAttribute("profileId");
        if(null==pstr){
        	return getSessionTimeOutStr(1);
        }
        Long profileId = Long.parseLong(pstr.toString());
        if (profileId < 0) {
        	return getSessionTimeOutStr(0);
        }
        Long jobId=-1L;
        Byte oldStatus = null,nowStatus = null;
        int size=0;
        TaskAndDeviceDo taskAndDeviceDo=taskAndDeviceDoMapper.selectByPrimaryKey(taskId);
        if(null!=taskAndDeviceDo){
        	 jobId=taskAndDeviceDo.getJobId();
        	 oldStatus=taskAndDeviceDo.getStatus();
        	 taskAndDeviceDo.setLastUpdated(new Date());
             taskAndDeviceDo.setStatus(status);
             size=taskAndDeviceDoMapper.updateByPrimaryKey(taskAndDeviceDo);
             nowStatus=status;
        }
        //进行修改对应的job的统计值内容
        if(jobId>0 && null!=oldStatus && null!=nowStatus && size>0){
        	deviceUserService.changeJobNum(jobId,oldStatus,nowStatus,size);
        }
        
        Map map=new HashMap<>();
        map.put("task", taskAndDeviceDo);
        hidenVO.setResult(map);		
        System.out.println(JSON.toJSONString(hidenVO));
        return JSON.toJSONString(hidenVO);
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

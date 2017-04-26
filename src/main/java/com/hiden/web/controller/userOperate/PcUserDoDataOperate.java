package com.hiden.web.controller.userOperate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hiden.biz.cache.CheckNOGenerator;
import com.hiden.biz.cache.CheckNOValidator;
import com.hiden.biz.common.BizConstants;
import com.hiden.biz.common.BizErrorEnum;
import com.hiden.biz.common.ProfileStatusEnum;
import com.hiden.biz.security.BCrypt;
import com.hiden.biz.security.PasswordParser;
import com.hiden.biz.security.RSAEncrypt;
import com.hiden.biz.service.DeviceUserService;
import com.hiden.biz.service.JobCacheService;
import com.hiden.biz.service.MemcachedService;
import com.hiden.biz.service.UserService;
import com.hiden.persistence.DeviceInfoDOMapper;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.ProfileStatementDoMapper;
import com.hiden.persistence.ProfileWechatDOMapper;
import com.hiden.persistence.TaskAndDeviceDoMapper;
import com.hiden.persistence.WxContactInfoDOMapper;
import com.hiden.persistence.WxDeviceTaskJobDoMapper;
import com.hiden.persistence.WxFriendListMapper;
import com.hiden.persistence.domain.DeviceInfoDO;
import com.hiden.persistence.domain.DeviceInfoDOExample;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileDOExample;
import com.hiden.persistence.domain.ProfileStatementDo;
import com.hiden.persistence.domain.ProfileStatementDoExample;
import com.hiden.persistence.domain.TaskAndDeviceDo;
import com.hiden.persistence.domain.TaskAndDeviceDoExample;
import com.hiden.persistence.domain.WxContactInfoDO;
import com.hiden.persistence.domain.WxContactInfoDOExample;
import com.hiden.persistence.domain.WxDeviceTaskJobDo;
import com.hiden.persistence.domain.WxFriendList;
import com.hiden.persistence.domain.WxFriendListExample;
import com.hiden.tacker.EventTracker;
import com.hiden.utils.EmojiFilter;
import com.hiden.utils.PhenixUserHander;
import com.hiden.utils.TimeUtils;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.PcTaskRecordVo;
import com.hiden.web.model.ResponseResult;
import com.hiden.web.model.TaskRecordVo;
import com.hiden.web.model.UserAgent;
import com.hiden.web.model.WxInfoVo;
import com.hiden.web.model.pcPersonStatement;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 11-21-2016 You
 *extends AbstractJsonpResponseBodyAdvice 
 */
@RestController
public class PcUserDoDataOperate extends AbstractJsonpResponseBodyAdvice {
	
	private static int FLAG=1;
	
	
	public PcUserDoDataOperate() {
        super("callback", "jsonp");
    }

    private static org.slf4j.Logger log = LoggerFactory.getLogger(PcUserDoDataOperate.class);
    @Resource
    private ProfileDOMapper profileDOMapper;
    @Resource
    private DeviceUserService deviceUserService;
    @Resource
    private ProfileStatementDoMapper profileStatementDoMapper;
    @Resource
    private WxDeviceTaskJobDoMapper wxDeviceTaskJobDoMapper;
    @Resource
    private TaskAndDeviceDoMapper taskAndDeviceDoMapper;
    @Resource
    private WxFriendListMapper wxFriendListMapper;
    @Resource 
    private WxContactInfoDOMapper wxContactInfoDOMapper;
    @Resource 
	private DeviceInfoDOMapper deviceInfoDOMapper;
    @Resource
    private MemcachedService memcachedService;
    @Resource
    private JobCacheService jobCacheService;
    
    public static int STATUS=1;
    public static int TASKNUM=2;
    public static int STATEMENTNUM=3;
    
   
    
    
   //根据对应的用户的id来进行获取整个微信列表信息内容
    @RequestMapping(value = {"/pc/h/1.0/getdeviceListByUserId.json"}, produces = "application/json;charset=utf-8")
    public String getdeviceListByUserId(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="pageNo", required=false,defaultValue="0") int pageNo,
//    		@RequestParam(value="profileId", required=false,defaultValue="-1") Long profileId,
    		@RequestParam(value="pageSize", required=false,defaultValue="10") int pageSize) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        Long profileId = (long) session.getAttribute("profileId");
        if (profileId < 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
            hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
            System.out.println(JSON.toJSONString(hidenVO));
            return returnStr(FLAG,hidenVO);
        }
        Map tmmap=new HashedMap();
        tmmap.put("userId", profileId);
        tmmap.put("limit", pageSize);
        tmmap.put("offset",(pageNo*pageSize));
        List<WxInfoVo> list=deviceInfoDOMapper.selectDeviceByUserId(tmmap);
        for(WxInfoVo winfo:list){
        	boolean flag=(profileStatementDoMapper.selectStamentFlag(winfo.getId())>0)?true:false;
        	winfo.setAutoStatus(flag);
        }
        int total=deviceInfoDOMapper.selectTotalCount(profileId);
        Map map=new HashMap<>();
        map.put("data",list);
        map.put("totalSize",total);
        hidenVO.setResult(map);
        return returnStr(FLAG,hidenVO);
    }
    
    //对未开始的朋友圈进行编辑内容值
    @Transactional
    @RequestMapping(value = {"/pc/h/1.0/editOneTaskContent.json"}, produces = "application/json;charset=utf-8")
    public String editOneTaskContent(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="taskId", required=false,defaultValue="-1") Long taskId,
    		@RequestParam(value="taskValue", required=false) String taskValue,
    		@RequestParam(value="startTime", required=false) String startTime,
    		@RequestParam(value="comment", required=false) String comment,
    		@RequestParam(value="address", required=false) String address,
    		@RequestParam(value="taskPic", required=false) String taskPic) throws Exception {
    	 org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
         Session session = currentUser.getSession();
         HidenVO hidenVO = new HidenVO();
         Map map=new HashMap<>();
         hidenVO.setStatus(1);
         Long profileId = (long) session.getAttribute("profileId");
         if (profileId < 0) {
             hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
             hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
             hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
             System.out.println(JSON.toJSONString(hidenVO));
             return returnStr(FLAG,hidenVO);
         }
         WxDeviceTaskJobDo model=wxDeviceTaskJobDoMapper.selectByPrimaryKey(taskId);
         Date time=null;
         if(null!=startTime){
         	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         	time= sdf.parse(startTime);
         }
         //更新对应的job内容
         if(null!=model){
        	 model.setLastUpdated(new Date());
        	 model.setTaskPic(taskPic);
        	 model.setTaskValue(taskValue);
        	 model.setStartTime(time);
        	 model.setComment(comment);
        	 model.setAddress(address);
//        	 wxDeviceTaskJobDoMapper.setUtf8mb4Charat();
        	 wxDeviceTaskJobDoMapper.updateByPrimaryKey(model);
        	 map.put("data",model);
         }
         
         //找到对的设备的ids
         String deviceIds=deviceUserService.getDevicesIdByTaskId(taskId);
         if(deviceIds.length()>0){
        	//Memcache的缓存内容值
             memcachedService.setDevicesMemcachedValue(profileId,deviceIds,TASKNUM);
         }
         hidenVO.setResult(map);
    	 return returnStr(FLAG,hidenVO);
    }
    
    
    
    //添加对应的task内容：增加任务操作[朋友圈  淘宝任务  获取朋友列表信息 自动回复内容的添加]
    @Transactional
    @RequestMapping(value = {"/pc/h/1.0/addOneTaskContent.json"}, produces = "application/json;charset=utf-8")
    public String addOneTaskContent(HttpServletRequest request, HttpServletResponse response,
//    		@RequestParam(value="profileId", required=false,defaultValue="-1") Long profileId,
    		@RequestParam(value="flag", required=false,defaultValue="-1") int flag,
    		@RequestParam(value="deviceIds", required=false) String deviceIds,
    		@RequestParam(value="taskValue", required=false) String taskValue,
    		@RequestParam(value="startTime", required=false) String startTime,
    		@RequestParam(value="comment", required=false) String comment,
    		@RequestParam(value="address", required=false) String address,
    		@RequestParam(value="taskPic", required=false) String taskPic) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        Long profileId = (long) session.getAttribute("profileId");
        if (profileId < 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
            hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
            System.out.println(JSON.toJSONString(hidenVO));
            return returnStr(FLAG,hidenVO);
        }
        Date time=null;
        if(null!=startTime){
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	time= sdf.parse(startTime);
        }
        
        String[] strArr=deviceIds.split(";");
        
       //1:获取微信用户列表信息 2:发朋友圈 3:淘宝任务 4.其他..
        WxDeviceTaskJobDo model=deviceUserService.InsertOneTaskJob(flag,profileId,taskValue,taskPic,time,comment,address,strArr.length);
        switch (flag) {
		case 1:
			model.setTaskName("获取微信用户列表信息 ");
			break;
		case 2:
			model.setTaskName("发朋友圈");
			break;
		case 3:
			model.setTaskName("淘宝任务");
			break;
		case 4:
			model.setTaskName("其他");
			break;
		}
        //进行添加对应的任务内容
//        wxDeviceTaskJobDoMapper.setUtf8mb4Charat();
        wxDeviceTaskJobDoMapper.insert(model);
        
        List<TaskAndDeviceDo> list=new ArrayList<TaskAndDeviceDo>();
    	for(String str:strArr){
    		TaskAndDeviceDo conndata=deviceUserService.InsertOneTaskAndDeviceData(model,Long.parseLong(str));
    		list.add(conndata);
    		//job-cache表的缓存数据存储
//    		jobCacheService.insertOneJobData(conndata);
    	}
    	
        Map map=new HashMap<>();
        map.put("task",list);
        map.put("taskConnList",list);
        hidenVO.setResult(map);
        //Memcache的缓存内容值
        memcachedService.setDevicesMemcachedValue(profileId,deviceIds,TASKNUM);
        return returnStr(FLAG,hidenVO);
    }
 
    
    //添加多个自动回复的内容值
    @RequestMapping(value = {"/pc/h/1.0/insertManyProfileStatemts.json"}, produces = "application/json;charset=utf-8")
    public String insertManyProfileStatemts(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="content", required=false) String content,
    		@RequestParam(value="keyword", required=false) String keyword,
//    		@RequestParam(value="profileId", required=false,defaultValue="-1") Long profileId,
    		@RequestParam(value="deviceIds", required=false) String deviceIds) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        Long profileId = (long) session.getAttribute("profileId");
        if (profileId < 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
            hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
            System.out.println(JSON.toJSONString(hidenVO));
            return returnStr(FLAG,hidenVO);
        }
        String[] strArr=deviceIds.split(";");
        List<Long> longlist=new LinkedList<Long>();
    	for(String str:strArr){
    		 Long deviceId=Long.parseLong(str);
    		 ProfileStatementDo pmodel=deviceUserService.addProStatemt(profileId,deviceId,content,keyword);
    		 longlist.add(pmodel.getId());
    	}
    	pcPersonStatement one=new pcPersonStatement();
    	one.setIds(deviceUserService.getIdListStr(longlist));
    	one.setKeyword(keyword);
    	one.setReplyContent(content);
        Map map=new HashMap<>();
        map.put("statementSize",strArr.length);
        map.put("data",one);
        hidenVO.setResult(map);
        //Memcache的缓存内容值
        memcachedService.setDevicesMemcachedValue(profileId,deviceIds,STATEMENTNUM);
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
    
    //编辑自动回复的内容
    //flag=1为更新  flag=0为删除
    @RequestMapping(value = {"/pc/h/1.0/updateManyProfileStatemts.json"}, produces = "application/json;charset=utf-8")
    public String updateManyProfileStatemts(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="flag", required=false,defaultValue="-1") int flag,
    		@RequestParam(value="content", required=false) String content,
    		@RequestParam(value="keyword", required=false) String keyword,
    		@RequestParam(value="profileId", required=false,defaultValue="-1") Long profileId,
    		@RequestParam(value="sIds", required=false) String sIds) throws Exception {
//        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
//        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
//        Long profileId = (long) session.getAttribute("profileId");
        if (profileId < 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
            hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
            System.out.println(JSON.toJSONString(hidenVO));
            return returnStr(FLAG,hidenVO);
        }
        String[] strArr=sIds.split(";");
        int len=strArr.length;
        int[] intArr=new int[len];
        for(int i=0;i<len;i++){
        	intArr[i]=Integer.parseInt(strArr[i]);
        }
        //Memcache的缓存内容值
        String deviceIds=deviceUserService.getDeviceIdsByStatmentIds(intArr);
        //删除的操作
        if(flag==0){
        	for(String str:strArr){
	       		 Long sId=Long.parseLong(str);
	       		 profileStatementDoMapper.deleteByPrimaryKey(sId);
        	}
        }
        //更新操作
        else if(flag==1){
        	for(String str:strArr){
	       		 Long sId=Long.parseLong(str);
	       		 ProfileStatementDo p=profileStatementDoMapper.selectByPrimaryKey(sId);
	       		 p.setLastUpdated(new Date());
	       		 p.setKeyword(keyword);
	       		 p.setReplyContent(content);
	       		 profileStatementDoMapper.updateByPrimaryKey(p);
        	}
        }
        
    	memcachedService.setDevicesMemcachedValue(profileId,deviceIds,STATEMENTNUM);
        Map map=new HashMap<>();
        map.put("statementSize",strArr.length);
        hidenVO.setResult(map);
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
    //用户取消个人自动回复的功能
    @RequestMapping(value = {"/pc/h/1.0/changePersonAuto.json"}, produces = "application/json;charset=utf-8")
    public String changePersonAuto(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="profileId", required=false,defaultValue="-1") Long profileId,
    		@RequestParam(value="autoStatus", required=false,defaultValue="1") Byte autoStatus) throws Exception {
//        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
//        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
//        Long profileId = (long) session.getAttribute("profileId");
        if (profileId < 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
            hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
            System.out.println(JSON.toJSONString(hidenVO));
            return returnStr(FLAG,hidenVO);
        }
        ProfileDO profileDO=profileDOMapper.selectByPrimaryKey(profileId);
        profileDO.setAutoReply(autoStatus);
        profileDO.setLastUpdated(new Date());
        int size=profileDOMapper.updateByPrimaryKey(profileDO);
        Map map=new HashMap<>();
        map.put("flag",(size>1)?true:false);
        hidenVO.setResult(map);
        //Memcache的缓存内容值
        //deviceIds的集合
        String deviceIds=deviceInfoDOMapper.selectDeviceIdsByUserId(profileId);
        if(deviceIds!=null){
        	memcachedService.setDevicesAutoFlagMemcachedValue(profileId,deviceIds.replace(",", ";"),autoStatus);
        }
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
    //用户取消个人对应终端的自动回复的功能【终端的集合】
    @RequestMapping(value = {"/pc/h/1.0/changeDeviceRelyAuto.json"}, produces = "application/json;charset=utf-8")
    public String changeDeviceRelyAuto(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="deviceIds", required=false) String deviceIds,
    		@RequestParam(value="autoStatus", required=false,defaultValue="1") Byte autoStatus) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Long profileId = (long) session.getAttribute("profileId");
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        int totalSize=0;
        String[] strArr=deviceIds.split(";");
    	for(String str:strArr){
    		ProfileStatementDoExample example=new ProfileStatementDoExample();
            example.createCriteria().andDeviceIdEqualTo(Long.parseLong(str));
            List<ProfileStatementDo> profilelist=profileStatementDoMapper.selectByExample(example);
            for(ProfileStatementDo p:profilelist){
            	p.setLastUpdated(new Date());
            	p.setStatus(autoStatus);
            	int size=profileStatementDoMapper.updateByPrimaryKey(p);
            	totalSize+=size;
            }
    	}
        Map map=new HashMap<>();
        map.put("size",totalSize);
        hidenVO.setResult(map);
        //Memcache的缓存内容值
        memcachedService.setDevicesAutoFlagMemcachedValue(profileId,deviceIds,autoStatus);
        return returnStr(FLAG,hidenVO);
    }
    
   
    
    //根据用户id与终端的id集合来查找全部自动回复的内容值
    @RequestMapping(value = {"/pc/h/1.0/selectAutoContents.json"}, produces = "application/json;charset=utf-8")
    public String selectAutoContents(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="deviceIds", required=false) String deviceIds,
    		@RequestParam(value="userId", required=false,defaultValue="-1") Long userId,
    		@RequestParam(value="pageNo", required=false,defaultValue="0") int pageNo,
    		@RequestParam(value="pageSize", required=false,defaultValue="10") int pageSize) throws Exception {
    	String[] strArr=deviceIds.split(";");
 	    int[] intids=new int[strArr.length];
 	    for(int i=0;i<strArr.length;i++){
 	    	intids[i]=Integer.valueOf(strArr[i]);
 	    }
 	    Map tmap=new HashMap<>();
 	    tmap.put("ids", intids);
 	    tmap.put("limit", pageSize);
 	    tmap.put("offset", pageNo);
 	    List<ProfileStatementDo> list=profileStatementDoMapper.selectInfoListByDeviceId(tmap);
 	    List<pcPersonStatement>  ptlist=new LinkedList<pcPersonStatement>();
 	    if(strArr.length>1){
 	    	//进行过滤数据内容
 	    	List<ProfileStatementDo> contetntlist= profileStatementDoMapper.selectGroupByContent(tmap);
 	    	ptlist=deviceUserService.getqcStatementContent(list,contetntlist,intids);
 	    }else{
 	    	for(ProfileStatementDo p:list){
 	    		pcPersonStatement pmodel=deviceUserService.getpcPersonStatement(p);
 	    		pmodel.setIds(p.getId().toString());
 	    		ptlist.add(pmodel);
 	    	}
 	    }
 	    //进行截取对应的内容值
 	    //进行设置对应的值内容
 	    int size=(ptlist.size()>(pageNo+pageSize))?(pageNo+pageSize):ptlist.size();
 	    boolean flag=(ptlist.size()>=(pageNo+pageSize))?true:false;
 	    int begin=(ptlist.size()>(pageNo))?(pageNo):-1;
 	    List<pcPersonStatement>  newptlist=new LinkedList<pcPersonStatement>();
 	    if(begin!=-1){
 	    	for(int i=pageNo;i<size;i++){
 	 	    	newptlist.add(ptlist.get(i));
 	 	    }
 	    }
        Map map=new HashMap<>();
        map.put("hasNext",flag);
        map.put("statement",newptlist);
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        hidenVO.setResult(map);
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
    
    
    //根据用户id来查找对应的用户任务列表信息内容
    //执行状态:-1失败 0=未开始 1成功 2异常 -2删掉
    @RequestMapping(value = {"/pc/h/1.0/selectPcTaskRecordInfoByUserId.json"}, produces = "application/json;charset=utf-8")
    public String selectPcTaskRecordInfoByUserId(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="status", required=false) Byte status,
    		@RequestParam(value="userId", required=false,defaultValue="-1") Long userId,
    		@RequestParam(value="pageNo", required=false,defaultValue="0") int pageNo,
    		@RequestParam(value="pageSize", required=false,defaultValue="10") int pageSize) throws Exception {
    	Map tmmap=new HashedMap();
        tmmap.put("userId", userId);
        tmmap.put("limit", pageSize);
        tmmap.put("offset",(pageNo*pageSize));
        if(null!=status)
        	tmmap.put("status", status);
//    	List<PcTaskRecordVo> list= wxDeviceTaskJobDoMapper.selectPcTaskRecordInfoByUserIdInf(tmmap);
    	List<PcTaskRecordVo> list= wxDeviceTaskJobDoMapper.selectPcTaskRecordInfoByUserIdInfPlusPlus(tmmap);
    	list= deviceUserService.getFinalTaskRecordList(list);
        int totalsize= wxDeviceTaskJobDoMapper.selectTotalSizeByStatus(tmmap);
        Map map=new HashMap<>();
        map.put("list",list);
        map.put("totalsize",totalsize);
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        hidenVO.setResult(map);
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
    
    
    
    
    
    
    //一批task从一个任务状态转变成另外一种状态
    //执行状态:-1失败 0=未开始 1成功 2异常 -2删掉
    @RequestMapping(value = {"/pc/h/1.0/changeManyTaskStatus.json"}, produces = "application/json;charset=utf-8")
    public String changeManyTaskStatus(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="taskIds", required=false) String taskIds,
    		@RequestParam(value="status", required=false,defaultValue="-5") Byte status) throws Exception {
    	org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Long profileId = (long) session.getAttribute("profileId");
    	String[]  jDataStr=taskIds.split("#");
    	int size=0;
    	Byte oldStatus=(byte)-1;
    	Long jobId=-1L;
    	List<String> strlist=new ArrayList<String>();
    	for(String dtaskId:jDataStr){
    		String[] strArr=dtaskId.split(";");
        	for(String str:strArr){
        		Long taskId=Long.parseLong(str);
        		TaskAndDeviceDo taskAndDeviceDo=taskAndDeviceDoMapper.selectByPrimaryKey(taskId);
        		if(null==taskAndDeviceDo)
        			continue;
        		//获取的oldstatus
        		if(size==0){
        			oldStatus=taskAndDeviceDo.getStatus();
        			jobId=taskAndDeviceDo.getJobId();
        		}
        		int usize=0;
        		//删除内容进行删除处理
        		if((-2)==status){
        			usize=taskAndDeviceDoMapper.deleteByPrimaryKey(taskId);
        		}else{
        			taskAndDeviceDo.setLastUpdated(new Date());
                	taskAndDeviceDo.setStatus(status);
                	strlist.add(String.valueOf(taskAndDeviceDo.getDeviceId()));
                	usize=taskAndDeviceDoMapper.updateByPrimaryKey(taskAndDeviceDo);
        		}
            	//当status设置为0的未开始时候是否对朋友圈进行
            	if(status==(byte)0){
            		deviceUserService.doChangeStatusToJob(taskAndDeviceDo.getJobId());
            	}
            	size+=usize;
        	}
    	}
    	//进行对job的数量的设置
//    	deviceUserService.changeJobNum(jobId,oldStatus,status,size);
    	wxDeviceTaskJobDoMapper.updateJobNums(jobId);
        Map map=new HashMap<>();
        map.put("size",size);
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        hidenVO.setResult(map);
        hidenVO.setMsg("修改了"+size+"条信息");
        //Memcache的缓存内容值
        //获取对应的deviceIds
        if(strlist.size()>0){
        	  String deviceIds=deviceUserService.getFinalDevides(strlist);
              if(deviceIds!=null){
              	memcachedService.setDevicesMemcachedValue(profileId,deviceIds,TASKNUM);
              }
        }
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
    
    
    //根据用户id查找对应的微信朋友圈信息用户列表内容
    @RequestMapping(value = {"/pc/h/1.0/selectWxListByUserId.json"}, produces = "application/json;charset=utf-8")
    public String selectWxListByUserId(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="userId", required=false,defaultValue="-1") Long userId,
    		@RequestParam(value="pageNo", required=false,defaultValue="0") int pageNo,
    		@RequestParam(value="pageSize", required=false,defaultValue="10") int pageSize) throws Exception {
    	WxContactInfoDOExample wxExample=new WxContactInfoDOExample();
    	wxExample.createCriteria().andUserIdEqualTo(userId);
    	wxExample.setOrderByClause("last_updated  desc");
    	wxExample.setLimit(pageSize);
    	wxExample.setOffset(pageNo*pageSize);
    	List<WxContactInfoDO> wxList=wxContactInfoDOMapper.selectByExample(wxExample);
    	int totalSize=wxContactInfoDOMapper.selectTotalCount(userId);
        Map map=new HashMap<>();
        map.put("list", wxList);
        map.put("totalSize", totalSize);
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        hidenVO.setResult(map);
        return returnStr(FLAG,hidenVO);
    }
    
    //根据信息用户列表内容获取对应朋友圈信息内容
    @RequestMapping(value = {"/pc/h/1.0/selectFriendListByWxId.json"}, produces = "application/json;charset=utf-8")
    public String selectFriendListByWxId(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="userId", required=false,defaultValue="-1") Long userId,
    		@RequestParam(value="wxId", required=false,defaultValue="-1") Long wxId,
    		@RequestParam(value="pageNo", required=false,defaultValue="0") int pageNo,
    		@RequestParam(value="pageSize", required=false,defaultValue="10") int pageSize) throws Exception {
    	WxFriendListExample wxExample=new WxFriendListExample();
    	wxExample.createCriteria().andUserIdEqualTo(userId).andWxIdEqualTo(wxId);
    	wxExample.setOrderByClause("last_updated  desc");
    	wxExample.setLimit(pageSize);
    	wxExample.setOffset(pageNo*pageSize);
    	List<WxFriendList> wxList=wxFriendListMapper.selectByExample(wxExample);
//    	int totalSize=wxFriendListMapper.selectTotalCount(userId);
        Map map=new HashMap<>();
        map.put("list", wxList);
        if(wxList.size()>=pageSize){
   		 map.put("hasNext", true);
        }else{
         map.put("hasNext", false);
        }
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        hidenVO.setResult(map);
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
   //根据信息用户列表内容获取对应朋友圈信息内容
    @RequestMapping(value = {"/pc/h/1.0/enableUserId.json"}, produces = "application/json;charset=utf-8")
    public String enableUserId(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Object pstr= session.getAttribute("profileId");
        HidenVO hidenVO = new HidenVO();
        Map map=new HashMap<>();
        if(null==pstr){
        	map.put("flag", 0);
        	hidenVO.setMsg("会话时间失效");
        }else{
        	map.put("flag", 1);
        	hidenVO.setMsg("当前用户有效");
        }
        hidenVO.setStatus(1);
        hidenVO.setResult(map);
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
    
    //删除对应的微信关联内容信息
    @Transactional
    @RequestMapping(value = {"/pc/h/1.0/deleteOneDivcByWxId.json"}, produces = "application/json;charset=utf-8")
    public String deleteOneDivcByWxId(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="userId", required=false,defaultValue="-1") Long userId,
    		@RequestParam(value="wxId", required=false,defaultValue="-1") Long wxId) throws Exception {
    	DeviceInfoDOExample deDoExample=new DeviceInfoDOExample();
    	deDoExample.createCriteria().andWxidEqualTo(wxId).andUserIdEqualTo(userId);
    	List<DeviceInfoDO> dlist=deviceInfoDOMapper.selectByExample(deDoExample);
    	List<String> infoList=new ArrayList<String>();
    	int size=0;
    	if(dlist.size()>0){
    		DeviceInfoDO deviceInfoDO=dlist.get(0);
    		infoList.add(deviceInfoDO.getDeviceInfo());
    		size=deviceInfoDOMapper.deleteByPrimaryKey(deviceInfoDO.getId());
    		TaskAndDeviceDoExample example=new TaskAndDeviceDoExample();
    		example.createCriteria().andDeviceIdEqualTo(deviceInfoDO.getId());
    		List<TaskAndDeviceDo> list=taskAndDeviceDoMapper.selectByExample(example);
    		for(TaskAndDeviceDo ts:list){
    			//进行对应的job统计值的修改
//    			deviceUserService.changeJobNum(ts.getJobId(),ts.getStatus(),(byte)-2,1);
    			taskAndDeviceDoMapper.deleteByPrimaryKey(ts.getId());
    			wxDeviceTaskJobDoMapper.updateJobNums(ts.getJobId());
    		}
    	}
        Map map=new HashMap<>();
        map.put("size", size);
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        hidenVO.setResult(map);
        //删掉对应的缓存内容信息
        if(infoList.size()>0){
        	 memcachedService.deleteMemcacheContent(userId,infoList);
        }
        return returnStr(FLAG,hidenVO);
    }
    
    
    
    
    
    
    
    
    
    
    public String returnStr(int flag,HidenVO hidenVO){
    	if(flag==1){
    		 System.out.println(JSON.toJSONString(hidenVO));
    		return JSON.toJSONString(hidenVO);
    	}else{
    		 System.out.println("Callback("+JSON.toJSONString(hidenVO)+")");
    		return "Callback("+JSON.toJSONString(hidenVO)+")";
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}

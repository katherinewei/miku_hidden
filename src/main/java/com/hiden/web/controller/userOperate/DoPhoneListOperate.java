package com.hiden.web.controller.userOperate;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.hiden.biz.common.BizErrorEnum;
import com.hiden.biz.service.DeviceUserService;
import com.hiden.biz.service.DoPhoneListService;
import com.hiden.biz.service.MemcachedService;
import com.hiden.persistence.DeviceInfoDOMapper;
import com.hiden.persistence.PhoneBookListDoMapper;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.WxContactInfoDOMapper;
import com.hiden.persistence.WxDeviceTaskJobDoMapper;
import com.hiden.persistence.WxPhoneResultDoMapper;
import com.hiden.persistence.domain.DeviceInfoDO;
import com.hiden.persistence.domain.DeviceInfoDOExample;
import com.hiden.persistence.domain.PhoneBookListDo;
import com.hiden.persistence.domain.PhoneBookListDoExample;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileStatementDoExample;
import com.hiden.persistence.domain.WxContactInfoDO;
import com.hiden.persistence.domain.WxDeviceTaskJobDo;
import com.hiden.persistence.domain.WxPhoneResultDo;
import com.hiden.persistence.domain.WxPhoneResultDoExample;
import com.hiden.utils.ExcelUtils;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.PcJobListVo;
import com.hiden.web.model.PcTaskRecordVo;
@RestController
public class DoPhoneListOperate {
	
	
	@Resource
	PhoneBookListDoMapper phoneBookListDoMapper;
	@Resource
	WxPhoneResultDoMapper wxPhoneResultDoMapper;
	@Resource
	DoPhoneListService doPhoneListService;
	@Resource
	MemcachedService memcachedService;
	@Resource
	ProfileDOMapper profileDOMapper;
	@Resource
	WxContactInfoDOMapper wxContactInfoDOMapper;
	@Resource
	DeviceInfoDOMapper deviceInfoDOMapper;
	@Resource
	DeviceUserService deviceUserService;
	@Resource
	WxDeviceTaskJobDoMapper wxDeviceTaskJobDoMapper;
	 
	private static org.slf4j.Logger log = LoggerFactory.getLogger(DoPhoneListOperate.class);
	/********************************************app端的内容*******************************************************/
	//ids
	//-->id#type#isenable;id#type#isenable;id#type#isenable
	//内容说明:id为数据的电话簿的id  isenable :是否有效[1有效 -1处理过但无效]  type:电话号码的类型:1搜索类型 2添加通讯录
	@RequestMapping(value = {"/api/m/1.0/changePhoneListStatus.json"}, produces = "application/json;charset=utf-8")
	public String changePhoneListStatus(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="wxId", required=false,defaultValue="-1") Long wxId,
			@RequestParam(value="deviceInfo", required=false) String deviceInfo,
    		@RequestParam(value="ids", required=false) String ids) throws Exception {
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Object pstr= session.getAttribute("profileId");
        if(null==pstr){
        	return getSessionTimeOutStr(1);
        }
        Long profileId = Long.parseLong(pstr.toString());
        WxContactInfoDO wxdata=wxContactInfoDOMapper.selectByPrimaryKey(wxId);
        if(null!=wxdata){
        	String[] strArr=ids.split(";");
    		for(String str:strArr){
    			String[] dataArr=str.split("#");
    			if(dataArr.length==3){
    				PhoneBookListDo phoneBookListDo= doPhoneListService.updateTwoPointPhoneList(dataArr[0],dataArr[1],dataArr[2]);
        			//3个属性才能进行对应的设置值
        			doPhoneListService.addOneWxPhoneResult(phoneBookListDo.getId(), deviceInfo,wxdata,profileId);
    			}
    		}
        }
    	HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        Map map=new HashMap<>();
        map.put("msg", "更改对应的通讯电话本信息内容");
        hidenVO.setResult(map);
		return JSON.toJSONString(hidenVO);
	}
	
	//-->id#wxName;id#wxName;id#wxName
	//内容说明:id为数据的电话簿的id  wxName:添加成功的微信名称内容
	@RequestMapping(value = {"/api/m/1.0/changePhonetoSuccess.json"}, produces = "application/json;charset=utf-8")
	public String changePhonetoSuccess(HttpServletRequest request, HttpServletResponse response,
	    		@RequestParam(value="ids", required=false) String ids,
	    		@RequestParam(value="wxName", required=false) String wxName) throws Exception {
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
	        Session session = currentUser.getSession();
	        Object pstr= session.getAttribute("profileId");
	        if(null==pstr){
	        	return getSessionTimeOutStr(1);
	        }
	        Long profileId = Long.parseLong(pstr.toString());
	        String[] strArr=ids.split(";");
	    	for(String str:strArr){
	    			String[] dataArr=str.split("#");
	    			if(dataArr.length==2){
	    				Long pId=Long.parseLong(dataArr[0]);
	    				PhoneBookListDo phoneBookListDo=phoneBookListDoMapper.selectByPrimaryKey(pId);
	    				phoneBookListDo.setLastUpdated(new Date());
	    				phoneBookListDo.setIsSuccess((byte)1);
	    				phoneBookListDoMapper.updateByPrimaryKey(phoneBookListDo);
	    				
	    				//名称的添加
	    				WxPhoneResultDoExample example=new WxPhoneResultDoExample();
	    				example.createCriteria().andPhoneIdEqualTo(pId).andUserIdEqualTo(profileId);
	    				List<WxPhoneResultDo> list=wxPhoneResultDoMapper.selectByExample(example);
	    				if(list.size()>0){
	    					WxPhoneResultDo model=list.get(0);
	    					model.setWxName(wxName);
	    					model.setLastUpdated(new Date());
	    					wxPhoneResultDoMapper.updateByPrimaryKey(model);
	    				}
	    			}
	    	}
	    	HidenVO hidenVO = new HidenVO();
	        hidenVO.setStatus(1);
	        Map map=new HashMap<>();
	        map.put("msg", "更改对应的通讯电话本信息内容");
	        hidenVO.setResult(map);
			return JSON.toJSONString(hidenVO);
		}
	
	
	
	
	/********************************************pc端的内容********************************************************/
	//根据用户id来查找对应的用户任务列表信息内容[selectfinalJobList]
    //执行状态:-1失败 0=未开始 1成功 2异常 -2删掉
    @RequestMapping(value = {"/pc/h/1.0/selectfinalJobList.json"}, produces = "application/json;charset=utf-8")
    public String selectfinalJobList(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="status", required=false) Byte status,
    		@RequestParam(value="userId", required=false,defaultValue="-1") Long userId,
    		@RequestParam(value="pageNo", required=false,defaultValue="0") int pageNo,
    		@RequestParam(value="pageSize", required=false,defaultValue="10") int pageSize) throws Exception {
    	Map tmmap=new HashedMap();
        tmmap.put("userId", userId);
        tmmap.put("limit", pageSize);
        tmmap.put("offset",(pageNo*pageSize));
        tmmap=deviceUserService.getTargetMapValue(status,tmmap);
        List<PcJobListVo> plist=new LinkedList<PcJobListVo>(); 
        int totalsize=0;
        //对应的job的list内容值
        List<WxDeviceTaskJobDo> list=wxDeviceTaskJobDoMapper.selectPcFinalJobList(tmmap);
        if(list.size()>0){
        	 Long[] iarr=new Long[list.size()];
             for(int i=0;i<list.size();i++){
             	iarr[i]=list.get(i).getId();
             }
             tmmap.put("ids", iarr);
             List<PcTaskRecordVo> targetList=wxDeviceTaskJobDoMapper.selectPcFinalJobDetailList(tmmap);
             
             for(WxDeviceTaskJobDo wx:list){
             	PcJobListVo model=new PcJobListVo();
             	model.setJob(wx);
             	List<PcTaskRecordVo> oneList=new LinkedList<PcTaskRecordVo>();
             	for(PcTaskRecordVo pcmodel:targetList){
             		if(pcmodel.getJobId().toString().equals(wx.getId().toString())){
             			oneList.add(pcmodel);
             		}
             	}
             	model.setList(oneList);
             	plist.add(model);
             }
             totalsize= wxDeviceTaskJobDoMapper.selectPcFinalJobListSize(tmmap);
        }
        Map map=new HashMap<>();
        map.put("list",plist);
        map.put("totalsize",totalsize);
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        hidenVO.setResult(map);
        return JSON.toJSONString(hidenVO);
    }
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 进行对上传的excel进行读取对应的电话号码
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/pc/h/1.0/wxuploadExcel.json"}, produces = "application/json;charset=utf-8")
	public String wxuploadExcel(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="userId", required=false,defaultValue="1") Long userId,
			@RequestParam(value = "file", required=true) MultipartFile file // 关键就是这句话起了作用
			) throws Exception {
		int size=0;
		InputStream inputStream;
		//不为空的文件进行操作
		if (null != file && !file.isEmpty()) {
			inputStream = file.getInputStream();
			List<String> list=ExcelUtils.getOneColumData(inputStream, "手机");
			if(list.size()>0){
				System.out.println("上传成功~~~");
				for(int i=0,len=list.size();i<len;i++){
					size+=doPhoneListService.addOnePhoneData(userId, list.get(i)); 
				}
			}
		}
		//进行缓存的设置内容
		memcachedService.SetPhoneListCache(userId,size);
		HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        Map map=new HashMap<>();
        map.put("size", size);
        hidenVO.setResult(map);
        System.out.println(JSON.toJSONString(hidenVO));
		return JSON.toJSONString(hidenVO);
	}
	
	
	
	//改变对应的默认值内容
	@RequestMapping(value = {"/pc/h/1.0/changeDefualtName.json"}, produces = "application/json;charset=utf-8")
	public String changeDefualtName(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="defaultName", required=false) String defaultName) throws Exception {
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        Object pstr= session.getAttribute("profileId");
        if(null==pstr){
        	return getSessionTimeOutStr(1);
        }
        Long profileId = Long.parseLong(pstr.toString());
        ProfileDO profileDO=profileDOMapper.selectByPrimaryKey(profileId);
        profileDO.setRealName(defaultName);
        profileDOMapper.updateByPrimaryKey(profileDO);
    	HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        Map map=new HashMap<>();
        map.put("defaultName", defaultName);
        hidenVO.setResult(map);
		return JSON.toJSONString(hidenVO);
	}
	
	
	
	
	    //改变电话簿的电话内容状态
		//是否已经读取过[0没有读取 1读取 -1为删除的内容]---->仅能是读取过的，但是isEnabled=0 和  isSuccess=0
		@RequestMapping(value = {"/pc/h/1.0/changePhoneListStatus.json"}, produces = "application/json;charset=utf-8")
		public String changePhoneListStatus(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(value="phoneIds", required=false) String phoneIds,
				@RequestParam(value="status", required=false,defaultValue="-2") Byte status
				) throws Exception {
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
	        Session session = currentUser.getSession();
	        Object pstr= session.getAttribute("profileId");
	        if(null==pstr){
	        	return getSessionTimeOutStr(1);
	        }
	        Long profileId = Long.parseLong(pstr.toString());
	        String[] strArr=phoneIds.split(";");
	        int size=0;
	        if(status==(byte)(-2)){
	        	return "no paramater.";
	        }
	    	for(String str:strArr){
	    		if((byte)(-1)==status){
	    			size+=phoneBookListDoMapper.deleteByPrimaryKey(Long.parseLong(str));
	    		}else{
	    			PhoneBookListDo phone=phoneBookListDoMapper.selectByPrimaryKey(Long.parseLong(str));
	    			phone.setIsRead(status);
	    			phone.setLastUpdated(new Date());
	    			phoneBookListDoMapper.updateByPrimaryKey(phone);
	    		}
	    	}
	    	HidenVO hidenVO = new HidenVO();
	        hidenVO.setStatus(1);
	        Map map=new HashMap<>();
	        map.put("size", size);
	        hidenVO.setResult(map);
			return JSON.toJSONString(hidenVO);
		}
		
		
		//进行查看对应详情内容
		@RequestMapping(value = {"/pc/h/1.0/getPhoneDetailContent.json"}, produces = "application/json;charset=utf-8")
		public String getPhoneDetailContent(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(value="phoneId", required=false,defaultValue="-1") Long phoneId
				) throws Exception {
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
	        Session session = currentUser.getSession();
	        Object pstr= session.getAttribute("profileId");
	        if(null==pstr){
	        	return getSessionTimeOutStr(1);
	        }
	        Long profileId = Long.parseLong(pstr.toString());
	        WxPhoneResultDoExample example=new WxPhoneResultDoExample();
	        example.createCriteria().andPhoneIdEqualTo(phoneId);
	        List<WxPhoneResultDo> list=wxPhoneResultDoMapper.selectByExample(example);
	    	HidenVO hidenVO = new HidenVO();
	        hidenVO.setStatus(1);
	        Map map=new HashMap<>();
	        if(list.size()>0){
	        	map.put("data", list.get(0));
	        }else{
	        	map.put("msg", "nodata!");
	        }
	        hidenVO.setResult(map);
			return JSON.toJSONString(hidenVO);
		}
	
	
	
	//改变用户电话簿的记录内容
	@RequestMapping(value = {"/pc/h/1.0/changeDevicePhoneFlag.json"}, produces = "application/json;charset=utf-8")
	public String changeDevicePhoneFlag(HttpServletRequest request, HttpServletResponse response,
			    @RequestParam(value="deviceIds", required=false) String deviceIds,
	    		@RequestParam(value="status", required=false,defaultValue="-1") Byte status,
	    		@RequestParam(value="talkStatus", required=false,defaultValue="-1") Byte talkStatus) throws Exception {
			org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
	        Session session = currentUser.getSession();
	        Object pstr= session.getAttribute("profileId");
	        if(null==pstr){
	        	return getSessionTimeOutStr(1);
	        }
	        Long profileId = Long.parseLong(pstr.toString());
	        String[] strArr=deviceIds.split(";");
	        int size=0;
	    	for(String str:strArr){
	    		 DeviceInfoDO deviceInfoDO=deviceInfoDOMapper.selectByPrimaryKey(Long.parseLong(str));
	 	         if(null!=deviceInfoDO && status!=(byte)(-1)){
	 	        	deviceInfoDO.setLastUpdated(new Date());
	 	        	deviceInfoDO.setContactFlag(status);
	 	        	size+=deviceInfoDOMapper.updateByPrimaryKey(deviceInfoDO);
	 	        }
	 	        if(null!=deviceInfoDO && talkStatus!=(byte)(-1)){
	 	        	deviceInfoDO.setLastUpdated(new Date());
	 	        	deviceInfoDO.setAutoTalk(talkStatus);
	 	        	size+=deviceInfoDOMapper.updateByPrimaryKey(deviceInfoDO);
	 	        	
	 	        	//查找全部的设备
	 	        	DeviceInfoDOExample example=new DeviceInfoDOExample();
	 	        	example.createCriteria().andUserIdEqualTo(profileId);
	 	        	List<DeviceInfoDO> deviceList=deviceInfoDOMapper.selectByExample(example);
	 	        	for(DeviceInfoDO demodel:deviceList){
	 	        		//清空缓存让它来重新查
		 	        	//设置缓存:自动对话标示
	        			memcachedService.setOneTargetMemecachedValue(profileId, demodel.getDeviceInfo(),6,(-1));
	        			//设置缓存:自动对话数量的标示
	        			memcachedService.setOneTargetMemecachedValue(profileId, demodel.getDeviceInfo(),7,(-1));
	 	        	}
	 	        }
	    	}
	    	HidenVO hidenVO = new HidenVO();
	        hidenVO.setStatus(1);
	        Map map=new HashMap<>();
	        map.put("status", status);
	        map.put("talkStatus", talkStatus);
	        map.put("size", size);
	        hidenVO.setResult(map);
			return JSON.toJSONString(hidenVO);
		}
	
	
	
	/**
	 * 查询出对应的用户内容列表信息:时间的倒叙
	 * @param request
	 * @param response
	 * @param userId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = {"/pc/h/1.0/getPhoneListData.json"}, produces = "application/json;charset=utf-8")
	public String getPhoneListData(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="userId", required=false,defaultValue="-1") Long userId,
			@RequestParam(value="pageNo", required=false,defaultValue="0") int pageNo,
    		@RequestParam(value="pageSize", required=false,defaultValue="10") int pageSize) throws Exception {
		List<PhoneBookListDo> plist= doPhoneListService.getPhoneList(userId,pageNo,pageSize);
		int totalsize=phoneBookListDoMapper.selectTotalCount(userId);
    	HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(1);
        Map map=new HashMap<>();
        map.put("data", plist);
        map.put("totalsize", totalsize);
        hidenVO.setResult(map);
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

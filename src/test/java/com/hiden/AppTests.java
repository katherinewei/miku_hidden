package com.hiden;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.hiden.persistence.DeviceInfoDOMapper;
import com.hiden.persistence.PhoneBookListDoMapper;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.ProfileStatementDoMapper;
import com.hiden.persistence.TaskAndDeviceDoMapper;
import com.hiden.persistence.WxContactInfoDOMapper;
import com.hiden.persistence.WxDeviceTaskJobDoMapper;
import com.hiden.persistence.WxPhoneResultDoMapper;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileStatementDo;
import com.hiden.persistence.domain.WxDeviceTaskJobDo;
import com.hiden.persistence.domain.WxDeviceTaskJobDoExample;
import com.hiden.web.model.PcTaskRecordVo;
import com.hiden.web.model.TaskRecordVo;
import com.hiden.web.model.WxInfoVo;
import com.hiden.web.model.WxNumVo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/mybatis/mybatis-applicationContext.xml"})
@ActiveProfiles("dev")
public class AppTests{
	
	
	@Resource
	private ProfileDOMapper profileDOMapper;
	@Resource
    private WxDeviceTaskJobDoMapper wxDeviceTaskJobDoMapper;
	@Resource 
    private WxContactInfoDOMapper wxContactInfoDOMapper;
	@Resource 
	private DeviceInfoDOMapper deviceInfoDOMapper;
	
	@Resource 
	private ProfileStatementDoMapper profileStatementDoMapper;
	@Resource
	PhoneBookListDoMapper phoneBookListDoMapper;
	@Resource
	WxPhoneResultDoMapper wxPhoneResultDoMapper;
	@Resource 
	private TaskAndDeviceDoMapper taskAndDeviceDoMapper;
	
	
	
	
	@Test
	public void testInFo(){
		List<WxNumVo> list=deviceInfoDOMapper.selectWxNumByUserId(79275L);
		System.out.println(JSON.toJSONString(list));
	}
	
	
    @Test
    public void simple() throws Exception {
//     ProfileDO p=profileDOMapper.selectByPrimaryKey(79268L);
//     System.out.println(p.getPassword());
//     List<WxDeviceTaskJobDo> list= wxDeviceTaskJobDoMapper.selectTaskByDeviceInfo("xxxx");
//     System.out.println(list.size());
       Map map=new HashedMap();
       map.put("deviceInfo", "867831024316788");
      
       map.put("limit", 70);
       map.put("offset", 0);
       map.put("status", -1);
//       {info=867831024316788, limit=10, userId=79269, offset=0}
//       List<TaskRecordVo> list= wxDeviceTaskJobDoMapper.selectTaskListRecordByDeviceInfo(map);
//       System.out.println(list.size());
//       
//       
//       List<TaskRecordVo> tlis= wxDeviceTaskJobDoMapper.selectTaskInfoByDeviceInf(map);
//       System.out.println(tlis.get(0).toString());
//       System.out.println(wxContactInfoDOMapper.selectTotalCount(79269L));
       
       //selectDeviceByUserId
//       List<WxInfoVo> list=deviceInfoDOMapper.selectDeviceByUserId(map);
//       System.out.println(list.size());
//       System.out.println(deviceInfoDOMapper.selectTotalCount(79269L));
//       List<PcTaskRecordVo> list= wxDeviceTaskJobDoMapper.selectPcTaskRecordInfoByUserIdInf(map);
       
//       sysolist(list);
       
//       System.err.println(profileStatementDoMapper.selectStamentFlag(79411L));
//       int[] intids=new int[]{79411,79412};
//       map.put("ids", intids);
//       List<ProfileStatementDo> list=profileStatementDoMapper.selectInfoListByDeviceId(map); 
//       System.out.println(list.size());
       
       
//       int size= wxDeviceTaskJobDoMapper.selectTotalSizeByStatus(map);
//       System.out.println(size);
       
       
        
//       List<ProfileStatementDo> contetntlist= profileStatementDoMapper.selectGroupByContent(map);
//       System.out.println(contetntlist.size());
//         String selectDeviceIdsStrByStatementIds(Map map)
       
//       System.out.println(profileStatementDoMapper.selectDeviceIdsStrByStatementIds(map));
         
         
         
//         String str="79411,79412,79411,79411,2,2,79411,2";
//         String[] arr=str.split(",");
//         String targetStr="";
//         List<String> list=new ArrayList<String>();
//         for(String s:arr){
//        	 if(!list.contains(s)){
//        		 list.add(s);
//        	 }
//         }
//         System.out.println(getIdStrListStr(list));
//         System.out.println(deviceInfoDOMapper.selectDeviceIdsByUserId(79269L));
         
//         List<String> li=new ArrayList<String>();
//         li.add("dsada");
//         li.add("xx");
//         String[] arr=li.toArray(new String[li.size()]); 
//         System.out.println(arr.length);
       
//         System.out.println(wxDeviceTaskJobDoMapper.setUtf8mb4Charat());
//           System.out.println(phoneBookListDoMapper.selectTotalCount(79270L));
//         Date date=new Date();
//         map.put("dateCreated", "2016-12-7 00:00:00");
//         System.out.println(wxPhoneResultDoMapper.getTodayCountByDeviceInfo(map));
//         
//         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//         Date now = new Date();
//         String hehe = dateFormat.format( now );   
//         System.out.println(hehe); 
//         
         
//         System.out.println(phoneBookListDoMapper.selectPhoneListDataByUserId(map).size());
         
//         map.put("status", -1);
//         map.put("limit", 30);
//         map.put("offset", 0);
//         map.put("allNum", 1);
//         map.put("userId", 79269L);
//         List<WxDeviceTaskJobDo> list=wxDeviceTaskJobDoMapper.selectPcFinalJobList(map);
//         for(WxDeviceTaskJobDo wx:list){
//        	 System.out.print(wx.getId() + " ");
//         }
//         Long[] intids=new Long[]{79696L,79695L,79694L,79693L,79692L,79691L,79690L,79689L,79688L,79687L,79682L};
//         map.put("ids", intids);
//       System.out.println(list.size());
//         List<PcTaskRecordVo> targetList=wxDeviceTaskJobDoMapper.selectPcFinalJobDetailList(map);
//         System.out.println(targetList.size()+" <------------");
       
       WxDeviceTaskJobDoExample example=new WxDeviceTaskJobDoExample();
       example.createCriteria().andStatusEqualTo((byte)1);
       List<WxDeviceTaskJobDo> list=wxDeviceTaskJobDoMapper.selectByExample(example);
       for(WxDeviceTaskJobDo wx:list){
    	   wxDeviceTaskJobDoMapper.updateJobNums(wx.getId());
       }
      
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
    
    public void lsysolist(List<PcTaskRecordVo> list){
    	System.out.println(list.size());
    	for(PcTaskRecordVo t:list){
    		System.out.println(t.toString());
    	}
    }
}

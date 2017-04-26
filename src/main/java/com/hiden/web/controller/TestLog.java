package com.hiden.web.controller;


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
import com.hiden.biz.service.UserService;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.ProfileWechatDOMapper;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileDOExample;
import com.hiden.tacker.EventTracker;
import com.hiden.utils.ParameterUtil;
import com.hiden.utils.PhenixUserHander;
import com.hiden.utils.TimeUtils;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.ResponseResult;
import com.hiden.web.model.SocketVo;
import com.hiden.web.model.UserAgent;
import com.hiden.web.socket.android.MessageCenter;
import com.hiden.web.socket.js.MyWebSocketHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.CharBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class TestLog {

	@Resource
	MyWebSocketHandler handler;
    private static org.slf4j.Logger log = LoggerFactory.getLogger(TestLog.class);
    @Resource
    private ProfileDOMapper profileDOMapper;
    @Resource
    private DeviceUserService deviceUserService;
    
    private MessageCenter messageCenter;
    
    //app进行注册使用
    @RequestMapping(value = {"/test/socket.json"}, produces = "application/json;charset=utf-8")
    public String socket(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="sessionId", required=false) String sessionId) throws Exception {
    	CharBuffer buffer = CharBuffer.wrap("Wilikeok");
    	SocketVo  socketVo=new SocketVo(null, null, sessionId,null);
    	messageCenter.getInstance().broadcast(buffer, socketVo);
    	return "ok";
    }
    
    
    
    
    //app进行注册使用
    @RequestMapping(value = {"/test/socket/deviceRegister.json"}, produces = "application/json;charset=utf-8")
    public String deviceRegister(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="password", required=false) String password,
    		@RequestParam(value="mobile", required=false) String mobile) throws Exception {
    	 HidenVO hidenVO = new HidenVO();
    	 hidenVO.setStatus(1);
         Map resultMap = new HashMap();
    	//进行加密
    	if (null!=password &&  null!=mobile) {
    		//不进行插入对应的重复的mobile的电话号码
    		if(!isHaveProfile(mobile)){
    			resultMap.put("flag", "1");
    			//进行添加加密操作的数据库信息
    			String toStorePassword = BCrypt.hashpw(password, BCrypt.gensalt());
    			ProfileDO pro=addOneProfile(toStorePassword,mobile);
    			resultMap.put("data", pro);
    			hidenVO.setMsg("成功提交信息内容");
    		}
    		else{
    			resultMap.put("flag", "2");
    			hidenVO.setMsg("此用户已经注册过了");	
    		}
    	}else{
    		resultMap.put("flag", "0");
			hidenVO.setMsg("提交信息内容不符合要求");
    	}
    	hidenVO.setResult(resultMap);
    	System.out.println(JSON.toJSONString(hidenVO));
    	return JSON.toJSONString(hidenVO);
    }


    //判断是否存在对应用户
    public boolean isHaveProfile(String mobile){
        return (deviceUserService.selectProfileBymobile(mobile).size()>0)?true:false;
    }
    
  //添加对应的用户的操作
    public ProfileDO addOneProfile(String password,String mobile){
    	ProfileDO profileDO=new ProfileDO();
    	profileDO.setMobile(mobile);
    	profileDO.setDateCreated(new Date());
    	profileDO.setLastUpdated(new Date());
    	profileDO.setStatus((byte)1);
    	profileDO.setMobile(mobile);
    	profileDO.setAutoReply((byte)1);
    	profileDO.setPassword(password);
    	profileDOMapper.insert(profileDO);
    	return profileDO;
    }
   

}

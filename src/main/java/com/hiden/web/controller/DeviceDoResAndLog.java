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
import com.hiden.web.model.UserAgent;
import com.hiden.web.utils.VerifyCodeUtils;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class DeviceDoResAndLog {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(DeviceDoResAndLog.class);
    @Resource
    private ProfileDOMapper profileDOMapper;
    @Resource
    private DeviceUserService deviceUserService;
    
    
    
    //app进行注册使用
    @RequestMapping(value = {"/api/m/1.0/deviceRegister.json", "/api/h/1.0/deviceRegister.json"}, produces = "application/json;charset=utf-8")
    public String deviceRegister(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="checkNO", required=false) String checkNO,
    		@RequestParam(value="password", required=false) String password,
    		@RequestParam(value="mobile", required=false) String mobile) throws Exception {
    	 org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
    	 Session session = currentUser.getSession();
    	 HidenVO hidenVO = new HidenVO();
    	 hidenVO.setStatus(1);
         Map resultMap = new HashMap();
        if(null!=checkNO){
        	String numNo=(String) session.getAttribute(VerifyCodeUtils.sendYzmSession);
        	if(null!=numNo && !checkNO.equals(numNo)){
        		resultMap.put("flag", "3");
    			resultMap.put("msg", "验证码不正确");
        	}
        }
        //进行对此用户进行判断是否有此权限
        Long profileId = (long) session.getAttribute("profileId");
        if (profileId < 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
            hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
            return JSON.toJSONString(hidenVO);
        }
        
        ProfileDO hidenpro=profileDOMapper.selectByPrimaryKey(profileId);
        //不存在并对应的权限不在
        if(null == hidenpro || ((byte)2!=hidenpro.getStatus())){
        	resultMap.put("flag", "-1");
			resultMap.put("msg", "此用户没有对应的权限去添加新用户");
			hidenVO.setMsg("此用户没有对应的权限去添加新用户");	
        }
        else{
        	//进行加密
        	if (null!=password &&  null!=mobile) {
        		//不进行插入对应的重复的mobile的电话号码
        		if(!isHaveProfile(mobile)){
        			resultMap.put("flag", "1");
        			resultMap.put("msg", "成功提交信息内容");
        			//进行添加加密操作的数据库信息
        			String toStorePassword = BCrypt.hashpw(password, BCrypt.gensalt());
        			ProfileDO pro=addOneProfile(toStorePassword,mobile);
        			resultMap.put("data", pro);
        			hidenVO.setMsg("成功提交信息内容");
        		}
        		else{
        			resultMap.put("flag", "2");
        			resultMap.put("msg", "此用户已经注册过了");
        			hidenVO.setMsg("此用户已经注册过了");	
        		}
        	}else{
        		resultMap.put("flag", "0");
        		resultMap.put("msg", "提交信息内容不符合要求");
    			hidenVO.setMsg("提交信息内容不符合要求");
        	}
        }
    	hidenVO.setResult(resultMap);
    	System.out.println(JSON.toJSONString(hidenVO));
    	return JSON.toJSONString(hidenVO);
    }
    
    
    
    //app进行登录
    @RequestMapping(value = {"/api/m/1.0/deviceLogin.json", "/api/h/1.0/deviceLogin.json"}, produces = "application/json;charset=utf-8")
    public String deviceLogin(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="password", required=false) String password,
    		@RequestParam(value="flag", required=false,defaultValue="1") int flag,
    		@RequestParam(value="mobile", required=false) String mobile) throws Exception {
    	 org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
    	 Session session = currentUser.getSession();
    	 HidenVO hidenVO = new HidenVO();
    	 hidenVO.setStatus(1);
         Map resultMap = new HashMap();
    	//进行加密
    	if (null!=password &&  null!=mobile) {
    		List<ProfileDO> list=deviceUserService.selectProfileBymobile(mobile);
    		if(list.size()>0){
    			//进行解密的明文
    			//当flag=1则为app服务的内容
    			String depswd = password;
    			if(flag==1){
    				depswd = PasswordParser.parserPlanPswd(password, null, false);
    			}
        		String tarpswd=list.get(0).getPassword();
        		//参数:明文   加密的密文【数据里面的】
        		if(BCrypt.checkpw(depswd, tarpswd)){
        			resultMap.put("flag", "1");
        			hidenVO.setMsg("登录成功...");
//        			 session.setTimeout(TimeUtils.TIME_1_MONTH_MILi);//millis
        			 session.setTimeout(24*60*60*1000);//millis
                     session.setAttribute("profileId", list.get(0).getId());
                     session.setAttribute("mobile", mobile);
                     Cookie cookieU = new Cookie(BizConstants.JSESSION_ID, session.getId().toString());
                     cookieU.setMaxAge(TimeUtils.TIME_1_MONTH_SEC);
                     cookieU.setPath("/");
                     response.addCookie(cookieU);
        		}else{
        			resultMap.put("flag", "2");
        			hidenVO.setMsg("校验不对");
        		}
    		}else{
    			resultMap.put("flag", "3");
    			hidenVO.setMsg("无此用户");
    		}
    	}
    	else{
    		resultMap.put("flag", "0");
			hidenVO.setMsg("提交信息内容不符合要求");
    	}
    	hidenVO.setResult(resultMap);
    	System.out.println(JSON.toJSONString(hidenVO));
    	return JSON.toJSONString(hidenVO);
    }
    
    
    
    //app进行登出
    @RequestMapping(value = {"/api/m/1.0/devicelogOut.json", "/api/h/1.0/devicelogOut.json"}, produces = "application/json;charset=utf-8")
    public String devicelogOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        try {
            logOut(currentUser, session, response);
        } catch (Exception e) {
            log.error("logout failed . exp:" + e.getMessage());
        }
        hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        return JSON.toJSONString(hidenVO);
    }

    
    
    private void logOut(Subject currentUser, Session session, HttpServletResponse response) {
        if (null != session) {
            //in case
            session.setAttribute("profileId", -1l);
            //执行登出
            currentUser.logout();
            session.setAttribute("mobile", "");
            Cookie cookieU = new Cookie(BizConstants.JSESSION_ID, "-1");
            cookieU.setMaxAge(60 * 60 * 24 * 1);
            cookieU.setPath("/");
            response.addCookie(cookieU);
        }
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

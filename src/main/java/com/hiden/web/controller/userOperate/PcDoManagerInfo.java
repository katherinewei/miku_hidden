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
import com.hiden.biz.service.UserService;
import com.hiden.persistence.DeviceInfoDOMapper;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.ProfileStatementDoMapper;
import com.hiden.persistence.ProfileWechatDOMapper;
import com.hiden.persistence.TaskAndDeviceDoMapper;
import com.hiden.persistence.WxContactInfoDOMapper;
import com.hiden.persistence.WxDeviceTaskJobDoMapper;
import com.hiden.persistence.WxFriendListMapper;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileDOExample;
import com.hiden.persistence.domain.ProfileStatementDo;
import com.hiden.persistence.domain.ProfileStatementDoExample;
import com.hiden.persistence.domain.TaskAndDeviceDo;
import com.hiden.persistence.domain.WxContactInfoDO;
import com.hiden.persistence.domain.WxContactInfoDOExample;
import com.hiden.persistence.domain.WxDeviceTaskJobDo;
import com.hiden.persistence.domain.WxFriendList;
import com.hiden.persistence.domain.WxFriendListExample;
import com.hiden.tacker.EventTracker;
import com.hiden.utils.ParameterUtil;
import com.hiden.utils.PhenixUserHander;
import com.hiden.utils.TimeUtils;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.PcTaskRecordVo;
import com.hiden.web.model.ResponseResult;
import com.hiden.web.model.TaskRecordVo;
import com.hiden.web.model.UserAgent;
import com.hiden.web.model.WxInfoVo;
import com.sun.tools.internal.ws.processor.model.ModelVisitor;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
@Controller
public class PcDoManagerInfo {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(PcDoManagerInfo.class);
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
    
    
   //pc登录界面
    @RequestMapping(value = {"/pc/h/1.0/login.htm"}, produces = "text/html;charset=utf-8")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response, ModelMap model,
    		@RequestParam(value="password", required=false) String password,
    		@RequestParam(value="mobile", required=false) String mobile) throws Exception {
    	ModelAndView mav = new ModelAndView();
    	org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
    	Session session = currentUser.getSession();
    	ProfileDO p=new ProfileDO();
    	if (null!=password &&  null!=mobile) {
    		List<ProfileDO> list=deviceUserService.selectProfileBymobile(mobile);
    		if(list.size()>0){
    			p=list.get(0);
    			String tarpswd=p.getPassword();
        		//参数:明文   加密的密文【数据里面的】
        		if(BCrypt.checkpw(password, tarpswd)){
        			session.setTimeout(3*60*60*1000);//millis
                    session.setAttribute("profileId", list.get(0).getId());
                    session.setAttribute("mobile", mobile);
                    Cookie cookieU = new Cookie(BizConstants.JSESSION_ID, session.getId().toString());
                    cookieU.setMaxAge(TimeUtils.TIME_1_MONTH_SEC);
                    cookieU.setPath("/");
                    response.addCookie(cookieU);
//                    model.addAttribute("userName",p.getNickname());
//                    model.addAttribute("pic",p.getProfilePic());
//                    model.addAttribute("id",p.getId());
//                    mav.setViewName("index");
                    mav.setViewName("redirect:index.htm");
            	    return mav;
        		}else{
        			 model.addAttribute("errorMsg","用户名或密码错误");
        			 model.addAttribute("mobile",mobile);
        			 mav.setViewName("login");
    		   	     return mav;
        		}
    		}else{
    			 model.addAttribute("mobile",mobile);
    			 model.addAttribute("errorMsg","用户名或密码错误");
    			 mav.setViewName("login");
		   	     return mav;
    		}
    	}
    	model.addAttribute("name",p);
    	mav.setViewName("login");
	    return mav;
    }
    
    
    
    //pc登录界面
    @RequestMapping(value = {"/pc/h/1.0/index.htm"}, produces = "text/html;charset=utf-8")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
    	ModelAndView mav = new ModelAndView();
    	Session session = currentUser.getSession();
    	Object obj = session.getAttribute("profileId");
    	if(null!=obj){
    		Long profileId = Long.parseLong(obj.toString());
    		ProfileDO p=profileDOMapper.selectByPrimaryKey(profileId);
    		if(null!=p){
    			model.addAttribute("userName",p.getNickname());
    			//0是无效  1是有效  2有效加可以注册用户
    			model.addAttribute("status",p.getStatus());
    			model.addAttribute("defaultName",(p.getRealName()==null)?"":p.getRealName());
    	        model.addAttribute("pic",p.getProfilePic());
    	        model.addAttribute("id",p.getId());
    	        mav.setViewName("index");
    		}
    	}else{
    		mav.setViewName("login");
    	}
    	return mav;
    }
    
    
    //pc进行登录界面
    @RequestMapping(value = {"/pc/h/1.0/register.htm"}, produces = "text/html;charset=utf-8")
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response, ModelMap model,
    		@RequestParam(value="password", required=false) String password,
    		@RequestParam(value="mobile", required=false) String mobile) throws Exception {
    		ModelAndView mav = new ModelAndView();
		   	//进行加密
		   	if (null!=password &&  null!=mobile) {
		   		//不进行插入对应的重复的mobile的电话号码
		   		if(!isHaveProfile(mobile)){
		   			//进行添加加密操作的数据库信息
		   			String toStorePassword = BCrypt.hashpw(password, BCrypt.gensalt());
		   			ProfileDO pro=addOneProfile(toStorePassword,mobile);
		   			model.addAttribute("successMsg","成功注册对应的账号");
		   			mav.setViewName("login");
		   	        return mav;
		   		}
		   		else{
		   		    model.addAttribute("errorMsg","此用户已经注册过了");
		   			mav.setViewName("register");
		   	        return mav;
		   		}
		   	}else{
					model.addAttribute("提交信息内容不符合要求");
					mav.setViewName("register");
		   	        return mav;
		   	}
    }
    
    
    
    //pc进行登出
    @RequestMapping(value = {"/pc/m/1.0/logOut.htm"}, produces = "text/html;charset=utf-8")
    public ModelAndView logOut(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        try {
            logOut(currentUser, session, response);
        } catch (Exception e) {
            log.error("logout failed . exp:" + e.getMessage());
        }
        ModelAndView mav = new ModelAndView();
    	mav.setViewName("login");
        return mav;
    }
    
    
    
    
    @RequestMapping(value = {"/pc/h/1.0/login2.htm"})
    public ModelAndView login2(HttpServletRequest request, HttpServletResponse response, ModelMap model,
    		@RequestParam(value="flag", required=false,defaultValue="1xxxx") String flag) throws Exception {
    	List<ProfileDO> list=deviceUserService.selectProfileBymobile("13189071314");
    	ProfileDO p=list.get(0);
    	String op = ParameterUtil.getParameter(request, "op");
    	model.addAttribute("name","LaiHaoDa");
    	model.addAttribute("flag",flag);
    	model.addAttribute("p",p);
    	ModelAndView mav = new ModelAndView();
    	mav.setViewName("login");
        return mav;
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

}

package com.hiden.web.controller;

import com.alibaba.fastjson.JSON;
import com.hiden.Env;
import com.hiden.biz.cache.CheckNOGenerator;
import com.hiden.biz.common.BizConstants;
import com.hiden.biz.common.BizErrorEnum;
import com.hiden.biz.common.TimeConstants;
import com.hiden.biz.model.SmsResponseTpl;
import com.hiden.biz.security.NeedProfile;
import com.hiden.biz.sms.SMSUtils;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileDOExample;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.ResponseResult;
import com.hiden.web.utils.VerifyCodeUtil;
import com.hiden.web.utils.VerifyCodeUtils;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by myron on 16-9-10.
 * 验证手机是否本人
 * 这里只是发送短信验证码，短信验证码的验证在注册逻辑中统一校验
 */
@RestController
public class CheckMobile {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(CheckMobile.class);
    
    public static final String YZM_NO_PREFIX = "yzmNO_";

    @Resource
    private ProfileDOMapper profileDOMapper;

    @Resource
    private MemcachedClient memcachedClient;

    @Resource
    private SMSUtils smsUtils;

    @Resource
    private Env env;

    @RequestMapping(value = {"/api/m/1.0/checkMobile.json", "/api/h/1.0/checkMobile.json"}, produces = "application/json;charset=utf-8")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String mobile = request.getParameter("mobile");
        String act = request.getParameter("act");
        String reg = request.getParameter("reg");
        ResponseResult result = new ResponseResult();
        String checkNO = request.getParameter("checkNO");
        String yzmNO = request.getParameter("yzmNO");	//验证码
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        if (org.apache.commons.lang.StringUtils.equals(reg, "reg")) {
            //判断是否已经注册过
            //根据电话查出profile信息
            ProfileDOExample profileDOExample = new ProfileDOExample();
            profileDOExample.createCriteria().andMobileEqualTo(mobile).andStatusEqualTo((byte) 1);
            List<ProfileDO> thisNOprofiles = profileDOMapper.selectByExample(profileDOExample);
            if (null != thisNOprofiles && thisNOprofiles.size() > 0) {
                hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
                hidenVO.setCode(BizErrorEnum.REGISTED_YET.getCode());
                hidenVO.setMsg(BizErrorEnum.REGISTED_YET.getMsg());
                return JSON.toJSONString(hidenVO);
            }
        }

        if (StringUtils.equals("send", act)) {
        	if(StringUtils.isBlank(yzmNO)){
        		hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
                //hidenVO.setCode(BizErrorEnum.REGISTED_YET.getCode());
                hidenVO.setMsg("请输入验证码~");
                return JSON.toJSONString(hidenVO);
        	}
        	String yzmCache = null;
        	if(null != session.getAttribute(VerifyCodeUtil.sendYzmSession)){
        		yzmCache = (String)session.getAttribute(VerifyCodeUtil.sendYzmSession);
        	}
        	Long sendYzmTime = null;
        	Object sendYzmTimeObject = session.getAttribute(VerifyCodeUtil.sendYzmTimeSession);
        	if(null != sendYzmTimeObject){
        		sendYzmTime = Long.valueOf(String.valueOf(sendYzmTimeObject));
        	}
        	boolean yzmIsTimeOut = false;
        	if(null != sendYzmTime && sendYzmTime > 0 
        			&& (System.currentTimeMillis() - sendYzmTime) < 90 * 1000l){
        		yzmIsTimeOut = true;
        	}
        	if(yzmIsTimeOut && null != yzmCache && yzmNO.toUpperCase().equals(yzmCache.toUpperCase())){
        		//send check number and put it into cache
        		sendCheckNO(mobile, hidenVO, result);
        	}else{
        		if(!yzmIsTimeOut){
        			session.setAttribute(VerifyCodeUtil.sendYzmSession,null);  
        			session.setAttribute(VerifyCodeUtil.sendYzmTimeSession, null);
        		}
        		hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
                hidenVO.setMsg("验证码不正确或已过期~");
                return JSON.toJSONString(hidenVO);
        	}
        } else if (StringUtils.equals("check", act)) {
            //check the check number is valid or not
            checkCheckNO(mobile, checkNO, hidenVO);
        } else {
            result.setStatus(ResponseStatusEnum.FAILED.getCode());
            result.setErrorCode(BizErrorEnum.PARAMS_ERROR.getCode());
            result.setMsg(BizErrorEnum.PARAMS_ERROR.getMsg());
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.PARAMS_ERROR.getCode());
            hidenVO.setMsg(BizErrorEnum.PARAMS_ERROR.getMsg());
        }
        return JSON.toJSONString(hidenVO);
    }

    /**
     * check the check number is invalid or not
     *
     * @param mobile
     * @param checkNO
     * @param hidenVO
     */
    private void checkCheckNO(String mobile, String checkNO, HidenVO hidenVO) {
        //check code 验证码
        String codeo = (String) memcachedClient.get(BizConstants.CHECK_NO_PREFIX + mobile);//jedis.get(BizConstants.CHECK_NO_PREFIX + mobile);
        String tailMobile = org.apache.commons.lang.StringUtils.substring(mobile, 7, 11);
        if (env.isDev() || env.isTest()) {
            /*if (StringUtils.equals(checkNO, tailMobile)) {
                hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
                return;
            }*/
        	hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
            return;
        }
        if (StringUtils.equals(checkNO, codeo)) {
            hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        } else {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.CHECK_NO_ERROR.getCode());
            hidenVO.setMsg(BizErrorEnum.CHECK_NO_ERROR.getMsg());
            log.warn("验证码校验失败. mobile " + mobile);
        }
    }

    /**
     * send check number via sms then put it into cache
     *
     * @param mobile
     * @param hidenVO
     */
    private void sendCheckNO(String mobile, HidenVO hidenVO, ResponseResult result) {
        //boolean sendCheckNO = smsUtils.sendCheckCodeAndCacheCode(mobile);
    	SmsResponseTpl responseTpl = smsUtils.sendCheckCodeAndCacheCodeSmsResponseTpl(mobile);
    	if(null == responseTpl){
    		result.setStatus(BizErrorEnum.SYSTEM_BUSY.getCode());
            result.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
            result.setErrorCode(BizErrorEnum.SYSTEM_BUSY.getCode());
            hidenVO.setStatus(BizErrorEnum.SYSTEM_BUSY.getCode());
            hidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
            hidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
    	}
    	if (responseTpl.getCode() != 0) {//失败
    		result.setStatus(ResponseStatusEnum.FAILED.getCode());
            result.setErrorCode(responseTpl.getCode());
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(responseTpl.getCode());
            if(responseTpl.getCode() == 22){
            	hidenVO.setMsg("1小时内同一手机号发送次数不能超过3次");
            	result.setMsg("1小时内同一手机号发送次数不能超过3次");
            }else{
            	hidenVO.setMsg(responseTpl.getMsg());
            	result.setMsg(responseTpl.getMsg());
            }
        } else {
        	hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        }
        /*if (sendCheckNO) {
            hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        } else {
            result.setStatus(ResponseStatusEnum.FAILED.getCode());
            result.setMsg(BizErrorEnum.SEND_CHECKNO_FAILED.getMsg());
            result.setErrorCode(BizErrorEnum.SEND_CHECKNO_FAILED.getCode());
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SEND_CHECKNO_FAILED.getCode());
            hidenVO.setMsg(BizErrorEnum.SEND_CHECKNO_FAILED.getMsg());
        }*/
    }
    
    @NeedProfile
    @RequestMapping(value = {"/api/m/1.0/sendTestNO.json", "/api/h/1.0/sendTestNO.json"}, produces = "application/json;charset=utf-8")
    public String sendTestNO(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String mobile = request.getParameter("mobile");
		HidenVO hidenVO = new HidenVO();
		hidenVO.setStatus(1);
        Map result = new HashMap();
        //ResponseResult result = new ResponseResult();
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        long profileId = -1;
        profileId = (long) session.getAttribute("profileId");
        if (profileId < 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
            hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
            return JSON.toJSONString(hidenVO);
        }
        
        ProfileDO profileDO = profileDOMapper.selectByPrimaryKey(profileId); 
        if(null != profileDO && null != profileDO.getMobile() && "15622395287".equals(profileDO.getMobile().trim())){
        	String checkCode = CheckNOGenerator.getFixLenthString(BizConstants.CHECK_NO_LEN);
        	result.put("checkCode", checkCode);
        	memcachedClient.set(BizConstants.CHECK_NO_PREFIX + mobile, TimeConstants.REDIS_EXPIRE_SECONDS_10, checkCode);
        	/*SmsResponseTpl responseTpl = smsUtils.sendCheckCodeAndCacheCodeSmsResponseTplTest(mobile, checkCode);
        	if (null != responseTpl && responseTpl.getCode() != 0) {//失败
        		result.put("status", ResponseStatusEnum.FAILED.getCode());
        		result.put("msg", responseTpl.getMsg());
        		result.put("errorCode", responseTpl.getCode());
        		hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
        		hidenVO.setCode(responseTpl.getCode());
        		hidenVO.setMsg(responseTpl.getMsg());
        	} else {
        		hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        	}*/
        	hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        }else{
        	hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
        	result.put("msg", "错误");
        }
        
    	hidenVO.setResult(result);
        return JSON.toJSONString(hidenVO);
    }
    
    /**
     * 获取图片验证码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/api/m/1.0/getMobileVerificationCode2.json", "/api/h/1.0/getMobileVerificationCode2.json"}, produces = "application/json;charset=utf-8")
    public String getMobileVerificationCode2(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String mobile = request.getParameter("mobile");
		HidenVO hidenVO = new HidenVO();
		hidenVO.setStatus(1);
        Map result = new HashMap();
        //ResponseResult result = new ResponseResult();
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        /*long profileId = -1;
        profileId = (long) session.getAttribute("profileId");
        if (profileId < 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
            hidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
            return JSON.toJSONString(hidenVO);
        }*/
        /*if(StringUtils.isBlank(mobile)){
        	hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
        	hidenVO.setCode(BizErrorEnum.PARAMS_ERROR.getCode());
            hidenVO.setMsg(BizErrorEnum.PARAMS_ERROR.getMsg());
            return JSON.toJSONString(hidenVO);
        }*/
        //String checkCode = CheckNOGenerator.getFixLenthString(BizConstants.CHECK_NO_LEN);
        //String sessionId = String.valueOf(session.getId());
        String verifyCode = VerifyCodeUtil.create(80, 20, 1, request, response);
        /*if(StringUtils.isNotBlank(verifyCode)){
        	//memcachedClient.set(YZM_NO_PREFIX + mobile, 60, verifyCode.toUpperCase());
        	System.out.println("++++++++++++++++++++++++=");
        	System.out.println("--------------------------"+sessionId);
        	memcachedClient.set("111111111", 5, verifyCode.toUpperCase());
        }*/
        return null;
    }
    
    /**
     * 获取图片验证码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/api/m/1.0/getMobileVerificationCode.json", "/api/h/1.0/getMobileVerificationCode.json"}, produces = "application/json;charset=utf-8")
    public String getMobileVerificationCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0);  
        response.setContentType("image/jpeg");
    	
    	String mobile = request.getParameter("mobile");
		HidenVO hidenVO = new HidenVO();
		hidenVO.setStatus(1);
        Map result = new HashMap();
        //ResponseResult result = new ResponseResult();
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        //生成随机字串  
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4, 1);
        //生成图片  
        int w = 200, h = 75;  
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        //存入会话session  
        //HttpSession session = request.getSession(true);  
        session.setAttribute(VerifyCodeUtils.sendYzmSession,verifyCode);  
        session.setAttribute(VerifyCodeUtils.sendYzmTimeSession, System.currentTimeMillis());
        return null;
    }
    
}

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
import com.hiden.biz.service.UserService;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.ProfileWechatDOMapper;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileDOExample;
import com.hiden.tacker.EventTracker;
import com.hiden.utils.EmojiFilter;
import com.hiden.utils.PhenixUserHander;
import com.hiden.utils.TimeUtils;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.ResponseResult;
import com.hiden.web.model.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
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
 * Created by myron on 16-9-29.
 */
@RestController
public class Register {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(Register.class);

    @Resource
    private ProfileDOMapper profileDOMapper;

    @Resource
    private UserService userService;

    @Resource
    private CheckNOValidator checkNOValidator;

    @Resource
    private ProfileWechatDOMapper profileWeChatDOMapper;

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/api/m/1.0/register2.json", "/api/h/1.0/register2.json"}, produces = "application/json;charset=utf-8")
    public String register2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //get params
        String mobileNum = request.getParameter("mobile");
        String pswd = request.getParameter("pswd");
        String hpswd = request.getParameter("hp");
        String checkNO = request.getParameter("checkNum");
        String ip = request.getParameter("ip");
        String deviceId = request.getParameter("deviceId");
        
        UserAgent ua = new UserAgent();
        ResponseResult result = new ResponseResult();
        HidenVO hidenVO = new HidenVO();
        Map resultMap = new HashMap();
        //获取用户设备信息
        String userAgent = request.getHeader(BizConstants.USER_AGENT);
        try {
            EventTracker.track(mobileNum, "register", "register-action", "regiser-pre", 1L);
        } catch (Exception e) {
            log.error("register mobile error mobile:" + mobileNum);
        }
        //根据电话查出profile信息
        ProfileDOExample profileDOExample = new ProfileDOExample();
        profileDOExample.createCriteria().andMobileEqualTo(mobileNum).andStatusEqualTo((byte) 1);
        List<ProfileDO> thisNOprofiles = profileDOMapper.selectByExample(profileDOExample);
        if (null != thisNOprofiles && thisNOprofiles.size() > 0) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.REGISTED_YET.getCode());
            hidenVO.setMsg(BizErrorEnum.REGISTED_YET.getMsg());
            return JSON.toJSONString(hidenVO);
        }
        String tailMobile = org.apache.commons.lang.StringUtils.substring(mobileNum, 7, 11);
        //if (checkNOValidator.checkNOisValid(checkNO, mobileNum)||StringUtils.equals(checkNO,tailMobile)) {//注册特殊验证码
        if (null != checkNO && checkNOValidator.checkNOisValid(checkNO, mobileNum)) {//注册特殊验证码
            //checkNOOK = true;
        } else {
            //checkNOOK = false;
            log.error("注册放开验证码 验证码校验失败... mobile:" + mobileNum + ",checkNUM:" + checkNO);
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.CHECK_NO_ERROR.getCode());
            hidenVO.setMsg(BizErrorEnum.CHECK_NO_ERROR.getMsg());
            log.warn("用户注册过程 验证码校验失败. mobile " + mobileNum);
            return JSON.toJSONString(hidenVO);
        }
        //密码操作 -- h5
        boolean isH5 = false;
        if (StringUtils.isBlank(pswd)) {
            if (StringUtils.isNotBlank(hpswd)) {
                pswd = hpswd;
                byte[] pswdArray = RSAEncrypt.hexStringToBytes(pswd);
                pswd = new String(pswdArray);
                isH5 = true;
            } else {
                hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
                hidenVO.setCode(BizErrorEnum.PARAMS_ERROR.getCode());
                hidenVO.setMsg(BizErrorEnum.PARAMS_ERROR.getMsg());
                log.error("password param is blank...  mobile:" + mobileNum);
                return JSON.toJSONString(hidenVO);
            }
        }
        String dePswd = PasswordParser.parserPlanPswd(pswd, null, isH5);
        String toStorePassword = BCrypt.hashpw(dePswd, BCrypt.gensalt());

        com.hiden.web.filter.Profiler.enter("register action");
        ProfileDO profileDO = null;
        byte deplom = 0;
        try {
            if (org.apache.commons.lang.StringUtils.isNotBlank(userAgent)) {
                ua = JSON.parseObject(userAgent, UserAgent.class);
            }
            if (null != ua) {
                if (StringUtils.equals(BizConstants.PRE_CLIENT_FLAG, ua.getAppbundle())) {
                    deplom = 1;
                }
            }
        } catch (Exception e) {
            log.error("parse welinkAgent failed. mobile:" + mobileNum);
        }

        //role
        EventTracker.track(profileDO.getMobile(), "register", "register-action", "register-success", 1L);
        result.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        resultMap.put("profile_pic", profileDO.getProfilePic());
        resultMap.put("lemon_name", profileDO.getNickname());
        resultMap.put("uid", PhenixUserHander.encodeUserId(profileDO.getId()));
        resultMap.put("pid", PhenixUserHander.encodeUserId(profileDO.getId()));
        hidenVO.setResult(resultMap);
        return JSON.toJSONString(hidenVO);
    }

    /**
     * 执行登录
     *
     * @param mobileNum
     * @param toStorePassword
     * @param profileId
     */
    private void doLogin(String mobileNum, String toStorePassword, long profileId, HttpServletResponse response) {
        //注册成功后做登陆 --- 以mobile为name登陆
        UsernamePasswordToken token = new UsernamePasswordToken(mobileNum, toStorePassword);
        token.setRememberMe(true);
        //2. 获取当前Subject：
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();//boolean create
        session.setTimeout(TimeUtils.TIME_1_MONTH_MILi);//millis
        session.setAttribute("profileId", profileId);
        session.setAttribute("mobile", mobileNum);
        Cookie cookieU = new Cookie("JSESSIONID", session.getId().toString());
        cookieU.setMaxAge(60 * 60 * 24 * 15);
        cookieU.setPath("/");
        response.addCookie(cookieU);
    }

    /**
     * insert profile
     *
     * @param mobileNum
     * @param storedPassword
     */
    private ProfileDO addProfile(long buildingId, String mobileNum, String storedPassword, byte deplom) {
        ProfileDO profileDO = new ProfileDO();
        profileDO.setDateCreated(new Date());
        profileDO.setLastUpdated(new Date());
        profileDO.setMobile(mobileNum);
        profileDO.setStatus(ProfileStatusEnum.valid.getCode());
        profileDO.setInstalledApp((byte) 1);
        profileDO.setPassword(storedPassword);
        profileDO.setDiploma(deplom);
        String checkCode = CheckNOGenerator.getFixLenthString(BizConstants.CHECK_NO_LEN);
        if (null != mobileNum && mobileNum.length() > 4) {
            profileDO.setNickname(mobileNum.substring(mobileNum.length() - 4, mobileNum.length()) + checkCode);
        } else {
            profileDO.setNickname(CheckNOGenerator.getFixLenthString(6));
        }
        int updateprofile = profileDOMapper.insertSelective(profileDO);
        if (updateprofile <= 0) {
            log.error("注册过程更改数据失败. update profile failed. mobile:" + mobileNum);
            com.hiden.web.filter.Profiler.release();
            return null;
        }
        com.hiden.web.filter.Profiler.release();
        return profileDO;
    }

    /**
     * check is register
     * @param request
     * @param response
     * @param mobile
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/api/m/1.0/checkIsRegister.json", "/api/h/1.0/checkIsRegister.json"}, produces = "application/json;charset=utf-8")
    public String checkIsRegister(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam String mobile) throws Exception {
        HidenVO welinkVO = new HidenVO();
        Map resultMap = new HashMap();
        //根据电话查出profile信息
        ProfileDOExample profileDOExample = new ProfileDOExample();
        profileDOExample.createCriteria().andMobileEqualTo(mobile).andStatusEqualTo((byte) 1);
        List<ProfileDO> thisNOprofiles = profileDOMapper.selectByExample(profileDOExample);
        if (null != thisNOprofiles && thisNOprofiles.size() > 0) {
            ProfileDO profileDO = thisNOprofiles.get(0);
            if (null == profileDO.getPassword() || "".equals(profileDO.getPassword().trim())) {
                resultMap.put("isRegister", 2);        //已注册未设密码
            } else {
                resultMap.put("isRegister", 1);        //已注册
            }
        } else {
            resultMap.put("isRegister", 0);        //未注册
        	/*ProfileTempDOExample profileTempDOExample = new ProfileTempDOExample();
        	profileTempDOExample.createCriteria().andMobileEqualTo(mobile);
        	List<ProfileTempDO> profileTempDOList = profileTempDOMapper.selectByExample(profileTempDOExample);
        	if(!profileTempDOList.isEmpty()){
        		resultMap.put("isRegister", 4);		//未注册,已有temp记录
        	}else{
        		resultMap.put("isRegister", 3);		//未注册
        	}*/
        }
        welinkVO.setStatus(1);
        welinkVO.setResult(resultMap);
        return JSON.toJSONString(welinkVO);
    }

    public void setProfileDOMapper(ProfileDOMapper profileDOMapper) {
        this.profileDOMapper = profileDOMapper;
    }

    public static void main(String[] args) {
        Long ss = 12L;
        System.out.println(ss.toString());
    }

}

package com.hiden.web.controller;

import com.alibaba.fastjson.JSON;
import com.hiden.biz.common.BizConstants;
import com.hiden.biz.common.BizErrorEnum;
import com.hiden.biz.security.PasswordParser;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileDOExample;
import com.hiden.tacker.EventTracker;
import com.hiden.utils.PhenixUserHander;
import com.hiden.utils.TimeUtils;
import com.hiden.utils.UserUtils;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.filter.Profiler;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.UserAgent;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by myron on 16-9-28.
 */
@RestController
public class Login {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(Login.class);

    @Resource
    private ProfileDOMapper profileDOMapper;

    @Resource
    private UserUtils userUtils;

    @RequestMapping(value = {"/api/m/1.0/login.json", "/api/h/1.0/login.json"}, produces = "application/json;charset=utf-8")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //fetch parameters
        String mobile = request.getParameter("m");
        String pswd = request.getParameter("p");
        String hpswd = request.getParameter("hp");
        String ip = request.getParameter("ip");
        String deviceId = request.getParameter("deviceId");
        UserAgent ua = new UserAgent();
        HidenVO hidenVO = new HidenVO();
        Map resultMap = new HashMap();
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();

        //check has profile
        if (StringUtils.isBlank(mobile)) {
            log.error("登录失败 手机号码为空  Login failed. mobile:" + mobile + ",pswd:" + pswd + ",sessionId:" + session.getId().toString());
            EventTracker.track(BizConstants.LOGIN, "login", "check-mobile", "failure", 1L);
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.MOBILE_ERROR.getCode());
            hidenVO.setMsg(BizErrorEnum.MOBILE_ERROR.getMsg());
            return JSON.toJSONString(hidenVO);
        }
        Profiler.enter("user login, fetch mobile " + mobile + " profile");
        ProfileDO hPDO = null;
        ProfileDOExample hpExample = new ProfileDOExample();
        hpExample.createCriteria().andMobileEqualTo(mobile);
        List<ProfileDO> hpDOs = profileDOMapper.selectByExample(hpExample);
        Profiler.release();

        EventTracker.track(BizConstants.LOGIN, "login", "login", "pre", 1L);
        if (null == hpDOs) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.NO_SUCH_MEMBER.getCode());
            hidenVO.setMsg(BizErrorEnum.NO_SUCH_MEMBER.getMsg());
            return JSON.toJSONString(hidenVO);
        }
        if (null != hpDOs && hpDOs.size() < 1) {
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.NO_SUCH_MEMBER.getCode());
            hidenVO.setMsg(BizErrorEnum.NO_SUCH_MEMBER.getMsg());
            return JSON.toJSONString(hidenVO);
        }

        hPDO = hpDOs.get(0);

        //1. check password
        boolean isH5 = false;
        Profiler.enter("password check");
        if (StringUtils.isBlank(pswd)) {
            if (StringUtils.isNotBlank(hpswd)) {
                pswd = hpswd;
                /*byte[] pswdArray = RSAEncrypt.hexStringToBytes(pswd);
                pswd = new String(pswdArray);*/
                isH5 = true;
            }
        }
        if (!isH5) {
            pswd = PasswordParser.parserPlanPswd(pswd, null, isH5);
        }
        if (StringUtils.isBlank(pswd)) {
            log.error("登录失败 密码错误  Login failed. mobile:" + mobile + ",pswd:" + pswd + ",sessionId:" + session.getId().toString());
            EventTracker.track(hPDO.getMobile(), "login", "check-password", "failure", 1L);
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.PASSWORD_ERROR.getCode());
            hidenVO.setMsg(BizErrorEnum.PASSWORD_ERROR.getMsg());
            return JSON.toJSONString(hidenVO);
        }
        Profiler.release();

        EventTracker.track(BizConstants.LOGIN, "login", "password", "success", 1L);

        boolean pass = userUtils.checkPswdByMobile(hPDO, pswd);
        //if password valid
        if (pass) {
            //获取用户设备信息
            String userAgent = request.getHeader(BizConstants.USER_AGENT);
            resultMap.put("uid", PhenixUserHander.encodeUserId(hPDO.getId()));
            //recode ext info
            try {
                if (StringUtils.isNotBlank(userAgent)) {
                    ua = JSON.parseObject(userAgent, UserAgent.class);
                }
                if (null != ua) {
                    if (StringUtils.equals(BizConstants.PRE_CLIENT_FLAG, ua.getAppbundle())) {
                        ProfileDO hpDO1 = new ProfileDO();
                        hpDO1.setDiploma((byte) 1);
                        ProfileDOExample hpDOExample = new ProfileDOExample();
                        hpDOExample.createCriteria().andIdEqualTo(hpDOs.get(0).getId());
                        if (profileDOMapper.updateByExampleSelective(hpDO1, hpDOExample) < 1) {
                            log.error("record client diploma failed. profileId:" + hpDOs.get(0).getId() + ",sessionId:" + session.getId().toString());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("record client diploma failed. profileId:" + hpDOs.get(0).getId() + ",sessionId:" + session.getId().toString());
            }

            //执行登录
            UsernamePasswordToken token = new UsernamePasswordToken(mobile, hPDO.getPassword());
            token.setRememberMe(true);
            currentUser.login(token);
            if (currentUser.isAuthenticated()) {
                session.setTimeout(TimeUtils.TIME_1_MONTH_MILi);//millis
                session.setAttribute("profileId", hPDO.getId());
                session.setAttribute("mobile", mobile);
                Cookie cookieU = new Cookie(BizConstants.JSESSION_ID, session.getId().toString());
                cookieU.setMaxAge(TimeUtils.TIME_1_MONTH_SEC);
                cookieU.setPath("/");
                response.addCookie(cookieU);
            }
        } else {
            //password invalid
            EventTracker.track(mobile, "login", "login-action", "login-pswd-error", 1L);
            log.error("-登录失败 密码错误  Login failed. mobile:" + mobile + ",pswd:" + pswd + ",sessionId:" + session.getId().toString());
            hidenVO.setStatus(ResponseStatusEnum.FAILED.getCode());
            hidenVO.setCode(BizErrorEnum.PASSWORD_ERROR.getCode());
            hidenVO.setMsg(BizErrorEnum.PASSWORD_ERROR.getMsg());
            return JSON.toJSONString(hidenVO);
        }
        //返回个人信息
        hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        resultMap.put("profilePic", hPDO.getProfilePic());
        resultMap.put("lemonName", hPDO.getNickname());
        //resultMap.put("uid", PhenixUserHander.encodeUserId(hPDO.getId()));
        resultMap.put("sex", hPDO.getSex());
        EventTracker.track(BizConstants.LOGIN, "login", "login", "success", 1L);
        hidenVO.setResult(resultMap);
        return JSON.toJSONString(hidenVO);
    }

    public void setUserUtils(UserUtils userUtils) {
        this.userUtils = userUtils;
    }

    public void setHidenProfileDOMapper(ProfileDOMapper profileDOMapper) {
        this.profileDOMapper = profileDOMapper;
    }
}

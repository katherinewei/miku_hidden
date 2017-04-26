package com.hiden.utils;

import com.alibaba.fastjson.JSON;
import com.hiden.biz.common.BizConstants;
import com.hiden.biz.security.BCrypt;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.persistence.domain.ProfileDO;
import com.hiden.persistence.domain.ProfileDOExample;
import com.hiden.web.model.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by myron on 16-9-30.
 */
@Service
public class UserUtils {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(UserUtils.class);

    @Resource
    private ProfileDOMapper profileDOMapper;

    /**
     * 是否需要根据用户id跳转判断
     *
     * @param session
     * @return
     */
    public static boolean redirect(Session session) {
        if (null == session) {
            return true;
        }
        if (null == session.getAttribute(BizConstants.PROFILE_ID)) {
            return true;
        }
        long profileId = (long) session.getAttribute(BizConstants.PROFILE_ID);
        if (profileId < 0) {
            return true;
        }
        return false;
    }

    /**
     * 是否根据店铺需要跳转判断
     *
     * @param session
     * @return
     */
    public static boolean redirectLbsShop(Session session) {
        if (null == session) {
            return true;
        }
        if (null == session.getAttribute(BizConstants.SHOP_ID)) {
            return true;
        }
        long shopId = (long) session.getAttribute(BizConstants.SHOP_ID);
        if (shopId < 0) {
            return true;
        }
        return false;
    }


    /**
     * 是否需要跳转微信进行授权
     *
     * @param session
     * @return
     */
    public static boolean oauthRedirect(Session session) {

        if (StringUtils.isBlank((String) session.getAttribute(BizConstants.OPENID)) || org.apache.commons.lang.StringUtils.equals("null", (String) session.getAttribute(BizConstants.OPENID))) {
            return true;
        }
        return false;
    }


    /**
     * 匹配密码
     *
     * @param mobile
     * @param pswd
     * @return
     */
    public boolean checkPswdByMobile(String mobile, String pswd) {
        boolean pass = false;
        ProfileDOExample hidenProfileDOExample = new ProfileDOExample();
        hidenProfileDOExample.createCriteria().andMobileEqualTo(mobile).andStatusEqualTo((byte) 1);
        List<ProfileDO> profileDOs = profileDOMapper.selectByExample(hidenProfileDOExample);
        if (null != profileDOs && profileDOs.size() > 0) {
            String storedPswd = profileDOs.get(0).getPassword();
            if (StringUtils.isBlank(storedPswd)) {
                return false;
            }
            //密码对比
            if (BCrypt.checkpw(pswd, storedPswd)) {
                pass = true;
            }
        }

        return pass;
    }

    /**
     * 匹配密码
     *
     * @param hidenProfileDO
     * @param pswd
     * @return
     */
    public boolean checkPswdByMobile(ProfileDO hidenProfileDO, String pswd) {
        if (null == hidenProfileDO) {
            return false;
        }
        boolean pass = false;
        String storedPswd = hidenProfileDO.getPassword();
        if (StringUtils.isBlank(storedPswd)) {
            return false;
        }
        //密码对比
        if (BCrypt.checkpw(pswd, storedPswd)) {
            pass = true;
        } else {
            log.error("用户密码校验失败 input pswd:" + pswd + "profileId:" + hidenProfileDO.getId());
        }
        return pass;
    }

    /**
     * 获取用户设备信息
     *
     * @param request
     * @return
     */
    public static Constants.TradeFrom differentiateOS(HttpServletRequest request) {
        UserAgent ua = new UserAgent();
        String userAgent = request.getHeader(BizConstants.USER_AGENT);
        //recode ext info
        try {
            if (StringUtils.isNotBlank(userAgent)) {
                ua = JSON.parseObject(userAgent, UserAgent.class);
            }
            if (null != ua) {
                if (StringUtils.equals(ua.getMode(), "iPhone")) {
                    return Constants.TradeFrom.APP_IOS;
                }
                if (StringUtils.equals(ua.getMode(), "android")) {
                    return Constants.TradeFrom.APP_ANDROID;
                }
            }
        } catch (Exception e) {
            log.error("fetch agent ios/android/h5 failed.");
        }
        return Constants.TradeFrom.UNKNOWN;
    }

}

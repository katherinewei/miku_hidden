package com.hiden.web.controller;

import com.alibaba.fastjson.JSON;
import com.hiden.biz.common.BizConstants;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by myron on 16-9-28.
 */
@RestController
public class LogOut {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(LogOut.class);

    @RequestMapping(value = {"/api/m/1.0/logOut.json", "/api/h/1.0/logOut.json"}, produces = "application/json;charset=utf-8")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        try {
            //do logout
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
}

package com.hiden.biz.service;

import com.alibaba.fastjson.JSON;
import com.hiden.biz.cache.CheckNOGenerator;
import com.hiden.biz.common.BizConstants;
import com.hiden.biz.model.AlipayInfoModel;
import com.hiden.biz.model.UserInfo;
import com.hiden.biz.model.WxInfoModel;
import com.hiden.biz.wechat.mp.bean.result.WxMpOAuth2AccessToken;
import com.hiden.biz.wechat.mp.bean.result.WxMpUser;
import com.hiden.persistence.*;
import com.hiden.persistence.domain.*;
import com.hiden.utils.EmojiFilter;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by myron on 16-9-30.
 */
@Service
public class UserService {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(UserService.class);

    @Resource
    private ProfileDOMapper profileDOMapper;

    @Resource
    private MessageSummaryDOMapper messageSummaryDOMapper;

    @Resource
    private ProfileWechatDOMapper profileWeChatDOMapper;

    @Resource
    private WechatProfileDOMapper wechatProfileDOMapper;

    @Resource
    private ProfileCoopDOMapper profileCoopDOMapper;

    @Resource
    private CoopProfileDOMapper coopProfileDOMapper;

    /**
     * 根据openid获取存储的profile_wechat id 如果没有存储则返回-1l
     *
     * @param openid
     * @return
     */
    public long fetchWechatIdByOpenid(String openid) {
        long wechatId = -1l;
        if (StringUtils.isNotBlank(openid)) {
            ProfileWechatDOExample pExample = new ProfileWechatDOExample();
            pExample.createCriteria().andOpenidEqualTo(openid);
            List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(pExample);
            if (null != profileWeChatDOs && profileWeChatDOs.size() > 0) {
                wechatId = profileWeChatDOs.get(0).getId();
            }
        }
        return wechatId;
    }

    /**
     * 添加wechat info
     *
     * @param weChatUserInfo
     * @return
     */
    public boolean addWechatInfo(WxMpUser weChatUserInfo, ProfileWechatDO profileWeChatDO) {
        ProfileWechatDO pweChatDO = new ProfileWechatDO();
        pweChatDO.setCity(weChatUserInfo.getCity());
        pweChatDO.setCountry(weChatUserInfo.getCountry());
        pweChatDO.setDateCreated(new Date());
        pweChatDO.setLastUpdated(new Date());
        pweChatDO.setHeadimgurl(weChatUserInfo.getHeadImgUrl());
        pweChatDO.setNickname(EmojiFilter.filterEmoji(weChatUserInfo.getNickname()));
        pweChatDO.setOpenid(weChatUserInfo.getOpenId());
        pweChatDO.setStatus(1);
        pweChatDO.setVersion(1l);
        pweChatDO.setSex(weChatUserInfo.getSex());
        pweChatDO.setProvince(weChatUserInfo.getProvince());
        pweChatDO.setAccessToken(profileWeChatDO.getAccessToken());
        pweChatDO.setExpiresIn(profileWeChatDO.getExpiresIn());
        pweChatDO.setRefreshToken(profileWeChatDO.getRefreshToken());
        pweChatDO.setOpenid(profileWeChatDO.getOpenid());
        pweChatDO.setScope(profileWeChatDO.getScope());
        pweChatDO.setUnionId(weChatUserInfo.getUnionId());
        pweChatDO.setSubscribe(weChatUserInfo.isSubscribe());
        pweChatDO.setLanguage(weChatUserInfo.getLanguage());
        pweChatDO.setSubscribeTime(weChatUserInfo.getSubscribeTime());
        //1. add wechat info
        if (profileWeChatDOMapper.insertSelective(pweChatDO) < 0) {
            log.error("insert wechat user info failed. nick:" + weChatUserInfo.getNickname() + ",openid:" + weChatUserInfo.getOpenId());
            return false;
        }
        return true;
    }

    /**
     * 更新profile_wechat data
     *
     * @param user
     */
    public void updateProfileWechatInfo(WxMpUser user) {
        if (null == user || StringUtils.isBlank(user.getOpenId())) {
            return;
        }
        ProfileWechatDO profileWeChatDO = new ProfileWechatDO();
        profileWeChatDO.setLastLoginTime(new Date());
        if (StringUtils.isNotBlank(user.getNickname())) {
        	profileWeChatDO.setNickname(EmojiFilter.filterEmoji(user.getNickname()));
            //profileWeChatDO.setNickname(user.getNickname());
        }
        if (StringUtils.isNotBlank(user.getCity())) {
            profileWeChatDO.setCity(user.getCity());
        }
        if (StringUtils.isNotBlank(user.getCountry())) {
            profileWeChatDO.setCountry(user.getCountry());
        }
        if (StringUtils.isNotBlank(user.getProvince())) {
            profileWeChatDO.setProvince(user.getProvince());
        }
        if (StringUtils.isNotBlank(user.getHeadImgUrl())) {
            profileWeChatDO.setHeadimgurl(user.getHeadImgUrl());
        }
        if (StringUtils.isNotBlank(user.getUnionId())) {
            profileWeChatDO.setUnionId(user.getUnionId());
        }
        if (null != user.getSubscribe() && user.getSubscribe()) {
            profileWeChatDO.setSubscribe(user.getSubscribe());
        }
        if (null != user.getSubscribeTime() && user.getSubscribeTime() > 0) {
            profileWeChatDO.setSubscribeTime(user.getSubscribeTime());
        }

        ProfileWechatDOExample qpExample = new ProfileWechatDOExample();
        qpExample.createCriteria().andOpenidEqualTo(user.getOpenId());
        List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(qpExample);
        if (null != profileWeChatDO && profileWeChatDOs.size() > 0) {
            ProfileWechatDOExample pExample = new ProfileWechatDOExample();
            pExample.createCriteria().andIdEqualTo(profileWeChatDOs.get(0).getId());
            if (profileWeChatDOMapper.updateByExampleSelective(profileWeChatDO, pExample) < 1) {
                log.warn("update profileWechatDO failed. openId:" + user.getOpenId());
            }
        }
    }

    public boolean addWechatInfoByToken(WxMpUser weChatUserInfo, WxMpOAuth2AccessToken wxMpOAuth2AccessToken) {
        ProfileWechatDO pweChatDO = new ProfileWechatDO();
        pweChatDO.setCity(weChatUserInfo.getCity());
        pweChatDO.setCountry(weChatUserInfo.getCountry());
        pweChatDO.setDateCreated(new Date());
        pweChatDO.setLastUpdated(new Date());
        pweChatDO.setHeadimgurl(weChatUserInfo.getHeadImgUrl());
        pweChatDO.setNickname(EmojiFilter.filterEmoji(weChatUserInfo.getNickname()));
        pweChatDO.setOpenid(weChatUserInfo.getOpenId());
        pweChatDO.setStatus(1);
        pweChatDO.setVersion(1l);
        pweChatDO.setSex(weChatUserInfo.getSex());
        pweChatDO.setProvince(weChatUserInfo.getProvince());
        if (null != wxMpOAuth2AccessToken) {
            pweChatDO.setAccessToken(wxMpOAuth2AccessToken.getAccessToken());
            pweChatDO.setExpiresIn(wxMpOAuth2AccessToken.getExpiresIn());
            pweChatDO.setRefreshToken(wxMpOAuth2AccessToken.getRefreshToken());
            pweChatDO.setOpenid(wxMpOAuth2AccessToken.getOpenId());
            pweChatDO.setScope(wxMpOAuth2AccessToken.getScope());
        } else {
            pweChatDO.setExpiresIn(wxMpOAuth2AccessToken.getExpiresIn());
            pweChatDO.setRefreshToken(wxMpOAuth2AccessToken.getRefreshToken());
            pweChatDO.setOpenid(wxMpOAuth2AccessToken.getOpenId());
            pweChatDO.setScope(wxMpOAuth2AccessToken.getScope());
        }

        pweChatDO.setUnionId(weChatUserInfo.getUnionId());
        pweChatDO.setSubscribe(weChatUserInfo.isSubscribe());
        pweChatDO.setLanguage(weChatUserInfo.getLanguage());
        pweChatDO.setSubscribeTime(weChatUserInfo.getSubscribeTime());
        //1. add wechat info
        if (profileWeChatDOMapper.insertSelective(pweChatDO) < 0) {
            log.error("insert wechat user info failed. nick:" + weChatUserInfo.getNickname() + ",openid:" + weChatUserInfo.getOpenId());
            return false;
        }
        return true;
    }

    /**
     * 通过手机号进行同步
     *
     * @param weChatUserInfo
     * @param session
     * @return
     */
    public boolean synchromProfileByMobile(WxMpUser weChatUserInfo, ProfileWechatDO profileWeChatDO, Session session, ProfileDO profileDO) {
        if (synchroned(weChatUserInfo.getOpenId())) {
            session.setAttribute(BizConstants.PROFILE_ID, profileDO.getId());
            return true;
        }
        //是否已经关联
        long relaProfileId = fetchProfileIdByOpenid(weChatUserInfo.getOpenId());
        if (relaProfileId > 0) {
            session.setAttribute(BizConstants.PROFILE_ID, profileDO.getId());
            session.setAttribute(BizConstants.UNION_ID, weChatUserInfo.getUnionId());
            return true;
        } else {
            //直接进行关联
            WechatProfileDO weChatProfileDO = new WechatProfileDO();
            weChatProfileDO.setStatus((byte) 1);
            weChatProfileDO.setVersion(1l);
            weChatProfileDO.setDateCreated(new Date());
            weChatProfileDO.setProfileId(profileDO.getId());
            weChatProfileDO.setWechatId(profileWeChatDO.getId());
            weChatProfileDO.setLastUpdated(new Date());
            weChatProfileDO.setUnionId(weChatUserInfo.getUnionId());
            if (wechatProfileDOMapper.insertSelective(weChatProfileDO) < 0) {
                log.error("insert we chat profile relationship table failed.  nick:" + weChatUserInfo.getNickname() + ",openid:" + weChatUserInfo.getOpenId() + " sessionid:" + session.getId());
                profileDOMapper.deleteByPrimaryKey(profileDO.getId());
                return false;
            }
            session.setAttribute(BizConstants.PROFILE_ID, profileDO.getId());
            session.setAttribute(BizConstants.UNION_ID, weChatUserInfo.getUnionId());
            ProfileWechatDO uProfileWechatDO = new ProfileWechatDO();
            uProfileWechatDO.setSynchron((byte) 1);
            uProfileWechatDO.setId(profileWeChatDO.getId());
            uProfileWechatDO.setLastLoginTime(new Date());
            if (StringUtils.isNotBlank(weChatUserInfo.getUnionId())) {
                uProfileWechatDO.setUnionId(weChatUserInfo.getUnionId());
            }
            profileWeChatDOMapper.updateByPrimaryKeySelective(uProfileWechatDO);
        }
        return true;
    }

    /**
     * 该微信xxOpenidxx  -->  unionId是否已经关联过数据
     *
     * @param openid
     * @return
     */
    public boolean synchroned(String openid) {
        if (StringUtils.isBlank(openid)) {
            return false;
        }
        ProfileWechatDOExample pExample = new ProfileWechatDOExample();
        pExample.createCriteria().andOpenidEqualTo(openid);
        List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(pExample);
        if (null != profileWeChatDOs && profileWeChatDOs.size() > 0) {
            String unionId = profileWeChatDOs.get(0).getUnionId();
            WechatProfileDOExample qwExample = new WechatProfileDOExample();
            qwExample.createCriteria().andUnionIdEqualTo(unionId);
            int wechatCount = wechatProfileDOMapper.countByExample(qwExample);
            if (wechatCount > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 该第三方登陆用户是否已经关联了Profile
     *
     * @param userId
     * @return
     */
    public boolean coopSynchroned(String userId) {
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        ProfileCoopDOExample pExample = new ProfileCoopDOExample();
        //pExample.createCriteria().andUserIdEqualTo(userId);
        List<ProfileCoopDO> profileCoopDOs = profileCoopDOMapper.selectByExample(pExample);
        if (null != profileCoopDOs && profileCoopDOs.size() > 0) {
            long coopId = profileCoopDOs.get(0).getId();
            CoopProfileDOExample qcExample = new CoopProfileDOExample();
            qcExample.createCriteria().andCoopIdEqualTo(coopId);
            int coopRelationCount = coopProfileDOMapper.countByExample(qcExample);
            if (coopRelationCount > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 对于公众账户进来的用户，判断是否手机号方式已经登录
     *
     * @param openid
     * @param session
     * @return
     */
    public boolean isMobileUserLogedin(String openid, Session session) {
        if (null != session && null != session.getAttribute(BizConstants.PROFILE_ID)) {
            return true;
        }
        return false;
    }
    
    /**
     * 根据openid获取关联关系
     *
     * @param openid
     * @return
     */
    public long fetchProfileIdByOpenid(String openid) {
        ProfileWechatDOExample pwExample = new ProfileWechatDOExample();
        pwExample.createCriteria().andOpenidEqualTo(openid);
        List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(pwExample);
        if (null != profileWeChatDOs && profileWeChatDOs.size() > 0) {
            String unionId = profileWeChatDOs.get(0).getUnionId();
            WechatProfileDOExample qwpExample = new WechatProfileDOExample();
            qwpExample.createCriteria().andUnionIdEqualTo(unionId);
            List<WechatProfileDO> wechatProfileDOs = wechatProfileDOMapper.selectByExample(qwpExample);
            if (null != wechatProfileDOs && wechatProfileDOs.size() > 0) {
                return wechatProfileDOs.get(0).getProfileId();
            } else {
                return -1l;
            }
        } else {
            return -1l;
        }
    }

    /**
     * 根据openid从数据库中获取用户的微信信息
     *
     * @param openid
     * @return
     */
    public ProfileWechatDO fetchProfileWechatByOpenid(String openid) {
        ProfileWechatDOExample pwExample = new ProfileWechatDOExample();
        pwExample.createCriteria().andOpenidEqualTo(openid);
        List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(pwExample);
        if (null != profileWeChatDOs && profileWeChatDOs.size() > 0) {
            return profileWeChatDOs.get(0);
        } else {
            return null;
        }
    }

    /**
     * 添加微信用户信息
     *
     * @param weChatUserInfo
     * @return
     */
    public boolean addSyncWeChatUserByMock(WxMpUser weChatUserInfo, ProfileWechatDO profileWeChatDO, Session session, String mobile) {
        if (synchroned(weChatUserInfo.getOpenId())) {
            return true;
        }
        //判断关联关系是否存在
        WechatProfileDOExample qwExample = new WechatProfileDOExample();
        qwExample.createCriteria().andWechatIdEqualTo(profileWeChatDO.getId());
        List<WechatProfileDO> wpDOs = wechatProfileDOMapper.selectByExample(qwExample);
        if (null != wpDOs && wpDOs.size() > 0) {
            //如果原先未存储unionId,存储之
            WechatProfileDO wechatProfileDO = new WechatProfileDO();
            wechatProfileDO.setUnionId(wpDOs.get(0).getUnionId());
            WechatProfileDOExample wExample = new WechatProfileDOExample();
            wExample.createCriteria().andIdEqualTo(wpDOs.get(0).getId());
            wechatProfileDOMapper.updateByExampleSelective(wechatProfileDO, wExample);

            session.setAttribute(BizConstants.PROFILE_ID, wpDOs.get(0).getProfileId());
            session.setAttribute(BizConstants.UNION_ID, weChatUserInfo.getUnionId());
            return true;
        } else {
            //添加profile并建立对应关系
            //2. add profile
            long profileWchatId = -1l;
            ProfileDO profileDO = new ProfileDO();
            profileDO.setDateCreated(new Date());
            profileDO.setLastUpdated(new Date());
            profileDO.setProfilePic(weChatUserInfo.getHeadImgUrl());
            profileDO.setType(BizConstants.LoginEnum.SERVICE.getType());
            profileDO.setStatus((byte) 1);
            profileDO.setMobile(mobile);
            String checkCode = CheckNOGenerator.getFixLenthString(BizConstants.CHECK_NO_LEN);
    		if(null != mobile && mobile.length() > 4){
    			profileDO.setNickname(mobile.substring(mobile.length()-4, mobile.length())+checkCode);
    		}else{
    			profileDO.setNickname(CheckNOGenerator.getFixLenthString(6));
    		}
            if (profileDOMapper.insertSelective(profileDO) < 0) {
                log.error("insert we chat profile table failed.  nick:" + weChatUserInfo.getNickname() + ",openid:" + weChatUserInfo.getOpenId() + " sessionid:" + session.getId());
                ProfileWechatDOExample pExample = new ProfileWechatDOExample();
                pExample.createCriteria().andOpenidEqualTo(weChatUserInfo.getOpenId());
                List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(pExample);
                if (null != profileWeChatDOs && profileWeChatDOs.size() > 0) {
                    profileWchatId = profileWeChatDOs.get(0).getId();
                    profileWeChatDOMapper.deleteByPrimaryKey(profileWchatId);
                }
                return false;
            } else {
                //3. 添加对应关系
                WechatProfileDO weChatProfileDO = new WechatProfileDO();
                weChatProfileDO.setStatus((byte) 1);
                weChatProfileDO.setVersion(1l);
                weChatProfileDO.setDateCreated(new Date());
                weChatProfileDO.setProfileId(profileDO.getId());
                weChatProfileDO.setWechatId(profileWeChatDO.getId());
                weChatProfileDO.setLastUpdated(new Date());
                weChatProfileDO.setUnionId(weChatUserInfo.getUnionId());
                if (wechatProfileDOMapper.insertSelective(weChatProfileDO) < 0) {
                    log.error("insert we chat profile relationship table failed.  nick:" + weChatUserInfo.getNickname() + ",openid:" + weChatUserInfo.getOpenId());
                    profileDOMapper.deleteByPrimaryKey(profileDO.getId());
                    return false;
                }
                session.setAttribute(BizConstants.PROFILE_ID, profileDO.getId());
                session.setAttribute(BizConstants.UNION_ID, weChatUserInfo.getUnionId());
                ProfileWechatDO uProfileWechatDO = new ProfileWechatDO();
                uProfileWechatDO.setSynchron((byte) 1);
                uProfileWechatDO.setId(profileWeChatDO.getId());
                uProfileWechatDO.setLastLoginTime(new Date());
                profileWeChatDOMapper.updateByPrimaryKeySelective(uProfileWechatDO);
            }
        }
        return true;
    }

    public ProfileDO fetchProfileById(long profileId) {
        return profileDOMapper.selectByPrimaryKey(profileId);
    }

    /**
     * 根据电话号码设置
     *
     * @param mobile
     * @return
     */
    public ProfileDO fetchProfileByMobile(String mobile) {
        ProfileDOExample pExample = new ProfileDOExample();
        pExample.createCriteria().andMobileEqualTo(mobile).andStatusEqualTo((byte) 1);
        List<ProfileDO> profileDOs = profileDOMapper.selectByExample(pExample);
        if (null != profileDOs && profileDOs.size() > 0) {
            return profileDOs.get(0);
        }
        return null;
    }

    /**
     * 手机号是否有账户
     *
     * @param mobile
     * @return
     */
    public boolean hasMobileAccount(String mobile) {
        boolean hasAccount = false;
        if (StringUtils.isNotBlank(mobile)) {
            if (null != fetchProfileByMobile(mobile)) {
                hasAccount = true;
            }
        }
        return hasAccount;
    }

    /**
     * 获取用户未读消息数量
     *
     * @param profileId
     * @param communityId
     * @return
     */
    public int fetchMsgCount(long profileId, long communityId) {

        MessageSummaryDOExample mExample = new MessageSummaryDOExample();
        mExample.createCriteria().andProfileIdEqualTo(profileId).andCommunityIdEqualTo(communityId)
                .andBizTypeIn(BizConstants.AllBizTypes);
        List<MessageSummaryDO> messageSummaryDOs = messageSummaryDOMapper.selectByExample(mExample);
        int count = 0;
        for (MessageSummaryDO m : messageSummaryDOs) {
            count += m.getNonReadCount();
        }
        return count;
    }

    /**
     * 根据用户权限查询成员信息
     *
     * @param profileId
     * @return
     */
    public List<UserInfo> fetchValidUsersByCommunityAndProfileId(long profileId) {
        //fetch users(tenant)
        List<UserInfo> users = new ArrayList<UserInfo>();
        ProfileDOExample pExample = new ProfileDOExample();
        pExample.createCriteria().andIdEqualTo(profileId).andStatusEqualTo((byte) 1);
        List<ProfileDO> profileDOs = profileDOMapper.selectByExample(pExample);
        if (null != profileDOs && profileDOs.size() > 0) {
            UserInfo userInfo = new UserInfo();
            userInfo.setProfileDO(profileDOs.get(0));
            users.add(userInfo);
            return users;
        } else {
            return null;
        }
    }

    public String list2Json(List list) {
        if (null == list || (list != null && list.size() > 0)) {
            return null;
        }
        String jsonText = JSON.toJSONString(list, true);
        return jsonText;
    }

    private boolean containsConfig(long tag, BizConstants.UserNotifyTagEnum userNotifyTagEnum) {
        long subTag = userNotifyTagEnum.getUserNotifyTagId();
        return (tag & subTag) == subTag;
    }

    /**
     * 是否已经关联过union_id
     *
     * @param openid
     */
    public boolean isOauthed(String openid) {
        ProfileWechatDOExample qpExample = new ProfileWechatDOExample();
        qpExample.createCriteria().andOpenidEqualTo(openid);
        List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(qpExample);
        if (null != profileWeChatDOs && profileWeChatDOs.size() > 0) {
            for (ProfileWechatDO pDO : profileWeChatDOs) {//有存储过微信信息且已经获取过union_id
                if (StringUtils.isNotBlank(pDO.getUnionId())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据openid获取用户微信信息
     *
     * @param openid
     * @return
     */
    public ProfileWechatDO fetchWxInfo(String openid) {
        ProfileWechatDOExample qpExample = new ProfileWechatDOExample();
        qpExample.createCriteria().andOpenidEqualTo(openid);
        List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(qpExample);
        if (null != profileWeChatDOs && profileWeChatDOs.size() > 0) {
            return profileWeChatDOs.get(0);
        }
        return null;
    }

    /**
     * 判断联合登陆是否已经保存过用户信息
     *
     * @param loginType
     * @param openid
     * @return
     */
    public boolean isCoopInfoStored(String loginType, String openid) {
        ProfileCoopDOExample qpExample = new ProfileCoopDOExample();
        qpExample.createCriteria().andOpenidEqualTo(openid).andTypeEqualTo(Byte.valueOf(loginType));
        List<ProfileCoopDO> profileCoopDOs = profileCoopDOMapper.selectByExample(qpExample);
        if (null != profileCoopDOs && profileCoopDOs.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取profile coop by union id
     *
     * @param loginType
     * @param unionId
     * @return
     */
    public ProfileCoopDO fetchProfileCoopByUnionId(String loginType, String unionId) {
        ProfileCoopDOExample qpExample = new ProfileCoopDOExample();
        qpExample.createCriteria().andUnionIdEqualTo(unionId).andTypeEqualTo(Byte.valueOf(loginType));
        List<ProfileCoopDO> profileCoopDOs = profileCoopDOMapper.selectByExample(qpExample);
        if (null != profileCoopDOs && profileCoopDOs.size() > 0) {
            return profileCoopDOs.get(0);
        }
        return null;
    }

    /**
     * 添加联合登陆之用户信息
     *
     * @param loginType
     * @param wxInfoModel
     * @return
     */
    public boolean addCoopInfo(String loginType, WxInfoModel wxInfoModel, Session session) {
        ProfileCoopDO profileCoopDO = new ProfileCoopDO();
        profileCoopDO.setCity(wxInfoModel.getCity());
        profileCoopDO.setCountry(wxInfoModel.getCountry());
        profileCoopDO.setHeadimgurl(wxInfoModel.getHeadimgurl());
        profileCoopDO.setLanguage(wxInfoModel.getLanguage());
        profileCoopDO.setNickname(EmojiFilter.filterEmoji(wxInfoModel.getNickname()));
        profileCoopDO.setOpenid(wxInfoModel.getOpenid());
        profileCoopDO.setSex(wxInfoModel.getSex());
        profileCoopDO.setUnionId(wxInfoModel.getUnionid());
        profileCoopDO.setVersion(1l);
        profileCoopDO.setDateCreated(new Date());
        profileCoopDO.setLastUpdated(new Date());
        profileCoopDO.setType(Byte.valueOf(loginType));
        profileCoopDO.setStatus(1);

        if (profileCoopDOMapper.insertSelective(profileCoopDO) < 0) {
            log.error("add wx user info failed. nick:" + wxInfoModel.getNickname() + ",openid:" + wxInfoModel.getOpenid());
            return false;
        }
        return true;
    }

    /**
     * 添加联合登陆之用户信息
     *
     * @param loginType
     * @param alipayInfoModel
     * @return
     */
    public boolean addCoopInfo(String loginType, AlipayInfoModel alipayInfoModel, Session session) {
        ProfileCoopDO profileCoopDO = new ProfileCoopDO();
        profileCoopDO.setVersion(1l);
        profileCoopDO.setDateCreated(new Date());
        profileCoopDO.setLastUpdated(new Date());
        profileCoopDO.setType(Byte.valueOf(loginType));
        profileCoopDO.setStatus(1);

        if (profileCoopDOMapper.insertSelective(profileCoopDO) < 0) {
            log.error("add alipay user info failed. userId:" + alipayInfoModel.getUserId());
            return false;
        }
        return true;
    }

    /**
     * 关联用户(如果用户对应的手机已经注册过app，则直接进行关联；否则Q用户并进行关联)
     *
     * @param mobile
     * @param profileCoopDO
     * @param loginType
     * @return
     */
    public boolean relaUser(String mobile, ProfileCoopDO profileCoopDO, String loginType, Session session) {
        ProfileDO profileDO = fetchProfileByMobile(mobile);
        //1. 存在profile 用户
        if (null != profileDO) {
            //1.1 存在用户进行关联
            return relaUser(mobile, loginType, profileDO, profileCoopDO, session);
        }
        //2. 不存在profile 用户
        else {

            //2.2 关联用户
            ProfileDOExample qqExample = new ProfileDOExample();
            qqExample.createCriteria().andMobileEqualTo(mobile).andStatusEqualTo((byte) 1);
            List<ProfileDO> profileDOs = profileDOMapper.selectByExample(qqExample);
            if (null != profileDOs && profileDOs.size() > 0) {
                return relaUser(mobile, loginType, profileDOs.get(0), profileCoopDO, session);
            } else {
                return false;
            }
        }
    }

    public boolean bindWxByProfileIdAndOpenId(long profileId, String openId) {
        ProfileWechatDOExample pExample = new ProfileWechatDOExample();
        pExample.createCriteria().andOpenidEqualTo(openId);
        List<ProfileWechatDO> profileWeChatDOs = profileWeChatDOMapper.selectByExample(pExample);
        if (null != profileWeChatDOs && profileWeChatDOs.size() > 0) {
            WechatProfileDO weChatProfileDO = new WechatProfileDO();
            weChatProfileDO.setLastUpdated(new Date());
            weChatProfileDO.setUnionId(profileWeChatDOs.get(0).getUnionId());
            weChatProfileDO.setWechatId(profileWeChatDOs.get(0).getId());
            weChatProfileDO.setProfileId(profileId);
            weChatProfileDO.setDateCreated(new Date());
            weChatProfileDO.setStatus((byte) 1);
            weChatProfileDO.setVersion(1l);
            wechatProfileDOMapper.insertSelective(weChatProfileDO);
            return true;
        }
        return false;
    }

    private boolean relaUser(String mobile, String loginType, ProfileDO profileDO, ProfileCoopDO profileCoopDO, Session session) {
        CoopProfileDO coopProfileDO = new CoopProfileDO();
        coopProfileDO.setType(Byte.valueOf(loginType));
        coopProfileDO.setVersion(1l);
        coopProfileDO.setCoopId(profileCoopDO.getId());
        coopProfileDO.setProfileId(profileDO.getId());
        coopProfileDO.setDateCreated(new Date());
        coopProfileDO.setLastUpdated(new Date());
        coopProfileDO.setStatus((byte) 1);
        if (coopProfileDOMapper.insertSelective(coopProfileDO) < 0) {
            log.error("add coop profile relationship failed. mobile:" + mobile);
            return false;
        }
        session.setAttribute(BizConstants.MOBILE, mobile);
        session.setAttribute(BizConstants.PROFILE_ID, profileDO.getId());
        return true;
    }

    /**
     * wx 是否关联过profileCoop -- profile
     *
     * @return
     */
    public boolean relatedCheck(ProfileCoopDO profileCoopDO, Session session) {
        CoopProfileDOExample cExample = new CoopProfileDOExample();
        cExample.createCriteria().andCoopIdEqualTo(profileCoopDO.getId());
        List<CoopProfileDO> coopProfileDOs = coopProfileDOMapper.selectByExample(cExample);
        if (null != coopProfileDOs && coopProfileDOs.size() > 0) {
            session.setAttribute(BizConstants.PROFILE_ID, coopProfileDOs.get(0).getProfileId());
            return true;
        }
        return false;
    }

    /**
     * 是否已经绑定过
     *
     * @param loginType
     * @param
     * @param profileCoopId
     * @return
     */
    public long hasBindUser(String loginType, long profileCoopId, Session session) {
        CoopProfileDOExample qcExample = new CoopProfileDOExample();
        qcExample.createCriteria().andTypeEqualTo(Byte.valueOf(loginType)).andCoopIdEqualTo(profileCoopId);
        List<CoopProfileDO> coopProfileDOs = coopProfileDOMapper.selectByExample(qcExample);
        if (null != coopProfileDOs && coopProfileDOs.size() > 0) {
            session.setAttribute(BizConstants.PROFILE_ID, coopProfileDOs.get(0).getProfileId());
            return coopProfileDOs.get(0).getProfileId();
        }
        return -1;
    }

    /**
     * 判断unionid 是否已经绑定过profile
     *
     * @param unionId
     * @return profileId
     */
    public long checkWxMpBinded(String unionId) {
        WechatProfileDOExample qExample = new WechatProfileDOExample();
        qExample.createCriteria().andUnionIdEqualTo(unionId);
        List<WechatProfileDO> weChatProfileDOs = wechatProfileDOMapper.selectByExample(qExample);
        if (null != weChatProfileDOs && weChatProfileDOs.size() > 0) {
            return weChatProfileDOs.get(0).getProfileId();
        }
        return -1l;
    }

    public long checkWxMpBinded(long profileId) {
        WechatProfileDOExample qExample = new WechatProfileDOExample();
        qExample.createCriteria().andProfileIdEqualTo(profileId);
        List<WechatProfileDO> weChatProfileDOs = wechatProfileDOMapper.selectByExample(qExample);
        if (null != weChatProfileDOs && weChatProfileDOs.size() > 0) {
            return weChatProfileDOs.get(0).getProfileId();
        }
        return -1l;
    }

    /**
     * 添加profile
     *
     * @param mobile
     * @return
     */
    public boolean addProfile(String mobile) {
        ProfileDO profileDO = new ProfileDO();
        profileDO.setType(BizConstants.LoginEnum.H5_MOBILE_CODE.getType());
        profileDO.setDateCreated(new Date());
        profileDO.setLastUpdated(new Date());
        profileDO.setStatus((byte) 1);
        profileDO.setMobile(mobile);
        String checkCode = CheckNOGenerator.getFixLenthString(BizConstants.CHECK_NO_LEN);
		if(null != mobile && mobile.length() > 4){
			profileDO.setNickname(mobile.substring(mobile.length()-4, mobile.length())+checkCode);
		}else{
			profileDO.setNickname(CheckNOGenerator.getFixLenthString(6));
		}
        if (profileDOMapper.insertSelective(profileDO) > 0) {
            return true;
        }
        return false;
    }

    public long fetchLastLoginShop(Session session) {
        if (null != session && null != session.getAttribute(BizConstants.SHOP_ID)) {
            return (long) session.getAttribute(BizConstants.SHOP_ID);
        } else {

        }
        return 0;
    }
    
    public ProfileWechatDO getProfileWeChatByProfileId(Long profileId){
    	try {
    		WechatProfileDOExample weChatProfileDOExample = new WechatProfileDOExample();
    		weChatProfileDOExample.createCriteria().andProfileIdEqualTo(profileId);
    		List<WechatProfileDO> weChatProfileDOList = wechatProfileDOMapper.selectByExample(weChatProfileDOExample);
    		if(null != weChatProfileDOList && !weChatProfileDOList.isEmpty()){
    			return profileWeChatDOMapper.selectByPrimaryKey(weChatProfileDOList.get(0).getWechatId());
    		}
		} catch (Exception e) {
		}
    	return null;
    }
    
}

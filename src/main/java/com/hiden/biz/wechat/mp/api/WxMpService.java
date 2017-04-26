package com.hiden.biz.wechat.mp.api;

import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.model.response.WxJsapiSignature;
import com.hiden.biz.wechat.common.model.response.WxMediaUploadResult;
import com.hiden.biz.wechat.common.model.response.WxMenu;
import com.hiden.biz.wechat.common.util.http.RequestExecutor;
import com.hiden.biz.wechat.mp.bean.*;
import com.hiden.biz.wechat.mp.bean.result.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface WxMpService {
    SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    boolean checkSignature(String var1, String var2, String var3);

    boolean checkSignature(String var1, String var2, String var3, String var4);

    String getAccessToken() throws WxErrorException;

    String getAccessToken(String var1) throws WxErrorException;

    String getAccessToken(boolean var1) throws WxErrorException;

    String getAccessToken(boolean var1, String var2) throws WxErrorException;

    String getJsapiTicket() throws WxErrorException;

    String getJsapiTicket(String var1) throws WxErrorException;

    String getJsapiTicket(boolean var1) throws WxErrorException;

    String getJsapiTicket(boolean var1, String var2) throws WxErrorException;

    WxJsapiSignature createJsapiSignature(String var1) throws WxErrorException;

    WxMediaUploadResult mediaUpload(String var1, String var2, InputStream var3) throws WxErrorException, IOException;

    WxMediaUploadResult mediaUpload(String var1, File var2) throws WxErrorException;

    File mediaDownload(String var1) throws WxErrorException;

    void customMessageSend(WxMpCustomMessage var1) throws WxErrorException;

    WxMpMassUploadResult massNewsUpload(WxMpMassNews var1) throws WxErrorException;

    WxMpMassUploadResult massVideoUpload(WxMpMassVideo var1) throws WxErrorException;

    WxMpMassSendResult massGroupMessageSend(WxMpMassGroupMessage var1) throws WxErrorException;

    WxMpMassSendResult massOpenIdsMessageSend(WxMpMassOpenIdsMessage var1) throws WxErrorException;

    void menuCreate(WxMenu var1) throws WxErrorException;

    void menuDelete() throws WxErrorException;

    WxMenu menuGet() throws WxErrorException;

    WxMpGroup groupCreate(String var1) throws WxErrorException;

    List<WxMpGroup> groupGet() throws WxErrorException;

    long userGetGroup(String var1) throws WxErrorException;

    void groupUpdate(WxMpGroup var1) throws WxErrorException;

    void userUpdateGroup(String var1, long var2) throws WxErrorException;

    void userUpdateRemark(String var1, String var2) throws WxErrorException;

    WxMpUser userInfo(String var1, String var2) throws WxErrorException;

    WxMpUser userInfo(String var1, String var2, String var3) throws WxErrorException;

    WxMpUserList userList(String var1) throws WxErrorException;

    WxMpQrCodeTicket qrCodeCreateTmpTicket(int var1, Integer var2) throws WxErrorException;

    WxMpQrCodeTicket qrCodeCreateLastTicket(int var1) throws WxErrorException;

    File qrCodePicture(WxMpQrCodeTicket var1) throws WxErrorException;

    String shortUrl(String var1) throws WxErrorException;

    String templateSend(WxMpTemplateMessage var1) throws WxErrorException;

    WxMpSemanticQueryResult semanticQuery(WxMpSemanticQuery var1) throws WxErrorException;

    String oauth2buildAuthorizationUrl(String var1, String var2);

    WxMpOAuth2AccessToken oauth2getAccessToken(String var1) throws WxErrorException;

    WxMpOAuth2AccessToken oauth2getAccessToken(String var1, String var2) throws WxErrorException;

    WxMpOAuth2AccessToken oauth2refreshAccessToken(String var1) throws WxErrorException;

    WxMpOAuth2AccessToken oauth2refreshAccessToken(String var1, String var2) throws WxErrorException;

    WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken var1, String var2) throws WxErrorException;

    WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken var1, String var2, String var3) throws WxErrorException;

    boolean oauth2validateAccessToken(WxMpOAuth2AccessToken var1);

    boolean oauth2validateAccessToken(WxMpOAuth2AccessToken var1, String var2);

    String[] getCallbackIP() throws WxErrorException;

    List<WxMpUserSummary> getUserSummary(Date var1, Date var2) throws WxErrorException;

    List<WxMpUserCumulate> getUserCumulate(Date var1, Date var2) throws WxErrorException;

    String get(String var1, String var2) throws WxErrorException;

    String post(String var1, String var2) throws WxErrorException;

    <T, E> T execute(RequestExecutor<T, E> var1, String var2, E var3) throws WxErrorException;

    <T, E> T execute(RequestExecutor<T, E> var1, String var2, E var3, String var4) throws WxErrorException;

    void setWxMpConfigStorage(WxMpConfigStorage var1);

    void setRetrySleepMillis(int var1);

    void setMaxRetryTimes(int var1);
}

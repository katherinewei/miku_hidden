package com.hiden.biz.wechat.mp.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.hiden.biz.wechat.common.exception.WxErrorException;
import com.hiden.biz.wechat.common.model.response.WxAccessToken;
import com.hiden.biz.wechat.common.model.response.WxJsapiSignature;
import com.hiden.biz.wechat.common.model.response.WxMediaUploadResult;
import com.hiden.biz.wechat.common.model.response.WxMenu;
import com.hiden.biz.wechat.common.model.response.error.WxError;
import com.hiden.biz.wechat.common.session.StandardSessionManager;
import com.hiden.biz.wechat.common.session.WxSessionManager;
import com.hiden.biz.wechat.common.util.RandomUtils;
import com.hiden.biz.wechat.common.util.StringUtils;
import com.hiden.biz.wechat.common.util.crypto.SHA1;
import com.hiden.biz.wechat.common.util.fileUtil.FileUtils;
import com.hiden.biz.wechat.common.util.http.*;
import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.*;
import com.hiden.biz.wechat.mp.bean.result.*;
import com.hiden.biz.wechat.mp.util.http.QrCodeRequestExecutor;
import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class WxMpServiceImpl implements WxMpService {
    protected final Logger log = LoggerFactory.getLogger(WxMpServiceImpl.class);
    protected final Object globalAccessTokenRefreshLock = new Object();
    protected final Object globalJsapiTicketRefreshLock = new Object();
    protected WxMpConfigStorage wxMpConfigStorage;
    protected CloseableHttpClient httpClient;
    protected HttpHost httpProxy;
    private int retrySleepMillis = 1000;
    private int maxRetryTimes = 5;
    protected WxSessionManager sessionManager = new StandardSessionManager();

    public WxMpServiceImpl() {
    }

    public boolean checkSignature(String timestamp, String nonce, String signature) {
        try {
            return SHA1.gen(new String[]{this.wxMpConfigStorage.getToken(), timestamp, nonce}).equals(signature);
        } catch (Exception var5) {
            return false;
        }
    }

    public boolean checkSignature(String timestamp, String nonce, String signature, String mpTag) {
        try {
            return SHA1.gen(new String[]{this.wxMpConfigStorage.getToken(mpTag), timestamp, nonce}).equals(signature);
        } catch (Exception var6) {
            return false;
        }
    }

    public String getAccessToken() throws WxErrorException {
        return this.getAccessToken(false);
    }

    public String getAccessToken(String mpTag) throws WxErrorException {
        return this.getAccessToken(false, mpTag);
    }

    public String getAccessToken(boolean forceRefresh) throws WxErrorException {
        if(forceRefresh) {
            this.wxMpConfigStorage.expireAccessToken();
        }

        if(this.wxMpConfigStorage.isAccessTokenExpired()) {
            Object var2 = this.globalAccessTokenRefreshLock;
            synchronized(this.globalAccessTokenRefreshLock) {
                if(this.wxMpConfigStorage.isAccessTokenExpired()) {
                    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + this.wxMpConfigStorage.getAppId() + "&secret=" + this.wxMpConfigStorage.getSecret();

                    try {
                        HttpGet e = new HttpGet(url);
                        if(this.httpProxy != null) {
                            RequestConfig httpclient = RequestConfig.custom().setProxy(this.httpProxy).build();
                            e.setConfig(httpclient);
                        }

                        CloseableHttpClient httpclient1 = this.getHttpclient();
                        CloseableHttpResponse response = httpclient1.execute(e);
                        String resultContent = (new BasicResponseHandler()).handleResponse(response);
                        WxError error = WxError.fromJson(resultContent);
                        if(error.getErrorCode() != 0) {
                            throw new WxErrorException(error);
                        }

                        WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
                        this.wxMpConfigStorage.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
                    } catch (ClientProtocolException var11) {
                        this.log.error("==========appid:" + this.wxMpConfigStorage.getAppId());
                        this.log.error("==========secret:" + this.wxMpConfigStorage.getSecret());
                        throw new RuntimeException(var11);
                    } catch (IOException var12) {
                        this.log.error("==========appid:" + this.wxMpConfigStorage.getAppId());
                        this.log.error("==========secret:" + this.wxMpConfigStorage.getSecret());
                        throw new RuntimeException(var12);
                    }
                }
            }
        }

        return this.wxMpConfigStorage.getAccessToken();
    }

    public String getAccessToken(boolean forceRefresh, String mpTag) throws WxErrorException {
        if(forceRefresh) {
            this.wxMpConfigStorage.expireAccessToken(mpTag);
        }

        if(this.wxMpConfigStorage.isAccessTokenExpired(mpTag)) {
            Object var3 = this.globalAccessTokenRefreshLock;
            synchronized(this.globalAccessTokenRefreshLock) {
                if(this.wxMpConfigStorage.isAccessTokenExpired(mpTag)) {
                    String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + this.wxMpConfigStorage.getAppId(mpTag) + "&secret=" + this.wxMpConfigStorage.getSecret(mpTag);

                    try {
                        HttpGet e = new HttpGet(url);
                        if(this.httpProxy != null) {
                            RequestConfig httpclient = RequestConfig.custom().setProxy(this.httpProxy).build();
                            e.setConfig(httpclient);
                        }

                        CloseableHttpClient httpclient1 = this.getHttpclient();
                        CloseableHttpResponse response = httpclient1.execute(e);
                        String resultContent = (new BasicResponseHandler()).handleResponse(response);
                        WxError error = WxError.fromJson(resultContent);
                        if(error.getErrorCode() != 0) {
                            throw new WxErrorException(error);
                        }

                        WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
                        this.wxMpConfigStorage.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn(), mpTag);
                    } catch (ClientProtocolException var12) {
                        throw new RuntimeException(var12);
                    } catch (IOException var13) {
                        throw new RuntimeException(var13);
                    }
                }
            }
        }

        return this.wxMpConfigStorage.getAccessToken(mpTag);
    }

    public String getJsapiTicket() throws WxErrorException {
        return this.getJsapiTicket(false);
    }

    public String getJsapiTicket(String mpTag) throws WxErrorException {
        return this.getJsapiTicket(false, mpTag);
    }

    public String getJsapiTicket(boolean forceRefresh) throws WxErrorException {
        if(forceRefresh) {
            this.wxMpConfigStorage.expireJsapiTicket();
        }

        if(this.wxMpConfigStorage.isJsapiTicketExpired()) {
            Object var2 = this.globalJsapiTicketRefreshLock;
            synchronized(this.globalJsapiTicketRefreshLock) {
                if(this.wxMpConfigStorage.isJsapiTicketExpired()) {
                    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
                    String responseContent = (String)this.execute(new SimpleGetRequestExecutor(), url, null);
                    JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
                    JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
                    String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
                    int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
                    this.wxMpConfigStorage.updateJsapiTicket(jsapiTicket, expiresInSeconds);
                }
            }
        }

        return this.wxMpConfigStorage.getJsapiTicket();
    }

    public String getJsapiTicket(boolean forceRefresh, String mpTag) throws WxErrorException {
        if(forceRefresh) {
            this.wxMpConfigStorage.expireJsapiTicket(mpTag);
        }

        if(this.wxMpConfigStorage.isJsapiTicketExpired(mpTag)) {
            Object var3 = this.globalJsapiTicketRefreshLock;
            synchronized(this.globalJsapiTicketRefreshLock) {
                if(this.wxMpConfigStorage.isJsapiTicketExpired(mpTag)) {
                    String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";
                    String responseContent = null;
                    if(StringUtils.isBlank(mpTag)) {
                        responseContent = (String)this.execute(new SimpleGetRequestExecutor(), url, null, mpTag);
                    } else {
                        responseContent = (String)this.execute(new SimpleGetRequestExecutor(), url, null);
                    }

                    JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
                    JsonObject tmpJsonObject = tmpJsonElement.getAsJsonObject();
                    String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
                    int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
                    this.wxMpConfigStorage.updateJsapiTicket(jsapiTicket, expiresInSeconds, mpTag);
                }
            }
        }

        return this.wxMpConfigStorage.getJsapiTicket(mpTag);
    }

    public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
        long timestamp = System.currentTimeMillis() / 1000L;
        String noncestr = RandomUtils.getRandomStr();
        String jsapiTicket = this.getJsapiTicket(false);

        try {
            String e = SHA1.genWithAmple(new String[]{"jsapi_ticket=" + jsapiTicket, "noncestr=" + noncestr, "timestamp=" + timestamp, "url=" + url});
            WxJsapiSignature jsapiSignature = new WxJsapiSignature();
            jsapiSignature.setTimestamp(timestamp);
            jsapiSignature.setNoncestr(noncestr);
            jsapiSignature.setUrl(url);
            jsapiSignature.setSignature(e);
            return jsapiSignature;
        } catch (NoSuchAlgorithmException var8) {
            throw new RuntimeException(var8);
        }
    }

    public void customMessageSend(WxMpCustomMessage message) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send";
        this.execute(new SimplePostRequestExecutor(), url, message.toJson());
    }

    public void menuCreate(WxMenu menu) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/create";
        this.execute(new SimplePostRequestExecutor(), url, menu.toJson());
    }

    public void menuDelete() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete";
        this.execute(new SimpleGetRequestExecutor(), url, null);
    }

    public WxMenu menuGet() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/get";

        try {
            String e = (String)this.execute(new SimpleGetRequestExecutor(), url, null);
            return WxMenu.fromJson(e);
        } catch (WxErrorException var3) {
            if(var3.getError().getErrorCode() == '뎳') {
                return null;
            } else {
                throw var3;
            }
        }
    }

    public WxMediaUploadResult mediaUpload(String mediaType, String fileType, InputStream inputStream) throws WxErrorException, IOException {
        return this.mediaUpload(mediaType, FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), fileType));
    }

    public WxMediaUploadResult mediaUpload(String mediaType, File file) throws WxErrorException {
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?type=" + mediaType;
        return (WxMediaUploadResult)this.execute(new MediaUploadRequestExecutor(), url, file);
    }

    public File mediaDownload(String media_id) throws WxErrorException {
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get";
        return (File)this.execute(new MediaDownloadRequestExecutor(), url, "media_id=" + media_id);
    }

    public WxMpMassUploadResult massNewsUpload(WxMpMassNews news) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews";
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, news.toJson());
        return WxMpMassUploadResult.fromJson(responseContent);
    }

    public WxMpMassUploadResult massVideoUpload(WxMpMassVideo video) throws WxErrorException {
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/uploadvideo";
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, video.toJson());
        return WxMpMassUploadResult.fromJson(responseContent);
    }

    public WxMpMassSendResult massGroupMessageSend(WxMpMassGroupMessage message) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall";
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, message.toJson());
        return WxMpMassSendResult.fromJson(responseContent);
    }

    public WxMpMassSendResult massOpenIdsMessageSend(WxMpMassOpenIdsMessage message) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/mass/send";
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, message.toJson());
        return WxMpMassSendResult.fromJson(responseContent);
    }

    public WxMpGroup groupCreate(String name) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/create";
        JsonObject json = new JsonObject();
        JsonObject groupJson = new JsonObject();
        json.add("group", groupJson);
        groupJson.addProperty("name", name);
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, json.toString());
        return WxMpGroup.fromJson(responseContent);
    }

    public List<WxMpGroup> groupGet() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/get";
        String responseContent = (String)this.execute(new SimpleGetRequestExecutor(), url, null);
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return (List)WxMpGsonBuilder.INSTANCE.create().fromJson(tmpJsonElement.getAsJsonObject().get("groups"), (new TypeToken() {
        }).getType());
    }

    public long userGetGroup(String openid) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/getid";
        JsonObject o = new JsonObject();
        o.addProperty("openid", openid);
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, o.toString());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return GsonHelper.getAsLong(tmpJsonElement.getAsJsonObject().get("groupid")).longValue();
    }

    public void groupUpdate(WxMpGroup group) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/update";
        this.execute(new SimplePostRequestExecutor(), url, group.toJson());
    }

    public void userUpdateGroup(String openid, long to_groupid) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/groups/members/update";
        JsonObject json = new JsonObject();
        json.addProperty("openid", openid);
        json.addProperty("to_groupid", Long.valueOf(to_groupid));
        this.execute(new SimplePostRequestExecutor(), url, json.toString());
    }

    public void userUpdateRemark(String openid, String remark) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark";
        JsonObject json = new JsonObject();
        json.addProperty("openid", openid);
        json.addProperty("remark", remark);
        this.execute(new SimplePostRequestExecutor(), url, json.toString());
    }

    public WxMpUser userInfo(String openid, String lang) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        lang = lang == null?"zh_CN":lang;
        String responseContent = (String)this.execute(new SimpleGetRequestExecutor(), url, "openid=" + openid + "&lang=" + lang);
        return WxMpUser.fromJson(responseContent);
    }

    public WxMpUser userInfo(String openid, String lang, String mpTag) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        lang = lang == null?"zh_CN":lang;
        String responseContent = (String)this.execute(new SimpleGetRequestExecutor(), url, "openid=" + openid + "&lang=" + lang, mpTag);
        return WxMpUser.fromJson(responseContent);
    }

    public WxMpUserList userList(String next_openid) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/user/get";
        String responseContent = (String)this.execute(new SimpleGetRequestExecutor(), url, next_openid == null ? null : "next_openid=" + next_openid);
        return WxMpUserList.fromJson(responseContent);
    }

    public WxMpQrCodeTicket qrCodeCreateTmpTicket(int scene_id, Integer expire_seconds) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
        JsonObject json = new JsonObject();
        json.addProperty("action_name", "QR_SCENE");
        if(expire_seconds != null) {
            json.addProperty("expire_seconds", expire_seconds);
        }

        JsonObject actionInfo = new JsonObject();
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_id", Integer.valueOf(scene_id));
        actionInfo.add("scene", scene);
        json.add("action_info", actionInfo);
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, json.toString());
        return WxMpQrCodeTicket.fromJson(responseContent);
    }

    public WxMpQrCodeTicket qrCodeCreateLastTicket(int scene_id) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
        JsonObject json = new JsonObject();
        json.addProperty("action_name", "QR_LIMIT_SCENE");
        JsonObject actionInfo = new JsonObject();
        JsonObject scene = new JsonObject();
        scene.addProperty("scene_id", Integer.valueOf(scene_id));
        actionInfo.add("scene", scene);
        json.add("action_info", actionInfo);
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, json.toString());
        return WxMpQrCodeTicket.fromJson(responseContent);
    }

    public File qrCodePicture(WxMpQrCodeTicket ticket) throws WxErrorException {
        String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
        return (File)this.execute(new QrCodeRequestExecutor(), url, ticket);
    }

    public String shortUrl(String long_url) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/shorturl";
        JsonObject o = new JsonObject();
        o.addProperty("action", "long2short");
        o.addProperty("long_url", long_url);
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, o.toString());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return tmpJsonElement.getAsJsonObject().get("short_url").getAsString();
    }

    public String templateSend(WxMpTemplateMessage templateMessage) throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send";
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, templateMessage.toJson());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return tmpJsonElement.getAsJsonObject().get("msgid").getAsString();
    }

    public WxMpSemanticQueryResult semanticQuery(WxMpSemanticQuery semanticQuery) throws WxErrorException {
        String url = "https://api.weixin.qq.com/semantic/semproxy/search";
        String responseContent = (String)this.execute(new SimplePostRequestExecutor(), url, semanticQuery.toJson());
        return WxMpSemanticQueryResult.fromJson(responseContent);
    }

    public String oauth2buildAuthorizationUrl(String scope, String state) {
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
        url = url + "appid=" + this.wxMpConfigStorage.getAppId();
        url = url + "&redirect_uri=" + URIUtil.encodeURIComponent(this.wxMpConfigStorage.getOauth2redirectUri());
        url = url + "&response_type=code";
        url = url + "&scope=" + scope;
        if(state != null) {
            url = url + "&state=" + state;
        }

        url = url + "#wechat_redirect";
        return url;
    }

    public WxMpOAuth2AccessToken oauth2getAccessToken(String code) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
        url = url + "appid=" + this.wxMpConfigStorage.getAppId();
        url = url + "&secret=" + this.wxMpConfigStorage.getSecret();
        url = url + "&code=" + code;
        url = url + "&grant_type=authorization_code";

        try {
            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
            String responseText = (String)e.execute(this.getHttpclient(), this.httpProxy, url, null);
            return WxMpOAuth2AccessToken.fromJson(responseText);
        } catch (ClientProtocolException var5) {
            throw new RuntimeException(var5);
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    public WxMpOAuth2AccessToken oauth2getAccessToken(String code, String mpTag) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
        url = url + "appid=" + this.wxMpConfigStorage.getAppId(mpTag);
        url = url + "&secret=" + this.wxMpConfigStorage.getSecret(mpTag);
        url = url + "&code=" + code;
        url = url + "&grant_type=authorization_code";

        try {
            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
            String responseText = (String)e.execute(this.getHttpclient(), this.httpProxy, url, null, mpTag);
            return WxMpOAuth2AccessToken.fromJson(responseText);
        } catch (ClientProtocolException var6) {
            throw new RuntimeException(var6);
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }

    public WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";
        url = url + "appid=" + this.wxMpConfigStorage.getAppId();
        url = url + "&grant_type=refresh_token";
        url = url + "&refresh_token=" + refreshToken;

        try {
            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
            String responseText = (String)e.execute(this.getHttpclient(), this.httpProxy, url, null);
            return WxMpOAuth2AccessToken.fromJson(responseText);
        } catch (ClientProtocolException var5) {
            throw new RuntimeException(var5);
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    public WxMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken, String mpTag) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";
        url = url + "appid=" + this.wxMpConfigStorage.getAppId(mpTag);
        url = url + "&grant_type=refresh_token";
        url = url + "&refresh_token=" + refreshToken;

        try {
            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
            String responseText = (String)e.execute(this.getHttpclient(), this.httpProxy, url, null, mpTag);
            return WxMpOAuth2AccessToken.fromJson(responseText);
        } catch (ClientProtocolException var6) {
            throw new RuntimeException(var6);
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }

    public WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken oAuth2AccessToken, String lang) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/userinfo?";
        url = url + "access_token=" + oAuth2AccessToken.getAccessToken();
        url = url + "&openid=" + oAuth2AccessToken.getOpenId();
        if(lang == null) {
            url = url + "&lang=zh_CN";
        } else {
            url = url + "&lang=" + lang;
        }

        try {
            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
            String responseText = (String)e.execute(this.getHttpclient(), this.httpProxy, url, null);
            return WxMpUser.fromJson(responseText);
        } catch (ClientProtocolException var6) {
            throw new RuntimeException(var6);
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
    }

    public WxMpUser oauth2getUserInfo(WxMpOAuth2AccessToken oAuth2AccessToken, String lang, String mpTag) throws WxErrorException {
        String url = "https://api.weixin.qq.com/sns/userinfo?";
        url = url + "access_token=" + oAuth2AccessToken.getAccessToken();
        url = url + "&openid=" + oAuth2AccessToken.getOpenId();
        if(lang == null) {
            url = url + "&lang=zh_CN";
        } else {
            url = url + "&lang=" + lang;
        }

        try {
            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
            String responseText = (String)e.execute(this.getHttpclient(), this.httpProxy, url, null, mpTag);
            return WxMpUser.fromJson(responseText);
        } catch (ClientProtocolException var7) {
            throw new RuntimeException(var7);
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }
    }

    public boolean oauth2validateAccessToken(WxMpOAuth2AccessToken oAuth2AccessToken) {
        String url = "https://api.weixin.qq.com/sns/auth?";
        url = url + "access_token=" + oAuth2AccessToken.getAccessToken();
        url = url + "&openid=" + oAuth2AccessToken.getOpenId();

        try {
            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
            e.execute(this.getHttpclient(), this.httpProxy, url, null);
            return true;
        } catch (ClientProtocolException var4) {
            throw new RuntimeException(var4);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        } catch (WxErrorException var6) {
            return false;
        }
    }

    public boolean oauth2validateAccessToken(WxMpOAuth2AccessToken oAuth2AccessToken, String mpTag) {
        String url = "https://api.weixin.qq.com/sns/auth?";
        url = url + "access_token=" + oAuth2AccessToken.getAccessToken();
        url = url + "&openid=" + oAuth2AccessToken.getOpenId();

        try {
            SimpleGetRequestExecutor e = new SimpleGetRequestExecutor();
            e.execute(this.getHttpclient(), this.httpProxy, url, null, mpTag);
            return true;
        } catch (ClientProtocolException var5) {
            throw new RuntimeException(var5);
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        } catch (WxErrorException var7) {
            return false;
        }
    }

    public String[] getCallbackIP() throws WxErrorException {
        String url = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
        String responseContent = this.get(url, (String) null);
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        JsonArray ipList = tmpJsonElement.getAsJsonObject().get("ip_list").getAsJsonArray();
        String[] ipArray = new String[ipList.size()];

        for(int i = 0; i < ipList.size(); ++i) {
            ipArray[i] = ipList.get(i).getAsString();
        }

        return ipArray;
    }

    public List<WxMpUserSummary> getUserSummary(Date beginDate, Date endDate) throws WxErrorException {
        String url = "https://api.weixin.qq.com/datacube/getusersummary";
        JsonObject param = new JsonObject();
        param.addProperty("begin_date", SIMPLE_DATE_FORMAT.format(beginDate));
        param.addProperty("end_date", SIMPLE_DATE_FORMAT.format(endDate));
        String responseContent = this.post(url, param.toString());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return (List)WxMpGsonBuilder.INSTANCE.create().fromJson(tmpJsonElement.getAsJsonObject().get("list"), (new TypeToken() {
        }).getType());
    }

    public List<WxMpUserCumulate> getUserCumulate(Date beginDate, Date endDate) throws WxErrorException {
        String url = "https://api.weixin.qq.com/datacube/getusercumulate";
        JsonObject param = new JsonObject();
        param.addProperty("begin_date", SIMPLE_DATE_FORMAT.format(beginDate));
        param.addProperty("end_date", SIMPLE_DATE_FORMAT.format(endDate));
        String responseContent = this.post(url, param.toString());
        JsonElement tmpJsonElement = Streams.parse(new JsonReader(new StringReader(responseContent)));
        return (List)WxMpGsonBuilder.INSTANCE.create().fromJson(tmpJsonElement.getAsJsonObject().get("list"), (new TypeToken() {
        }).getType());
    }

    public String get(String url, String queryParam) throws WxErrorException {
        return (String)this.execute(new SimpleGetRequestExecutor(), url, queryParam);
    }

    public String post(String url, String postData) throws WxErrorException {
        return (String)this.execute(new SimplePostRequestExecutor(), url, postData);
    }

    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
        int retryTimes = 0;

        while(true) {
            try {
                return this.executeInternal(executor, uri, data);
            } catch (WxErrorException var10) {
                WxError error = var10.getError();
                if(error.getErrorCode() != -1) {
                    throw var10;
                }

                int sleepMillis = this.retrySleepMillis * (1 << retryTimes);

                try {
                    this.log.debug("微信系统繁忙，{}ms 后重试(第{}次)", Integer.valueOf(sleepMillis), Integer.valueOf(retryTimes + 1));
                    Thread.sleep((long)sleepMillis);
                } catch (InterruptedException var9) {
                    throw new RuntimeException(var9);
                }

                ++retryTimes;
                if(retryTimes >= this.maxRetryTimes) {
                    throw new RuntimeException("微信服务端异常，超出重试次数");
                }
            }
        }
    }

    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data, String mpTag) throws WxErrorException {
        int retryTimes = 0;

        while(true) {
            try {
                return this.executeInternal(executor, uri, data, mpTag);
            } catch (WxErrorException var11) {
                WxError error = var11.getError();
                if(error.getErrorCode() != -1) {
                    throw var11;
                }

                int sleepMillis = this.retrySleepMillis * (1 << retryTimes);

                try {
                    this.log.debug("微信系统繁忙，{}ms 后重试(第{}次)", Integer.valueOf(sleepMillis), Integer.valueOf(retryTimes + 1));
                    Thread.sleep((long)sleepMillis);
                } catch (InterruptedException var10) {
                    throw new RuntimeException(var10);
                }

                ++retryTimes;
                if(retryTimes >= this.maxRetryTimes) {
                    throw new RuntimeException("微信服务端异常，超出重试次数");
                }
            }
        }
    }

    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data, String mpTag) throws WxErrorException {
        String accessToken = this.getAccessToken(false, mpTag);
        String uriWithAccessToken = uri + (uri.indexOf(63) == -1?"?access_token=" + accessToken:"&access_token=" + accessToken);

        try {
            return executor.execute(this.getHttpclient(), this.httpProxy, uriWithAccessToken, data, mpTag);
        } catch (WxErrorException var9) {
            WxError error = var9.getError();
            if(error.getErrorCode() != 'ꐑ' && error.getErrorCode() != '鱁') {
                if(error.getErrorCode() != 0) {
                    throw new WxErrorException(error);
                } else {
                    return null;
                }
            } else {
                if(StringUtils.isNotBlank(mpTag)) {
                    this.wxMpConfigStorage.expireAccessToken(mpTag);
                } else {
                    this.wxMpConfigStorage.expireAccessToken();
                }

                return this.execute(executor, uri, data, mpTag);
            }
        } catch (ClientProtocolException var10) {
            throw new RuntimeException(var10);
        } catch (IOException var11) {
            throw new RuntimeException(var11);
        }
    }

    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
        String accessToken = this.getAccessToken(false);
        String uriWithAccessToken = uri + (uri.indexOf(63) == -1?"?access_token=" + accessToken:"&access_token=" + accessToken);

        try {
            return executor.execute(this.getHttpclient(), this.httpProxy, uriWithAccessToken, data);
        } catch (WxErrorException var8) {
            WxError error = var8.getError();
            if(error.getErrorCode() != 'ꐑ' && error.getErrorCode() != '鱁') {
                if(error.getErrorCode() != 0) {
                    throw new WxErrorException(error);
                } else {
                    return null;
                }
            } else {
                this.wxMpConfigStorage.expireAccessToken();
                return this.execute(executor, uri, data);
            }
        } catch (ClientProtocolException var9) {
            throw new RuntimeException(var9);
        } catch (IOException var10) {
            throw new RuntimeException(var10);
        }
    }

    protected CloseableHttpClient getHttpclient() {
        return this.httpClient;
    }

    public void setWxMpConfigStorage(WxMpConfigStorage wxConfigProvider) {
        this.wxMpConfigStorage = wxConfigProvider;
        String http_proxy_host = this.wxMpConfigStorage.getHttp_proxy_host();
        int http_proxy_port = this.wxMpConfigStorage.getHttp_proxy_port();
        String http_proxy_username = this.wxMpConfigStorage.getHttp_proxy_username();
        String http_proxy_password = this.wxMpConfigStorage.getHttp_proxy_password();
        if(StringUtils.isNotBlank(http_proxy_host)) {
            if(StringUtils.isNotBlank(http_proxy_username)) {
                BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(new AuthScope(http_proxy_host, http_proxy_port), new UsernamePasswordCredentials(http_proxy_username, http_proxy_password));
                this.httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
            } else {
                this.httpClient = HttpClients.createDefault();
            }

            this.httpProxy = new HttpHost(http_proxy_host, http_proxy_port);
        } else {
            this.httpClient = HttpClients.createDefault();
        }

    }

    public void setRetrySleepMillis(int retrySleepMillis) {
        this.retrySleepMillis = retrySleepMillis;
    }

    public void setMaxRetryTimes(int maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }
}


package com.hiden.biz.wechat.mp.api;

import com.hiden.biz.wechat.common.model.response.WxAccessToken;

public interface WxMpConfigStorage {
    String getAccessToken();

    String getAccessToken(String var1);

    boolean isAccessTokenExpired();

    boolean isAccessTokenExpired(String var1);

    void expireAccessToken();

    void expireAccessToken(String var1);

    void updateAccessToken(WxAccessToken var1);

    void updateAccessToken(WxAccessToken var1, String var2);

    void updateAccessToken(String var1, int var2);

    void updateAccessToken(String var1, int var2, String var3);

    String getJsapiTicket();

    String getJsapiTicket(String var1);

    boolean isJsapiTicketExpired();

    boolean isJsapiTicketExpired(String var1);

    void expireJsapiTicket();

    void expireJsapiTicket(String var1);

    void updateJsapiTicket(String var1, int var2);

    void updateJsapiTicket(String var1, int var2, String var3);

    String getAppId();

    String getAppId(String var1);

    void setAppId(String var1, boolean var2);

    String getSecret();

    void setSecret(String var1, boolean var2);

    String getSecret(String var1);

    String getToken();

    void setToken(String var1, boolean var2);

    String getToken(String var1);

    String getAesKey();

    void setAesKey(String var1, boolean var2);

    String getAesKey(String var1);

    long getExpiresTime();

    void setExpiresTime(String var1, boolean var2);

    long getExpiresTime(String var1);

    String getOauth2redirectUri();

    String getOauth2redirectUri(String var1);

    String getHttp_proxy_host();

    String getHttp_proxy_host(String var1);

    int getHttp_proxy_port();

    int getHttp_proxy_port(String var1);

    String getHttp_proxy_username();

    String getHttp_proxy_username(String var1);

    String getHttp_proxy_password();

    String getHttp_proxy_password(String var1);
}

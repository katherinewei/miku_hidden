
package com.hiden.biz.wechat.mp.api;

import com.hiden.biz.wechat.common.model.response.WxAccessToken;

public class WxMpInMemoryConfigStorage implements WxMpConfigStorage {
    protected volatile String appId;
    protected volatile String secret;
    protected volatile String token;
    protected volatile String accessToken;
    protected volatile String aesKey;
    protected volatile long expiresTime;
    protected volatile String oauth2redirectUri;
    protected volatile String http_proxy_host;
    protected volatile int http_proxy_port;
    protected volatile String http_proxy_username;
    protected volatile String http_proxy_password;
    protected volatile String jsapiTicket;
    protected volatile long jsapiTicketExpiresTime;

    public WxMpInMemoryConfigStorage() {
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getAccessToken(String mpTag) {
        return null;
    }

    public boolean isAccessTokenExpired() {
        return System.currentTimeMillis() > this.expiresTime;
    }

    public boolean isAccessTokenExpired(String mpTag) {
        return false;
    }

    public synchronized void updateAccessToken(WxAccessToken accessToken) {
        this.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    }

    public void updateAccessToken(WxAccessToken accessToken, String mpTag) {
    }

    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        this.accessToken = accessToken;
        this.expiresTime = System.currentTimeMillis() + (long)(expiresInSeconds - 200) * 1000L;
    }

    public void updateAccessToken(String accessToken, int expiresIn, String mpTag) {
    }

    public void expireAccessToken() {
        this.expiresTime = 0L;
    }

    public void expireAccessToken(String mpTag) {
    }

    public String getJsapiTicket() {
        return this.jsapiTicket;
    }

    public String getJsapiTicket(String mpTag) {
        return null;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public long getJsapiTicketExpiresTime() {
        return this.jsapiTicketExpiresTime;
    }

    public void setJsapiTicketExpiresTime(long jsapiTicketExpiresTime) {
        this.jsapiTicketExpiresTime = jsapiTicketExpiresTime;
    }

    public boolean isJsapiTicketExpired() {
        return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
    }

    public boolean isJsapiTicketExpired(String mpTag) {
        return false;
    }

    public synchronized void updateJsapiTicket(String jsapiTicket, int expiresInSeconds) {
        this.jsapiTicket = jsapiTicket;
        this.jsapiTicketExpiresTime = System.currentTimeMillis() + (long)(expiresInSeconds - 200) * 1000L;
    }

    public void updateJsapiTicket(String jsapiTicket, int expiresInSeconds, String mpTag) {
    }

    public void expireJsapiTicket() {
        this.jsapiTicketExpiresTime = 0L;
    }

    public void expireJsapiTicket(String mpTag) {
    }

    public String getAppId() {
        return this.appId;
    }

    public String getAppId(String mpTag) {
        return null;
    }

    public void setAppId(String mpTag, boolean multi) {
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String mpTag, boolean multi) {
    }

    public String getSecret(String mpTag) {
        return null;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String mpTag, boolean multi) {
    }

    public String getToken(String mpTag) {
        return null;
    }

    public long getExpiresTime() {
        return this.expiresTime;
    }

    public void setExpiresTime(String mpTag, boolean multi) {
    }

    public long getExpiresTime(String mpTag) {
        return 0L;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return this.aesKey;
    }

    public void setAesKey(String mpTag, boolean multi) {
    }

    public String getAesKey(String mpTag) {
        return null;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getOauth2redirectUri() {
        return this.oauth2redirectUri;
    }

    public String getOauth2redirectUri(String mpTag) {
        return null;
    }

    public void setOauth2redirectUri(String oauth2redirectUri) {
        this.oauth2redirectUri = oauth2redirectUri;
    }

    public String getHttp_proxy_host() {
        return this.http_proxy_host;
    }

    public String getHttp_proxy_host(String mpTag) {
        return null;
    }

    public void setHttp_proxy_host(String http_proxy_host) {
        this.http_proxy_host = http_proxy_host;
    }

    public int getHttp_proxy_port() {
        return this.http_proxy_port;
    }

    public int getHttp_proxy_port(String mpTag) {
        return 0;
    }

    public void setHttp_proxy_port(int http_proxy_port) {
        this.http_proxy_port = http_proxy_port;
    }

    public String getHttp_proxy_username() {
        return this.http_proxy_username;
    }

    public String getHttp_proxy_username(String mpTag) {
        return null;
    }

    public void setHttp_proxy_username(String http_proxy_username) {
        this.http_proxy_username = http_proxy_username;
    }

    public String getHttp_proxy_password() {
        return this.http_proxy_password;
    }

    public String getHttp_proxy_password(String mpTag) {
        return null;
    }

    public void setHttp_proxy_password(String http_proxy_password) {
        this.http_proxy_password = http_proxy_password;
    }

    public String toString() {
        return "WxMpInMemoryConfigStorage{appId=\'" + this.appId + '\'' + ", secret=\'" + this.secret + '\'' + ", token=\'" + this.token + '\'' + ", accessToken=\'" + this.accessToken + '\'' + ", aesKey=\'" + this.aesKey + '\'' + ", expiresTime=" + this.expiresTime + ", http_proxy_host=\'" + this.http_proxy_host + '\'' + ", http_proxy_port=" + this.http_proxy_port + ", http_proxy_username=\'" + this.http_proxy_username + '\'' + ", http_proxy_password=\'" + this.http_proxy_password + '\'' + ", jsapiTicket=\'" + this.jsapiTicket + '\'' + ", jsapiTicketExpiresTime=\'" + this.jsapiTicketExpiresTime + '\'' + '}';
    }
}

package com.hiden.biz.wechat.mp.bean.result;

import com.hiden.biz.wechat.mp.util.json.WxMpGsonBuilder;
import java.io.Serializable;

public class WxMpOAuth2AccessToken implements Serializable {
   private String accessToken;
   private int expiresIn = -1;
   private String refreshToken;
   private String openId;
   private String scope;

   public WxMpOAuth2AccessToken() {
   }

   public String getRefreshToken() {
      return this.refreshToken;
   }

   public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
   }

   public String getOpenId() {
      return this.openId;
   }

   public void setOpenId(String openId) {
      this.openId = openId;
   }

   public String getScope() {
      return this.scope;
   }

   public void setScope(String scope) {
      this.scope = scope;
   }

   public String getAccessToken() {
      return this.accessToken;
   }

   public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
   }

   public int getExpiresIn() {
      return this.expiresIn;
   }

   public void setExpiresIn(int expiresIn) {
      this.expiresIn = expiresIn;
   }

   public static WxMpOAuth2AccessToken fromJson(String json) {
      return (WxMpOAuth2AccessToken)WxMpGsonBuilder.create().fromJson(json, WxMpOAuth2AccessToken.class);
   }

   public String toString() {
      return "WxMpOAuth2AccessToken{accessToken=\'" + this.accessToken + '\'' + ", expiresTime=" + this.expiresIn + ", refreshToken=\'" + this.refreshToken + '\'' + ", openId=\'" + this.openId + '\'' + ", scope=\'" + this.scope + '\'' + '}';
   }
}

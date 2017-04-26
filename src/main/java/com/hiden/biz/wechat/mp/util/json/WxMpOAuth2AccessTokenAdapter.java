package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.result.WxMpOAuth2AccessToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class WxMpOAuth2AccessTokenAdapter implements JsonDeserializer<WxMpOAuth2AccessToken> {
    public WxMpOAuth2AccessTokenAdapter() {
    }

    public WxMpOAuth2AccessToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxMpOAuth2AccessToken accessToken = new WxMpOAuth2AccessToken();
        JsonObject accessTokenJsonObject = json.getAsJsonObject();
        if(accessTokenJsonObject.get("access_token") != null && !accessTokenJsonObject.get("access_token").isJsonNull()) {
            accessToken.setAccessToken(GsonHelper.getAsString(accessTokenJsonObject.get("access_token")));
        }

        if(accessTokenJsonObject.get("expires_in") != null && !accessTokenJsonObject.get("expires_in").isJsonNull()) {
            accessToken.setExpiresIn(GsonHelper.getAsPrimitiveInt(accessTokenJsonObject.get("expires_in")));
        }

        if(accessTokenJsonObject.get("refresh_token") != null && !accessTokenJsonObject.get("refresh_token").isJsonNull()) {
            accessToken.setRefreshToken(GsonHelper.getAsString(accessTokenJsonObject.get("refresh_token")));
        }

        if(accessTokenJsonObject.get("openid") != null && !accessTokenJsonObject.get("openid").isJsonNull()) {
            accessToken.setOpenId(GsonHelper.getAsString(accessTokenJsonObject.get("openid")));
        }

        if(accessTokenJsonObject.get("scope") != null && !accessTokenJsonObject.get("scope").isJsonNull()) {
            accessToken.setScope(GsonHelper.getAsString(accessTokenJsonObject.get("scope")));
        }

        return accessToken;
    }
}

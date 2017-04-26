package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.result.WxMpUser;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class WxMpUserGsonAdapter implements JsonDeserializer<WxMpUser> {
    public WxMpUserGsonAdapter() {
    }

    public WxMpUser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject o = json.getAsJsonObject();
        WxMpUser wxMpUser = new WxMpUser();
        wxMpUser.setSubscribe(Boolean.valueOf(!(new Integer(0)).equals(GsonHelper.getInteger(o, "subscribe"))));
        wxMpUser.setCity(GsonHelper.getString(o, "city"));
        wxMpUser.setCountry(GsonHelper.getString(o, "country"));
        wxMpUser.setHeadImgUrl(GsonHelper.getString(o, "headimgurl"));
        wxMpUser.setLanguage(GsonHelper.getString(o, "language"));
        wxMpUser.setNickname(GsonHelper.getString(o, "nickname"));
        wxMpUser.setOpenId(GsonHelper.getString(o, "openid"));
        wxMpUser.setProvince(GsonHelper.getString(o, "province"));
        wxMpUser.setSubscribeTime(GsonHelper.getLong(o, "subscribe_time"));
        wxMpUser.setUnionId(GsonHelper.getString(o, "unionid"));
        Integer sexId = GsonHelper.getInteger(o, "sex");
        wxMpUser.setSexId(sexId);
        if((new Integer(1)).equals(sexId)) {
            wxMpUser.setSex("男");
        } else if((new Integer(2)).equals(sexId)) {
            wxMpUser.setSex("女");
        } else {
            wxMpUser.setSex("未知");
        }

        return wxMpUser;
    }
}

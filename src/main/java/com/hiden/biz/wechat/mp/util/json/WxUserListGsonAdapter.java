package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.result.WxMpUserList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class WxUserListGsonAdapter implements JsonDeserializer<WxMpUserList> {
    public WxUserListGsonAdapter() {
    }

    public WxMpUserList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject o = json.getAsJsonObject();
        WxMpUserList wxMpUserList = new WxMpUserList();
        wxMpUserList.setTotal(GsonHelper.getInteger(o, "total").intValue());
        wxMpUserList.setCount(GsonHelper.getInteger(o, "count").intValue());
        wxMpUserList.setNextOpenId(GsonHelper.getString(o, "next_openid"));
        if(!o.get("data").isJsonNull() && !o.get("data").getAsJsonObject().get("openid").isJsonNull()) {
            JsonArray data = o.get("data").getAsJsonObject().get("openid").getAsJsonArray();

            for(int i = 0; i < data.size(); ++i) {
                wxMpUserList.getOpenIds().add(GsonHelper.getAsString(data.get(i)));
            }
        }

        return wxMpUserList;
    }
}

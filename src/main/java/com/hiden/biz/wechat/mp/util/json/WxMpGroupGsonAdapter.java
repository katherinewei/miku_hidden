package com.hiden.biz.wechat.mp.util.json;
import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.WxMpGroup;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class WxMpGroupGsonAdapter implements JsonSerializer<WxMpGroup>, JsonDeserializer<WxMpGroup> {
    public WxMpGroupGsonAdapter() {
    }

    public JsonElement serialize(WxMpGroup group, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        JsonObject groupJson = new JsonObject();
        groupJson.addProperty("name", group.getName());
        groupJson.addProperty("id", Long.valueOf(group.getId()));
        groupJson.addProperty("count", Long.valueOf(group.getCount()));
        json.add("group", groupJson);
        return json;
    }

    public WxMpGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxMpGroup group = new WxMpGroup();
        JsonObject groupJson = json.getAsJsonObject();
        if(json.getAsJsonObject().get("group") != null) {
            groupJson = json.getAsJsonObject().get("group").getAsJsonObject();
        }

        if(groupJson.get("name") != null && !groupJson.get("name").isJsonNull()) {
            group.setName(GsonHelper.getAsString(groupJson.get("name")));
        }

        if(groupJson.get("id") != null && !groupJson.get("id").isJsonNull()) {
            group.setId(GsonHelper.getAsPrimitiveLong(groupJson.get("id")));
        }

        if(groupJson.get("count") != null && !groupJson.get("count").isJsonNull()) {
            group.setCount(GsonHelper.getAsPrimitiveLong(groupJson.get("count")));
        }

        return group;
    }
}

package com.hiden.biz.wechat.mp.util.json;
import com.hiden.biz.wechat.mp.bean.WxMpTemplateData;
import com.hiden.biz.wechat.mp.bean.WxMpTemplateMessage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Iterator;

public class WxMpTemplateMessageGsonAdapter implements JsonSerializer<WxMpTemplateMessage> {
    public WxMpTemplateMessageGsonAdapter() {
    }

    public JsonElement serialize(WxMpTemplateMessage message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("touser", message.getToUser());
        messageJson.addProperty("template_id", message.getTemplateId());
        if(message.getUrl() != null) {
            messageJson.addProperty("url", message.getUrl());
        }

        if(message.getTopColor() != null) {
            messageJson.addProperty("topcolor", message.getTopColor());
        }

        JsonObject datas = new JsonObject();
        messageJson.add("data", datas);

        WxMpTemplateData data;
        JsonObject dataJson;
        for(Iterator i$ = message.getDatas().iterator(); i$.hasNext(); datas.add(data.getName(), dataJson)) {
            data = (WxMpTemplateData)i$.next();
            dataJson = new JsonObject();
            dataJson.addProperty("value", data.getValue());
            if(data.getColor() != null) {
                dataJson.addProperty("color", data.getColor());
            }
        }

        return messageJson;
    }
}

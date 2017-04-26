package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.mp.bean.WxMpMassOpenIdsMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Iterator;

public class WxMpMassOpenIdsMessageGsonAdapter implements JsonSerializer<WxMpMassOpenIdsMessage> {
    public WxMpMassOpenIdsMessageGsonAdapter() {
    }

    public JsonElement serialize(WxMpMassOpenIdsMessage message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject messageJson = new JsonObject();
        JsonArray toUsers = new JsonArray();
        Iterator sub = message.getToUsers().iterator();

        while(sub.hasNext()) {
            String openId = (String)sub.next();
            toUsers.add(new JsonPrimitive(openId));
        }

        messageJson.add("touser", toUsers);
        JsonObject sub1;
        if("mpnews".equals(message.getMsgType())) {
            sub1 = new JsonObject();
            sub1.addProperty("media_id", message.getMediaId());
            messageJson.add("mpnews", sub1);
        }

        if("text".equals(message.getMsgType())) {
            sub1 = new JsonObject();
            sub1.addProperty("content", message.getContent());
            messageJson.add("text", sub1);
        }

        if("voice".equals(message.getMsgType())) {
            sub1 = new JsonObject();
            sub1.addProperty("media_id", message.getMediaId());
            messageJson.add("voice", sub1);
        }

        if("image".equals(message.getMsgType())) {
            sub1 = new JsonObject();
            sub1.addProperty("media_id", message.getMediaId());
            messageJson.add("image", sub1);
        }

        if("mpvideo".equals(message.getMsgType())) {
            sub1 = new JsonObject();
            sub1.addProperty("media_id", message.getMediaId());
            messageJson.add("mpvideo", sub1);
        }

        messageJson.addProperty("msgtype", message.getMsgType());
        return messageJson;
    }
}

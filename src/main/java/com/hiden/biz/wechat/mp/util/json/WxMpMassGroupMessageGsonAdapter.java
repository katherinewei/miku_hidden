package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.mp.bean.WxMpMassGroupMessage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class WxMpMassGroupMessageGsonAdapter implements JsonSerializer<WxMpMassGroupMessage> {
    public WxMpMassGroupMessageGsonAdapter() {
    }

    public JsonElement serialize(WxMpMassGroupMessage message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject messageJson = new JsonObject();
        JsonObject filter = new JsonObject();
        if(null == message.getGroupId()) {
            filter.addProperty("is_to_all", Boolean.valueOf(true));
        } else {
            filter.addProperty("is_to_all", Boolean.valueOf(false));
            filter.addProperty("group_id", message.getGroupId());
        }

        messageJson.add("filter", filter);
        JsonObject sub;
        if("mpnews".equals(message.getMsgtype())) {
            sub = new JsonObject();
            sub.addProperty("media_id", message.getMediaId());
            messageJson.add("mpnews", sub);
        }

        if("text".equals(message.getMsgtype())) {
            sub = new JsonObject();
            sub.addProperty("content", message.getContent());
            messageJson.add("text", sub);
        }

        if("voice".equals(message.getMsgtype())) {
            sub = new JsonObject();
            sub.addProperty("media_id", message.getMediaId());
            messageJson.add("voice", sub);
        }

        if("image".equals(message.getMsgtype())) {
            sub = new JsonObject();
            sub.addProperty("media_id", message.getMediaId());
            messageJson.add("image", sub);
        }

        if("mpvideo".equals(message.getMsgtype())) {
            sub = new JsonObject();
            sub.addProperty("media_id", message.getMediaId());
            messageJson.add("mpvideo", sub);
        }

        messageJson.addProperty("msgtype", message.getMsgtype());
        return messageJson;
    }
}

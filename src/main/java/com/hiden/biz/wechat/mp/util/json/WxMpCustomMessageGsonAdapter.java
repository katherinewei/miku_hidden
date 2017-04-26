package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage;
import com.hiden.biz.wechat.mp.bean.WxMpCustomMessage.WxArticle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Iterator;

public class WxMpCustomMessageGsonAdapter implements JsonSerializer<WxMpCustomMessage> {
    public WxMpCustomMessageGsonAdapter() {
    }

    public JsonElement serialize(WxMpCustomMessage message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject messageJson = new JsonObject();
        messageJson.addProperty("touser", message.getToUser());
        messageJson.addProperty("msgtype", message.getMsgType());
        JsonObject newsJsonObject;
        if("text".equals(message.getMsgType())) {
            newsJsonObject = new JsonObject();
            newsJsonObject.addProperty("content", message.getContent());
            messageJson.add("text", newsJsonObject);
        }

        if("image".equals(message.getMsgType())) {
            newsJsonObject = new JsonObject();
            newsJsonObject.addProperty("media_id", message.getMediaId());
            messageJson.add("image", newsJsonObject);
        }

        if("voice".equals(message.getMsgType())) {
            newsJsonObject = new JsonObject();
            newsJsonObject.addProperty("media_id", message.getMediaId());
            messageJson.add("voice", newsJsonObject);
        }

        if("video".equals(message.getMsgType())) {
            newsJsonObject = new JsonObject();
            newsJsonObject.addProperty("media_id", message.getMediaId());
            newsJsonObject.addProperty("thumb_media_id", message.getThumbMediaId());
            newsJsonObject.addProperty("title", message.getTitle());
            newsJsonObject.addProperty("description", message.getDescription());
            messageJson.add("video", newsJsonObject);
        }

        if("music".equals(message.getMsgType())) {
            newsJsonObject = new JsonObject();
            newsJsonObject.addProperty("title", message.getTitle());
            newsJsonObject.addProperty("description", message.getDescription());
            newsJsonObject.addProperty("thumb_media_id", message.getThumbMediaId());
            newsJsonObject.addProperty("musicurl", message.getMusicUrl());
            newsJsonObject.addProperty("hqmusicurl", message.getHqMusicUrl());
            messageJson.add("music", newsJsonObject);
        }

        if("news".equals(message.getMsgType())) {
            newsJsonObject = new JsonObject();
            JsonArray articleJsonArray = new JsonArray();
            Iterator i$ = message.getArticles().iterator();

            while(i$.hasNext()) {
                WxArticle article = (WxArticle)i$.next();
                JsonObject articleJson = new JsonObject();
                articleJson.addProperty("title", article.getTitle());
                articleJson.addProperty("description", article.getDescription());
                articleJson.addProperty("url", article.getUrl());
                articleJson.addProperty("picurl", article.getPicUrl());
                articleJsonArray.add(articleJson);
            }

            newsJsonObject.add("articles", articleJsonArray);
            messageJson.add("news", newsJsonObject);
        }

        return messageJson;
    }
}

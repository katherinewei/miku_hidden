package com.hiden.biz.wechat.mp.util.json;
import com.hiden.biz.wechat.mp.bean.WxMpMassNews;
import com.hiden.biz.wechat.mp.bean.WxMpMassNews.WxMpMassNewsArticle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Iterator;

public class WxMpMassNewsGsonAdapter implements JsonSerializer<WxMpMassNews> {
    public WxMpMassNewsGsonAdapter() {
    }

    public JsonElement serialize(WxMpMassNews message, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject newsJson = new JsonObject();
        JsonArray articleJsonArray = new JsonArray();
        Iterator i$ = message.getArticles().iterator();

        while(i$.hasNext()) {
            WxMpMassNewsArticle article = (WxMpMassNewsArticle)i$.next();
            JsonObject articleJson = new JsonObject();
            articleJson.addProperty("thumb_media_id", article.getThumbMediaId());
            articleJson.addProperty("title", article.getTitle());
            articleJson.addProperty("content", article.getContent());
            if(null != article.getAuthor()) {
                articleJson.addProperty("author", article.getAuthor());
            }

            if(null != article.getContentSourceUrl()) {
                articleJson.addProperty("content_source_url", article.getContentSourceUrl());
            }

            if(null != article.getDigest()) {
                articleJson.addProperty("digest", article.getDigest());
            }

            articleJson.addProperty("show_cover_pic", article.isShowCoverPic()?"1":"0");
            articleJsonArray.add(articleJson);
        }

        newsJson.add("articles", articleJsonArray);
        return newsJson;
    }
}

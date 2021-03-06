
package com.hiden.biz.wechat.common.util.json;

import com.google.gson.*;
import com.hiden.biz.wechat.common.model.response.WxMediaUploadResult;

import java.lang.reflect.Type;

public class WxMediaUploadResultAdapter implements JsonDeserializer<WxMediaUploadResult> {
    public WxMediaUploadResultAdapter() {
    }

    public WxMediaUploadResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxMediaUploadResult uploadResult = new WxMediaUploadResult();
        JsonObject uploadResultJsonObject = json.getAsJsonObject();
        if(uploadResultJsonObject.get("type") != null && !uploadResultJsonObject.get("type").isJsonNull()) {
            uploadResult.setType(GsonHelper.getAsString(uploadResultJsonObject.get("type")));
        }

        if(uploadResultJsonObject.get("media_id") != null && !uploadResultJsonObject.get("media_id").isJsonNull()) {
            uploadResult.setMediaId(GsonHelper.getAsString(uploadResultJsonObject.get("media_id")));
        }

        if(uploadResultJsonObject.get("thumb_media_id") != null && !uploadResultJsonObject.get("thumb_media_id").isJsonNull()) {
            uploadResult.setThumbMediaId(GsonHelper.getAsString(uploadResultJsonObject.get("thumb_media_id")));
        }

        if(uploadResultJsonObject.get("created_at") != null && !uploadResultJsonObject.get("created_at").isJsonNull()) {
            uploadResult.setCreatedAt(GsonHelper.getAsPrimitiveLong(uploadResultJsonObject.get("created_at")));
        }

        return uploadResult;
    }
}

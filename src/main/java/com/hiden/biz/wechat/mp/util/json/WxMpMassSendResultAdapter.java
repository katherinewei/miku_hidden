package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.result.WxMpMassSendResult;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class WxMpMassSendResultAdapter implements JsonDeserializer<WxMpMassSendResult> {
    public WxMpMassSendResultAdapter() {
    }

    public WxMpMassSendResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxMpMassSendResult sendResult = new WxMpMassSendResult();
        JsonObject sendResultJsonObject = json.getAsJsonObject();
        if(sendResultJsonObject.get("errcode") != null && !sendResultJsonObject.get("errcode").isJsonNull()) {
            sendResult.setErrorCode(GsonHelper.getAsString(sendResultJsonObject.get("errcode")));
        }

        if(sendResultJsonObject.get("errmsg") != null && !sendResultJsonObject.get("errmsg").isJsonNull()) {
            sendResult.setErrorMsg(GsonHelper.getAsString(sendResultJsonObject.get("errmsg")));
        }

        if(sendResultJsonObject.get("msg_id") != null && !sendResultJsonObject.get("msg_id").isJsonNull()) {
            sendResult.setMsgId(GsonHelper.getAsString(sendResultJsonObject.get("msg_id")));
        }

        return sendResult;
    }
}

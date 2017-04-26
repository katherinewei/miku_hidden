package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.result.WxMpUserSummary;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WxMpUserSummaryGsonAdapter implements JsonDeserializer<WxMpUserSummary> {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public WxMpUserSummaryGsonAdapter() {
    }

    public WxMpUserSummary deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxMpUserSummary summary = new WxMpUserSummary();
        JsonObject summaryJsonObject = json.getAsJsonObject();

        try {
            String e = GsonHelper.getString(summaryJsonObject, "ref_date");
            if (e != null) {
                summary.setRefDate(SIMPLE_DATE_FORMAT.parse(e));
            }

            summary.setUserSource(GsonHelper.getInteger(summaryJsonObject, "user_source"));
            summary.setNewUser(GsonHelper.getInteger(summaryJsonObject, "new_user"));
            summary.setCancelUser(GsonHelper.getInteger(summaryJsonObject, "cancel_user"));
            return summary;
        } catch (ParseException var7) {
            throw new JsonParseException(var7);
        }
    }
}

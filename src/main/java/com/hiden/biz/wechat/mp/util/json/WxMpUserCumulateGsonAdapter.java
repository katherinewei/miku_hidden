package com.hiden.biz.wechat.mp.util.json;
import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.result.WxMpUserCumulate;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class WxMpUserCumulateGsonAdapter implements JsonDeserializer<WxMpUserCumulate> {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public WxMpUserCumulateGsonAdapter() {
    }

    public WxMpUserCumulate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxMpUserCumulate cumulate = new WxMpUserCumulate();
        JsonObject summaryJsonObject = json.getAsJsonObject();

        try {
            String e = GsonHelper.getString(summaryJsonObject, "ref_date");
            if(e != null) {
                cumulate.setRefDate(SIMPLE_DATE_FORMAT.parse(e));
            }

            cumulate.setCumulateUser(GsonHelper.getInteger(summaryJsonObject, "cumulate_user"));
            return cumulate;
        } catch (ParseException var7) {
            throw new JsonParseException(var7);
        }
    }
}

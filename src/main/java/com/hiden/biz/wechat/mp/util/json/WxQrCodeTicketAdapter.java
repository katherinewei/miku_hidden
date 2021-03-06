package com.hiden.biz.wechat.mp.util.json;

import com.hiden.biz.wechat.common.util.json.GsonHelper;
import com.hiden.biz.wechat.mp.bean.result.WxMpQrCodeTicket;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class WxQrCodeTicketAdapter implements JsonDeserializer<WxMpQrCodeTicket> {
    public WxQrCodeTicketAdapter() {
    }

    public WxMpQrCodeTicket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxMpQrCodeTicket ticket = new WxMpQrCodeTicket();
        JsonObject ticketJsonObject = json.getAsJsonObject();
        if(ticketJsonObject.get("ticket") != null && !ticketJsonObject.get("ticket").isJsonNull()) {
            ticket.setTicket(GsonHelper.getAsString(ticketJsonObject.get("ticket")));
        }

        if(ticketJsonObject.get("expire_seconds") != null && !ticketJsonObject.get("expire_seconds").isJsonNull()) {
            ticket.setExpire_seconds(GsonHelper.getAsPrimitiveInt(ticketJsonObject.get("expire_seconds")));
        }

        if(ticketJsonObject.get("url") != null && !ticketJsonObject.get("url").isJsonNull()) {
            ticket.setUrl(GsonHelper.getAsString(ticketJsonObject.get("url")));
        }

        return ticket;
    }
}

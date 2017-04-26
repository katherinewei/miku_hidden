package com.hiden.biz.wechat.common.util.json;

import com.google.gson.*;
import com.hiden.biz.wechat.common.model.response.WxMenu;
import com.hiden.biz.wechat.common.model.response.WxMenu.WxMenuButton;

import java.lang.reflect.Type;
import java.util.Iterator;

public class WxMenuGsonAdapter implements JsonSerializer<WxMenu>, JsonDeserializer<WxMenu> {
    public WxMenuGsonAdapter() {
    }

    public JsonElement serialize(WxMenu menu, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        JsonArray buttonArray = new JsonArray();
        Iterator i$ = menu.getButtons().iterator();

        while(i$.hasNext()) {
            WxMenuButton button = (WxMenuButton)i$.next();
            JsonObject buttonJson = this.convertToJson(button);
            buttonArray.add(buttonJson);
        }

        json.add("button", buttonArray);
        return json;
    }

    protected JsonObject convertToJson(WxMenuButton button) {
        JsonObject buttonJson = new JsonObject();
        buttonJson.addProperty("type", button.getType());
        buttonJson.addProperty("name", button.getName());
        buttonJson.addProperty("key", button.getKey());
        buttonJson.addProperty("url", button.getUrl());
        if(button.getSubButtons() != null && button.getSubButtons().size() > 0) {
            JsonArray buttonArray = new JsonArray();
            Iterator i$ = button.getSubButtons().iterator();

            while(i$.hasNext()) {
                WxMenuButton sub_button = (WxMenuButton)i$.next();
                buttonArray.add(this.convertToJson(sub_button));
            }

            buttonJson.add("sub_button", buttonArray);
        }

        return buttonJson;
    }

    public WxMenu deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        WxMenu menu = new WxMenu();
        JsonObject menuJson = json.getAsJsonObject().get("menu").getAsJsonObject();
        JsonArray buttonsJson = menuJson.get("button").getAsJsonArray();

        for(int i = 0; i < buttonsJson.size(); ++i) {
            JsonObject buttonJson = buttonsJson.get(i).getAsJsonObject();
            WxMenuButton button = this.convertFromJson(buttonJson);
            menu.getButtons().add(button);
            if(buttonJson.get("sub_button") != null && !buttonJson.get("sub_button").isJsonNull()) {
                JsonArray sub_buttonsJson = buttonJson.get("sub_button").getAsJsonArray();

                for(int j = 0; j < sub_buttonsJson.size(); ++j) {
                    JsonObject sub_buttonJson = sub_buttonsJson.get(j).getAsJsonObject();
                    button.getSubButtons().add(this.convertFromJson(sub_buttonJson));
                }
            }
        }

        return menu;
    }

    protected WxMenuButton convertFromJson(JsonObject json) {
        WxMenuButton button = new WxMenuButton();
        button.setName(GsonHelper.getString(json, "name"));
        button.setKey(GsonHelper.getString(json, "key"));
        button.setUrl(GsonHelper.getString(json, "url"));
        button.setType(GsonHelper.getString(json, "type"));
        return button;
    }
}

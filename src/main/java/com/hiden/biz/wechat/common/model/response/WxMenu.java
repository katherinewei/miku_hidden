package com.hiden.biz.wechat.common.model.response;

import com.hiden.biz.wechat.common.util.json.WxGsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMenu
        implements Serializable {
    private List<WxMenuButton> buttons;

    public WxMenu() {
/* 18 */
        this.buttons = new ArrayList();
    }

    public List<WxMenuButton> getButtons() {
        return this.buttons;
    }

    public void setButtons(List<WxMenuButton> buttons) {
        this.buttons = buttons;
    }

    public String toJson() {
        return WxGsonBuilder.create().toJson(this);
    }

    public static WxMenu fromJson(String json) {
        return (WxMenu) WxGsonBuilder.create().fromJson(json, WxMenu.class);
    }

    public static WxMenu fromJson(InputStream is) {
        return (WxMenu) WxGsonBuilder.create().fromJson(new InputStreamReader(is), WxMenu.class);
    }


    public static class WxMenuButton {
        private String type;
        private String name;
        private String key;
        private String url;
        private List<WxMenuButton> subButtons = new ArrayList();

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<WxMenuButton> getSubButtons() {
            return this.subButtons;
        }

        public void setSubButtons(List<WxMenuButton> subButtons) {
            this.subButtons = subButtons;
        }
    }
}
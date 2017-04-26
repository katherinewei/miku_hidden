
package com.hiden.biz.wechat.common.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hiden.biz.wechat.common.model.response.WxAccessToken;
import com.hiden.biz.wechat.common.model.response.WxMediaUploadResult;
import com.hiden.biz.wechat.common.model.response.WxMenu;
import com.hiden.biz.wechat.common.model.response.error.WxError;

public class WxGsonBuilder {
    public static final GsonBuilder INSTANCE = new GsonBuilder();

    public WxGsonBuilder() {
    }

    public static Gson create() {
        return INSTANCE.create();
    }

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.registerTypeAdapter(WxAccessToken.class, new WxAccessTokenAdapter());
        INSTANCE.registerTypeAdapter(WxError.class, new WxErrorAdapter());
        INSTANCE.registerTypeAdapter(WxMenu.class, new WxMenuGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMediaUploadResult.class, new WxMediaUploadResultAdapter());
    }
}

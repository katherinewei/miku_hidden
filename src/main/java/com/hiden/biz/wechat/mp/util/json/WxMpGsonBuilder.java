package com.hiden.biz.wechat.mp.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hiden.biz.wechat.mp.bean.*;
import com.hiden.biz.wechat.mp.bean.result.*;

public class WxMpGsonBuilder {
    public static final GsonBuilder INSTANCE = new GsonBuilder();

    public WxMpGsonBuilder() {
    }

    public static Gson create() {
        return INSTANCE.create();
    }

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.registerTypeAdapter(WxMpCustomMessage.class, new WxMpCustomMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpMassNews.class, new WxMpMassNewsGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpMassGroupMessage.class, new WxMpMassGroupMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpMassOpenIdsMessage.class, new WxMpMassOpenIdsMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpGroup.class, new WxMpGroupGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpUser.class, new WxMpUserGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpUserList.class, new WxUserListGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpMassVideo.class, new WxMpMassVideoAdapter());
        INSTANCE.registerTypeAdapter(WxMpMassSendResult.class, new WxMpMassSendResultAdapter());
        INSTANCE.registerTypeAdapter(WxMpMassUploadResult.class, new WxMpMassUploadResultAdapter());
        INSTANCE.registerTypeAdapter(WxMpQrCodeTicket.class, new WxQrCodeTicketAdapter());
        INSTANCE.registerTypeAdapter(WxMpTemplateMessage.class, new WxMpTemplateMessageGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpSemanticQueryResult.class, new WxMpSemanticQueryResultAdapter());
        INSTANCE.registerTypeAdapter(WxMpOAuth2AccessToken.class, new WxMpOAuth2AccessTokenAdapter());
        INSTANCE.registerTypeAdapter(WxMpUserSummary.class, new WxMpUserSummaryGsonAdapter());
        INSTANCE.registerTypeAdapter(WxMpUserCumulate.class, new WxMpUserCumulateGsonAdapter());
    }
}

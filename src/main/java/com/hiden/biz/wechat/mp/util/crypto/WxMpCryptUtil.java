package com.hiden.biz.wechat.mp.util.crypto;

import com.hiden.biz.wechat.common.util.crypto.WxCryptUtil;
import com.hiden.biz.wechat.mp.api.WxMpConfigStorage;
import org.apache.commons.codec.binary.Base64;

public class WxMpCryptUtil extends WxCryptUtil {
    public WxMpCryptUtil(WxMpConfigStorage wxMpConfigStorage) {
        String encodingAesKey = wxMpConfigStorage.getAesKey();
        String token = wxMpConfigStorage.getToken();
        String appId = wxMpConfigStorage.getAppId();
        this.token = token;
        this.appidOrCorpid = appId;
        this.aesKey = Base64.decodeBase64(encodingAesKey + "=");
    }

    public WxMpCryptUtil(WxMpConfigStorage wxMpConfigStorage, String mpTag) {
        String encodingAesKey = wxMpConfigStorage.getAesKey(mpTag);
        String token = wxMpConfigStorage.getToken(mpTag);
        String appId = wxMpConfigStorage.getAppId(mpTag);
        this.token = token;
        this.appidOrCorpid = appId;
        this.aesKey = Base64.decodeBase64(encodingAesKey + "=");
    }
}

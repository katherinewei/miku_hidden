package com.hiden.biz.wechat.common.util;

import java.util.HashMap;
import java.util.Map;

public class WxConsts {
    public static final String XML_MSG_TEXT = "text";
    public static final String XML_MSG_IMAGE = "image";
    public static final String XML_MSG_VOICE = "voice";
    public static final String XML_MSG_VIDEO = "video";
    public static final String XML_MSG_NEWS = "news";
    public static final String XML_MSG_MUSIC = "music";
    public static final String XML_MSG_LOCATION = "location";
    public static final String XML_MSG_LINK = "link";
    public static final String XML_MSG_EVENT = "event";
    public static final String XML_TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";
    public static final String CUSTOM_MSG_TEXT = "text";
    public static final String CUSTOM_MSG_IMAGE = "image";
    public static final String CUSTOM_MSG_VOICE = "voice";
    public static final String CUSTOM_MSG_VIDEO = "video";
    public static final String CUSTOM_MSG_MUSIC = "music";
    public static final String CUSTOM_MSG_NEWS = "news";
    public static final String CUSTOM_MSG_FILE = "file";
    public static final String MASS_MSG_NEWS = "mpnews";
    public static final String MASS_MSG_TEXT = "text";
    public static final String MASS_MSG_VOICE = "voice";
    public static final String MASS_MSG_IMAGE = "image";
    public static final String MASS_MSG_VIDEO = "mpvideo";
    public static final String MASS_ST_SUCCESS = "send success";
    public static final String MASS_ST_FAIL = "send fail";
    public static final String MASS_ST_涉嫌广告 = "err(10001)";
    public static final String MASS_ST_涉嫌政治 = "err(20001)";
    public static final String MASS_ST_涉嫌社会 = "err(20004)";
    public static final String MASS_ST_涉嫌色情 = "err(20002)";
    public static final String MASS_ST_涉嫌违法犯罪 = "err(20006)";
    public static final String MASS_ST_涉嫌欺诈 = "err(20008)";
    public static final String MASS_ST_涉嫌版权 = "err(20013)";
    public static final String MASS_ST_涉嫌互推_互相宣传 = "err(22000)";
    public static final String MASS_ST_涉嫌其他 = "err(21000)";
    public static final Map<String, String> MASS_ST_2_DESC = new HashMap();
    public static final String EVT_SUBSCRIBE = "subscribe";
    public static final String EVT_UNSUBSCRIBE = "unsubscribe";
    public static final String EVT_SCAN = "SCAN";
    public static final String EVT_LOCATION = "LOCATION";
    public static final String EVT_CLICK = "CLICK";
    public static final String EVT_VIEW = "VIEW";
    public static final String EVT_MASS_SEND_JOB_FINISH = "MASSSENDJOBFINISH";
    public static final String EVT_SCANCODE_PUSH = "scancode_push";
    public static final String EVT_SCANCODE_WAITMSG = "scancode_waitmsg";
    public static final String EVT_PIC_SYSPHOTO = "pic_sysphoto";
    public static final String EVT_PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";
    public static final String EVT_PIC_WEIXIN = "pic_weixin";
    public static final String EVT_LOCATION_SELECT = "location_select";
    public static final String EVT_TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";
    public static final String EVT_ENTER_AGENT = "enter_agent";
    public static final String MEDIA_IMAGE = "image";
    public static final String MEDIA_VOICE = "voice";
    public static final String MEDIA_VIDEO = "video";
    public static final String MEDIA_THUMB = "thumb";
    public static final String MEDIA_FILE = "file";
    public static final String FILE_JPG = "jpeg";
    public static final String FILE_MP3 = "mp3";
    public static final String FILE_ARM = "arm";
    public static final String FILE_MP4 = "mp4";
    public static final String BUTTON_CLICK = "click";
    public static final String BUTTON_VIEW = "view";
    public static final String BUTTON_SCANCODE_PUSH = "scancode_push";
    public static final String BUTTON_SCANCODE_WAITMSG = "scancode_waitmsg";
    public static final String PIC_SYSPHOTO = "pic_sysphoto";
    public static final String PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";
    public static final String PIC_WEIXIN = "pic_weixin";
    public static final String LOCATION_SELECT = "location_select";
    public static final String OAUTH2_SCOPE_BASE = "snsapi_base";
    public static final String OAUTH2_SCOPE_USER_INFO = "snsapi_userinfo";

    public WxConsts() {
    }

    static {
        MASS_ST_2_DESC.put("send success", "发送成功");
        MASS_ST_2_DESC.put("send fail", "发送失败");
        MASS_ST_2_DESC.put("err(10001)", "涉嫌广告");
        MASS_ST_2_DESC.put("err(20001)", "涉嫌政治");
        MASS_ST_2_DESC.put("err(20004)", "涉嫌社会");
        MASS_ST_2_DESC.put("err(20002)", "涉嫌色情");
        MASS_ST_2_DESC.put("err(20006)", "涉嫌违法犯罪");
        MASS_ST_2_DESC.put("err(20008)", "涉嫌欺诈");
        MASS_ST_2_DESC.put("err(20013)", "涉嫌版权");
        MASS_ST_2_DESC.put("err(22000)", "涉嫌互推_互相宣传");
        MASS_ST_2_DESC.put("err(21000)", "涉嫌其他");
    }
}


package com.hiden.biz.wechat.common.util.http;

import com.hiden.biz.wechat.common.util.StringUtils;
import java.io.UnsupportedEncodingException;

public class URIUtil {
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*\'()";

    public URIUtil() {
    }

    public static String encodeURIComponent(String input) {
        if(StringUtils.isEmpty(input)) {
            return input;
        } else {
            int l = input.length();
            StringBuilder o = new StringBuilder(l * 3);

            try {
                for(int e = 0; e < l; ++e) {
                    String e1 = input.substring(e, e + 1);
                    if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*\'()".indexOf(e1) == -1) {
                        byte[] b = e1.getBytes("utf-8");
                        o.append(getHex(b));
                    } else {
                        o.append(e1);
                    }
                }

                return o.toString();
            } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
                return input;
            }
        }
    }

    private static String getHex(byte[] buf) {
        StringBuilder o = new StringBuilder(buf.length * 3);

        for(int i = 0; i < buf.length; ++i) {
            int n = buf[i] & 255;
            o.append("%");
            if(n < 16) {
                o.append("0");
            }

            o.append(Long.toString((long)n, 16).toUpperCase());
        }

        return o.toString();
    }
}

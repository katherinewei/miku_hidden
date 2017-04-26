
package com.hiden.biz.wechat.common.util.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SHA1 {
    public SHA1() {
    }

    public static String gen(String... arr) throws NoSuchAlgorithmException {
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();
        String[] arr$ = arr;
        int len$ = arr.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String a = arr$[i$];
            sb.append(a);
        }

        return genStr(sb.toString());
    }

    public static String genWithAmple(String... arr) throws NoSuchAlgorithmException {
        Arrays.sort(arr);
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < arr.length; ++i) {
            String a = arr[i];
            sb.append(a);
            if(i != arr.length - 1) {
                sb.append('&');
            }
        }

        return genStr(sb.toString());
    }

    public static String genStr(String str) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        sha1.update(str.getBytes());
        byte[] output = sha1.digest();
        return bytesToHex(output);
    }

    protected static String bytesToHex(byte[] b) {
        char[] hexDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuffer buf = new StringBuffer();

        for(int j = 0; j < b.length; ++j) {
            buf.append(hexDigit[b[j] >> 4 & 15]);
            buf.append(hexDigit[b[j] & 15]);
        }

        return buf.toString();
    }
}

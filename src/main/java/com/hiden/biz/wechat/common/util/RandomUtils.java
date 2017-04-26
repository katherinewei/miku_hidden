package com.hiden.biz.wechat.common.util;

import java.util.Random;

public class RandomUtils {
    private static final String RANDOM_STR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();

    public RandomUtils() {
    }

    public static String getRandomStr() {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 16; ++i) {
            sb.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(RANDOM.nextInt("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".length())));
        }

        return sb.toString();
    }
}

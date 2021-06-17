package com.guidian.teaching.util;

import java.util.Random;

/**
 * @Description 此类用于生成随机数
 * @author dhxstart
 * @date 2021/6/17 14:57
 */
public class RandomUtils {

    /**
     * @Description 获取随机用户名（英文+数字）
     * @author dhxstart
     * @date 2021/6/17 14:58
     * @param length 表示生成几位随机数
     * @return java.lang.String
     */
    public static String getRandomUsername(Integer length) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                // 输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                builder.append((char)(random.nextInt(26) + temp));
            } else {
                builder.append(String.valueOf(random.nextInt(10)));
            }
        }
        return builder.toString();
    }
}

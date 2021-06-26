package com.guidian.teaching;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dhxstart
 * @Description
 * @date 2021/6/19 14:56
 */
public class MapTest {

    @Test
    void test1() {
        String[] ids = {"111", "'222'"};

        Map<String, String[]> map = new HashMap<>();
        map.put("ids", ids);

        System.out.println(map.toString());
    }

    @Test
    void test2() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        String localTime = df.format(time);

        String timeStr = "2021-06-26 00:00:00";
        LocalDateTime ldt = LocalDateTime.parse(timeStr, df);
        System.out.println("LocalDateTime转成String类型的时间："+localTime);
        System.out.println("String类型的时间转成LocalDateTime："+ldt);
    }
}

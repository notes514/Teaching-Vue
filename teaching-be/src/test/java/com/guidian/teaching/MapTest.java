package com.guidian.teaching;

import org.junit.jupiter.api.Test;

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
}

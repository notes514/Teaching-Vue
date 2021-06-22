package com.guidian.teaching.util;

import com.guidian.teaching.common.lang.Const;

/**
 * @Description 此类是操作课程的工具类
 * @author dhxstart
 * @date 2021/6/20 10:48
 */
public class CourseUtils {

    /**
     * @Description 通过课程节数获取对应的索引
     * @author dhxstart
     * @date 2021/6/20 10:49
     * @param section 课程节数
     * @return java.lang.Integer
     */
    public static Integer getSectionIndex(String section) {
        int index;
        if (section.equals(Const.COURSE_SECTION[0])) {
            index = 0;
        } else if (section.equals(Const.COURSE_SECTION[1])) {
            index = 1;
        } else if (section.equals(Const.COURSE_SECTION[2])) {
            index = 2;
        } else if (section.equals(Const.COURSE_SECTION[3])) {
            index = 3;
        } else {
            index = 4;
        }
        return index;
    }
}

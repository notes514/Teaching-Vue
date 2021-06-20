package com.guidian.teaching.mapper;

import com.guidian.teaching.entity.StudentCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * @Description 此接口用于规范针对于tb_student_course表的常用操作
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
public interface StudentCourseMapper extends BaseMapper<StudentCourse> {

    /**
     * 通过课程编号以及学生编号删除student_coursor中的多条记录
     *
     * @author dhxstart
     * @date 2021/6/19 15:51
     * @param map map集合，包含课程编号数组以及学生编号数组
     * @return boolean
     */
    boolean deleteStudentCourseByCourseIdsAndStudentIds(Map<String, Object[]> map);
}

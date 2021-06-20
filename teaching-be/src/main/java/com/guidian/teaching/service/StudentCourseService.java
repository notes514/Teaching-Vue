package com.guidian.teaching.service;

import com.guidian.teaching.entity.StudentCourse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description 此服务类用于实现针对于tb_student_course表的常用操作
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
public interface StudentCourseService extends IService<StudentCourse> {

    /**
     * 通过课程编号以及学生编号删除学生课程表中的多条记录
     *
     * @author dhxstart
     * @date 2021/6/19 15:49
     * @param courseIds 课程编号数组
     * @param studentIds 学生编号数组
     * @return boolean
     */
    boolean removeStudentCourseByCourseIdsAndStudentIds(String[] courseIds, String[] studentIds);
}

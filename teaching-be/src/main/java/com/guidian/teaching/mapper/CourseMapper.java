package com.guidian.teaching.mapper;

import com.guidian.teaching.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guidian.teaching.entity.StudentCourse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Description 此接口用于规范针对于tb_course表的常用操作
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 查询添加的课程是否已经存在
     *
     * @author dhxstart
     * @date 2021/6/20 11:15
     * @param map 多参数
     * @return boolean
     */
    boolean isExistCourse(Map<String, String> map);

    /**
     * 获取管理员添加的课程信息
     *
     * @author dhxstart
     * @date 2021/6/22 11:28
     * @return java.util.List<com.guidian.teaching.entity.Course>
     */
    List<Course> getAdministratorAddCourse();

    /**
     * 通过课程编号获取课程表中的多条记录
     *
     * @author dhxstart
     * @date 2021/6/22 17:12
     * @param courseIds 多个课程信息编号
     * @return java.util.List<com.guidian.teaching.entity.Course>
     */
    List<Course> getCourseAllByCourseId(List<String> courseIds);

    /**
     * 获取课程表中的课程类别为选修课的多条记录
     *
     * @author dhxstart
     * @date 2021/6/22 22:15
     * @return java.util.List<com.guidian.teaching.entity.Course>
     */
    List<Course> getElectiveCourse();

    /**
     * 根据当前学生获取未选修的选修课信息
     *
     * @author dhxstart
     * @date 2021/6/24 22:45
     * @param courseIds 已选择课程编号集合
     * @return java.util.List<com.guidian.teaching.entity.Course>
     */
    List<Course> getCurrentStudentNoCourses(@Param("courseIds") List<String> courseIds);
}

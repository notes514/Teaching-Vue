package com.guidian.teaching.mapper;

import com.guidian.teaching.entity.StudentCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
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

    /**
     * 根据用户权限获取不重复的学生选课信息
     *
     * @author dhxstart
     * @date 2021/6/23 16:46
     * @param courseId 课程编号
     * @param teacherId 教师编号
     * @return java.util.List<com.guidian.teaching.entity.StudentCourse>
     */
    @Select("SELECT * FROM `tb_student_course` WHERE `course_id` = #{courseId} AND `teacher_id` = #{teacherId} " +
            "GROUP BY `course_id` AND `teacher_id`;")
    StudentCourse getTeacherNotRepeating(@Param("courseId") String courseId, @Param("teacherId") String teacherId);

    /**
     * 根据用户权限获取不重复的学生选课信息
     *
     * @author dhxstart
     * @date 2021/6/23 16:46
     * @param courseId 课程编号
     * @param studentId 学生编号
     * @return java.util.List<com.guidian.teaching.entity.StudentCourse>
     */
    @Select("SELECT * FROM `tb_student_course` WHERE `course_id` = #{courseId} AND `student_id` = #{studentId} " +
            "GROUP BY `course_id` AND `student_id`;")
    StudentCourse getStudentNotRepeating(@Param("courseId") String courseId, @Param("studentId") String studentId);

    /**
     * 通过学生编号和课程编号获取该学生这么课程
     *
     * @author dhxstart
     * @date 2021/6/24 20:46
     * @param studentId 学生编号
     * @param courseId 课程编号
     * @return com.guidian.teaching.entity.StudentCourse
     */
    @Select("SELECT * FROM `tb_student_course` WHERE `student_id` = #{studentId} AND `course_id` = #{courseId}")
    StudentCourse getStudentCourseByStudentIdAndCourseId(@Param("studentId") String studentId,
                                                         @Param("courseId") String courseId);

    /**
     * 根据教师编号获取不重复的课程信息
     * @author dhxstart
     * @date 2021/6/24 21:22
     * @param teacherId 教师编号
     * @return com.guidian.teaching.entity.StudentCourse
     */
    @Select("SELECT * FROM `tb_student_course` WHERE `teacher_id` = #{teacherId} GROUP BY `teacher_id`;")
    List<StudentCourse> getStudentIdNotRepeating(@Param("teacherId") String teacherId);
}

package com.guidian.teaching.service;

import com.guidian.teaching.entity.StudentCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 根据用户权限获取不重复的学生选课信息
     *
     * @author dhxstart
     * @date 2021/6/23 16:46
     * @param courseId 课程编号
     * @param teacherId 教师编号
     * @return java.util.List<com.guidian.teaching.entity.StudentCourse>
     */
    StudentCourse getTeacherNotRepeating(String courseId, String teacherId);

    /**
     * 根据用户权限获取不重复的学生选课信息
     *
     * @author dhxstart
     * @date 2021/6/23 16:46
     * @param courseId 课程编号
     * @param studentId 学生编号
     * @return java.util.List<com.guidian.teaching.entity.StudentCourse>
     */
    StudentCourse getStudentNotRepeating(String courseId, String studentId);

    /**
     * 通过学生编号和课程编号获取该学生这门课程
     *
     * @author dhxstart
     * @date 2021/6/24 20:46
     * @param studentId 学生编号
     * @param courseId 课程编号
     * @return com.guidian.teaching.entity.StudentCourse
     */
    StudentCourse getStudentCourseByStudentIdAndCourseId(String studentId, String courseId);

    /**
     * 根据教师编号获取不重复的课程信息
     *
     * @author dhxstart
     * @date 2021/6/24 21:22
     * @param teacherId 教师编号
     * @return com.guidian.teaching.entity.StudentCourse
     */
    List<StudentCourse> getStudentIdNotRepeating(String teacherId);

    /**
     * 更新当前学生的课程成绩
     * @author dhxstart
     * @date 2021/6/25 11:06
     * @param studentId 学生编号
     * @param courseId 课程编号
     * @param score 课程成绩
     * @return boolean
     */
    boolean updateCurrentStudentCourseByScore(String studentId, String courseId, Integer score);

    /**
     * 获取学生选课表中学生编号为空的课程编号集合
     * @author dhxstart
     * @date 2021/6/26 11:32
     * @return java.util.List<com.guidian.teaching.entity.StudentCourse>
     */
    List<StudentCourse> getStudentCourseByStudentIdIsNull();

    /**
     * 判断该学生是否已经选过选课表中的课程
     * @author dhxstart
     * @date 2021/6/26 11:31
     * @param studentId 学生编号
     * @param courseIds 课程编号
     * @return boolean
     */
    boolean isStudentCourseElective(@Param("studentId") String studentId,
                                    @Param("courseIds") List<String> courseIds);

    /**
     * 获取当期学生已选课程
     * @author dhxstart
     * @date 2021/6/26 17:35
     * @param studentId 学生编号
     * @return java.util.List<com.guidian.teaching.entity.StudentCourse>
     */
    List<StudentCourse> getCurrentStudentSelectedCourse(@Param("studentId") String studentId);

    /**
     * 获取选课表中多门课程是否已经被选
     * @author dhxstart
     * @date 2021/6/26 23:13
     * @param courseIds 课程编号集合
     * @return java.util.List<com.guidian.teaching.entity.StudentCourse>
     */
    List<StudentCourse> isExistStudentNoNullAndCourseId(@Param("courseIds") String[] courseIds);
}

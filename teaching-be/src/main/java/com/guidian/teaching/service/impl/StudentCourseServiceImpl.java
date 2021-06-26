package com.guidian.teaching.service.impl;

import com.guidian.teaching.entity.StudentCourse;
import com.guidian.teaching.mapper.StudentCourseMapper;
import com.guidian.teaching.service.StudentCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description 此服务实现类用于实现针对于tb_student_course表的常用操作
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@Service
public class StudentCourseServiceImpl extends ServiceImpl<StudentCourseMapper, StudentCourse> implements StudentCourseService {
    @Autowired
    StudentCourseMapper studentCourseMapper;

    @Override
    public boolean removeStudentCourseByCourseIdsAndStudentIds(String[] courseIds, String[] studentIds) {
        Map<String, Object[]> map = new HashMap<>();
        map.put("courseIds", courseIds);
        map.put("studentIds", studentIds);
        return studentCourseMapper.deleteStudentCourseByCourseIdsAndStudentIds(map);
    }

    @Override
    public StudentCourse getTeacherNotRepeating(String courseId, String teacherId) {
        return studentCourseMapper.getTeacherNotRepeating(courseId, teacherId);
    }

    @Override
    public StudentCourse getStudentNotRepeating(String courseId, String studentId) {
        return studentCourseMapper.getStudentNotRepeating(courseId, studentId);
    }

    @Override
    public StudentCourse getStudentCourseByStudentIdAndCourseId(String studentId, String courseId) {
        return studentCourseMapper.getStudentCourseByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public List<StudentCourse> getStudentIdNotRepeating(String teacherId) {
        return studentCourseMapper.getStudentIdNotRepeating(teacherId);
    }

    @Override
    public boolean updateCurrentStudentCourseByScore(String studentId, String courseId, Integer score) {
        return studentCourseMapper.updateCurrentStudentCourseByScore(studentId, courseId, score);
    }

    @Override
    public List<StudentCourse> getStudentCourseByStudentIdIsNull() {
        return studentCourseMapper.getStudentCourseByStudentIdIsNull();
    }

    @Override
    public boolean isStudentCourseElective(String studentId, List<String> courseIds) {
        return studentCourseMapper.isStudentCourseElective(studentId, courseIds);
    }

    @Override
    public List<StudentCourse> getCurrentStudentSelectedCourse(String studentId) {
        return studentCourseMapper.getCurrentStudentSelectedCourse(studentId);
    }

    @Override
    public List<StudentCourse> isExistStudentNoNullAndCourseId(String[] courseIds) {
        return studentCourseMapper.isExistStudentNoNullAndCourseId(courseIds);
    }
}

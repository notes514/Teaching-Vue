package com.guidian.teaching.service.impl;

import com.guidian.teaching.entity.StudentCourse;
import com.guidian.teaching.mapper.StudentCourseMapper;
import com.guidian.teaching.service.StudentCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Description 此服务实现类用于实现针对于tb_student_course表的常用操作
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@Service
public class StudentCourseServiceImpl extends ServiceImpl<StudentCourseMapper, StudentCourse> implements StudentCourseService {

}

package com.guidian.teaching.service.impl;

import com.guidian.teaching.entity.Course;
import com.guidian.teaching.mapper.CourseMapper;
import com.guidian.teaching.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description 此服务实现类用于实现针对于tb_course表的常用操作
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    CourseMapper courseMapper;

    @Override
    public boolean isExistCourse(Map<String, String> map) {
        return courseMapper.isExistCourse(map);
    }

    @Override
    public List<Course> getAdministratorAddCourse() {
        return courseMapper.getAdministratorAddCourse();
    }

    @Override
    public List<Course> getCourseAllByCourseId(List<String> courseIds) {
        return courseMapper.getCourseAllByCourseId(courseIds);
    }

    @Override
    public List<Course> getElectiveCourse() {
        return courseMapper.getElectiveCourse();
    }
}

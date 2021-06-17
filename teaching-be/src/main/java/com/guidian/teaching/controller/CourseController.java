package com.guidian.teaching.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.Course;
import com.guidian.teaching.entity.Course;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @Description 课程表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/course")
public class CourseController extends BaseController {

    /**
     * @Description 添加课程信息
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param course 课程实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/save")
    public BaseResult saveCourse(@Validated @RequestBody Course course) {
        course.setCreateTime(LocalDateTime.now());
        // 插入数据
        boolean flag = courseService.save(course);
        return BaseResult.success(getCode(flag), getMsg(flag, "添加"), null);
    }

    /**
     * @Description 删除课程信息
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param courseIds 课程编号数组
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/delete")
    public BaseResult deleteCourse(@RequestBody String[] courseIds) {
        boolean flag = courseService.removeByIds(Arrays.asList(courseIds));
        return BaseResult.success(getCode(flag), getMsg(flag, "删除"), null);
    }

    /**
     * @Description 获取多条课程信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/list")
    public BaseResult getCourseAll(String courseName) {
        Page<Course> coursePage = courseService.page(
                getPage(),
                new QueryWrapper<Course>().like(StrUtil.isNotBlank(courseName), "course_name", courseName)
        );
        return BaseResult.success(coursePage);
    }

    /**
     * @Description 获取单条课程信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param courseId 课程编号
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/info/{courseId}")
    public BaseResult getCourseInfo(@PathVariable(name = "courseId") String courseId) {
        Course course = courseService.getOne(new QueryWrapper<Course>().eq("course_id", courseId));
        return BaseResult.success(course);
    }

    /**
     * @Description 更新一条课程信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param course 课程实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/update")
    public BaseResult updateCourse(@Validated @RequestBody Course course) {
        course.setUpdateTime(LocalDateTime.now());
        // 更新数据
        boolean flag = courseService.updateById(course);
        return BaseResult.success(getCode(flag), getMsg(flag, "更新"), null);
    }
}

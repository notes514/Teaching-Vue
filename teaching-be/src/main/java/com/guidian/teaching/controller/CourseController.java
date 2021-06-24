package com.guidian.teaching.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.common.lang.Const;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.*;
import com.guidian.teaching.entity.Course;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

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
        Course courseObj = courseService.getById(course.getCourseId());
        if (courseObj != null) {
            return BaseResult.failure("该课程编号已存在！");
        }

        course.setCreateTime(LocalDateTime.now());
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
     * @Description 根据用户名获取课程信息
     * @author dhxstart
     * @date 2021/6/20 15:23
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getCourseByUsernameInfo")
    public BaseResult getCourseByUsernameInfo(Principal principal, String courseName) {
        User user = userService.getByUsername(principal.getName());

        Page<Course> coursePage;
        if (user.getAuthority().equals(Const.STUDENT_AUTHORITY)) {
            Student student = studentService.getById(user.getUserId());
            System.out.println(student.getClbumId());
            coursePage = courseService.page(getPage(),
                    new QueryWrapper<Course>().eq("clbum_id", student.getClbumId())
            );
        } else {
            Teacher teacher = teacherService.getById(user.getUserId());
            coursePage = courseService.page(getPage(),
                    new QueryWrapper<Course>().eq("teacher_id", teacher.getTeacherId())
            );
        }
        return BaseResult.success(coursePage);
    }

    /**
     * @Description 获取课程表中课程类型为选修课的多条记录
     * @author dhxstart
     * @date 2021/6/22 22:18
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getElectiveCourse")
    public BaseResult getElectiveCourse() {
        Page<Course> coursePage = new Page<>();
        List<Course> courses = courseService.getElectiveCourse();
        coursePage.setCurrent(1);
        coursePage.setSize(8);
        coursePage.setTotal(courses.size());
        coursePage.setRecords(courses);
        return BaseResult.success(coursePage);
    }

    /**
     * @Description 获取多条课程信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/list")
    public BaseResult getCourseAllPage(String courseName) {
        Page<Course> coursePage = courseService.page(
                getPage(),
                new QueryWrapper<Course>().like(StrUtil.isNotBlank(courseName), "course_name", courseName)
        );
        return BaseResult.success(coursePage);
    }

    /**
     * @Description 获取课程信息
     * @author dhxstart
     * @date 2021/6/18 11:56
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getCourseAll")
    public BaseResult getCourseAll() {
        return BaseResult.success(courseService.list());
    }

    /**
     * 获取管理员添加的课程信息
     *
     * @author dhxstart
     * @date 2021/6/22 11:20
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getAdministratorAddCourse")
    public BaseResult getAdministratorAddCourse() {
        return BaseResult.success(courseService.getAdministratorAddCourse());
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

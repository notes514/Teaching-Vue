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
import java.time.format.DateTimeFormatter;
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
     * @Description 添加必修课程
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param course 课程实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/saveCompulsory")
    public BaseResult saveCompulsoryCourse(@Validated @RequestBody Course course) {
        Course courseObj = courseService.getById(course.getCourseId());
        if (courseObj != null) {
            return BaseResult.failure("该课程已存在，无需重复添加！");
        }

        course.setCourseCategory(0);
        course.setCreateTime(LocalDateTime.now());
        boolean flag = courseService.save(course);
        return BaseResult.success(getCode(flag), getMsg(flag, "添加"), null);
    }

    /**
     * @Description 添加选修课程
     * @author dhxstart
     * @date 2021/6/26 9:42
     * @param map map参数实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/saveElective")
    public BaseResult saveElectiveCourse(@Validated @RequestBody Map<String, String> map) {
        String courseId = map.get("courseId");
        String teacherId = map.get("teacherId");
        String courseHours = map.get("courseHours");
        Integer courseCredit = Integer.parseInt(map.get("courseCredit"));
        String courseName = map.get("courseName");
        String courseSection = map.get("courseSection");
        String courseWhichDay = map.get("courseWhichDay");
        String startTime = map.get("startTime");
        String endTime = map.get("endTime");

        Course courseObj = courseService.getById(courseId);
        if (courseObj != null) {
            return BaseResult.failure("该课程已存在，无需重复添加！");
        }
        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName(courseName);
        course.setCourseCredit(courseCredit);
        course.setCourseHours(courseHours);
        course.setCourseCategory(1);
        // 解决日期格式解析错误问题
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        course.setStartTime(LocalDateTime.parse(startTime, dtf));
        course.setEndTime(LocalDateTime.parse(endTime, dtf));
        course.setCreateTime(LocalDateTime.now());
        boolean courseFlag = courseService.save(course);

        // 创建学生选课实例并设置数据
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseId(courseId);
        studentCourse.setTeacherId(teacherId);
        studentCourse.setScore(null);
        studentCourse.setCourseSection(courseSection);
        studentCourse.setCourseWhichDay(courseWhichDay);
        studentCourse.setCreateTime(LocalDateTime.now());
        boolean studentCourseFlag = studentCourseService.save(studentCourse);

        boolean flag = courseFlag && studentCourseFlag;
        return BaseResult.success(getCode(flag), getMsg(flag, "添加"), null);
    }

    /**
     * 此方法是管理员用于删除必修授课计划的课程
     * @author dhxstart
     * @date 2021/6/26 23:20
     * @param courseIds 课程编号集合
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/deleteCompulsory")
    public BaseResult deleteCompulsory(@RequestBody String[] courseIds) {
        for (String courseId : courseIds) {
            StudentCourse serviceOne = studentCourseService.getOne(
                    new QueryWrapper<StudentCourse>().eq("course_id", courseId));
            if (serviceOne != null) {
                return BaseResult.failure("该课程已被选，操作失败！");
            }
        }
        boolean flag = courseService.removeByIds(Arrays.asList(courseIds));
        return BaseResult.success(getCode(flag), getMsg(flag, "删除"), null);
    }

    /**
     * 此方法是管理员用于删除选课授课计划的课程
     * @author dhxstart
     * @date 2021/6/26 23:17
     * @param courseIds 课程编号集合
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/deleteElective")
    public BaseResult electiveDelete(@RequestBody String[] courseIds) {
        List<StudentCourse> studentCourses = studentCourseService.isExistStudentNoNullAndCourseId(courseIds);
        if (studentCourses != null){
            BaseResult.failure("该课程已被选，操作失败！");
        }

        for (String courseId : courseIds) {
            studentCourseService.remove(
                    new QueryWrapper<StudentCourse>().eq("course_id", courseId));
        }
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
    @GetMapping("/list/{courseCategory}")
    public BaseResult getCourseAllPage(@PathVariable(name = "courseCategory") String courseCategory, String courseName) {
        Page<Course> coursePage = courseService.page(
                getPage(),
                new QueryWrapper<Course>().eq("course_category", courseCategory)
                        .like(StrUtil.isNotBlank(courseName), "course_name", courseName)
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

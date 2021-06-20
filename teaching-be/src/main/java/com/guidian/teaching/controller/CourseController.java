package com.guidian.teaching.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.dto.CourseDto;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.*;
import com.guidian.teaching.entity.Course;
import com.guidian.teaching.util.CourseUtils;
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
            return BaseResult.failure("该课程和班级已存在！");
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
     * 获取课程信息
     *
     * @author dhxstart
     * @date 2021/6/20 10:40
     * @param principal principal
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getCurriculumInfo")
    public BaseResult getCurriculumInfo(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Teacher teacher = teacherService.getById(user.getUserId());

        List<Course> courseList = courseService.list(new QueryWrapper<Course>().eq("teacher_id",
                teacher.getTeacherId()));

        List<CourseDto> courseDtoList = new ArrayList<>();
        courseDtoList.add(0, new CourseDto("1-2节", new ArrayList<>()));
        courseDtoList.add(1, new CourseDto("3-4节", new ArrayList<>()));
        courseDtoList.add(2,new CourseDto("5-6节", new ArrayList<>()));
        courseDtoList.add(3,new CourseDto("7-8节", new ArrayList<>()));
        courseDtoList.add(4,new CourseDto("9-10节", new ArrayList<>()));

        List<String> courseNameList1 = new ArrayList<>();
        List<String> courseNameList2 = new ArrayList<>();
        List<String> courseNameList3 = new ArrayList<>();
        List<String> courseNameList4 = new ArrayList<>();
        List<String> courseNameList5 = new ArrayList<>();
        int length = 8;
        for (int j = 0; j < length; j++) {
            courseNameList1.add(j, "");
            courseNameList2.add(j, "");
            courseNameList3.add(j, "");
            courseNameList4.add(j, "");
            courseNameList5.add(j, "");
        }

        for (Course course : courseList) {
            String section = course.getCourseSection();
            String[] courseWhichDays = course.getCourseWhichDay().split(",");

            CourseDto courseDto = new CourseDto();
            List<List<String>> courseLists = new ArrayList<>();


            for (String whichDay : courseWhichDays) {
                if (CourseUtils.getSectionIndex(section) == 0) {
                    courseNameList1.set(Integer.parseInt(whichDay), course.getCourseName());
                } else if (CourseUtils.getSectionIndex(section) == 1) {
                    courseNameList2.set(Integer.parseInt(whichDay), course.getCourseName());
                } else if (CourseUtils.getSectionIndex(section) == 2) {
                    courseNameList3.set(Integer.parseInt(whichDay), course.getCourseName());
                } else if (CourseUtils.getSectionIndex(section) == 3) {
                    courseNameList4.set(Integer.parseInt(whichDay), course.getCourseName());
                } else {
                    courseNameList5.set(Integer.parseInt(whichDay), course.getCourseName());
                }
            }

            courseDto.setSection(course.getCourseSection());

            if (CourseUtils.getSectionIndex(section) == 0) {
                courseLists.add(courseNameList1);
            } else if (CourseUtils.getSectionIndex(section) == 1) {
                courseLists.add(courseNameList2);
            } else if (CourseUtils.getSectionIndex(section) == 2) {
                courseLists.add(courseNameList3);
            } else if (CourseUtils.getSectionIndex(section) == 3) {
                courseLists.add(courseNameList4);
            } else {
                courseLists.add(courseNameList5);
            }

            courseDto.setCourse(courseLists);

            if (CourseUtils.getSectionIndex(section) == 0) {
                courseDtoList.set(0, courseDto);
            } else if (CourseUtils.getSectionIndex(section) == 1) {
                courseDtoList.set(1, courseDto);
            } else if (CourseUtils.getSectionIndex(section) == 2) {
                courseDtoList.set(2, courseDto);
            } else if (CourseUtils.getSectionIndex(section) == 3) {
                courseDtoList.set(3, courseDto);
            } else {
                courseDtoList.set(4, courseDto);
            }
        }
        return BaseResult.success(courseDtoList);
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

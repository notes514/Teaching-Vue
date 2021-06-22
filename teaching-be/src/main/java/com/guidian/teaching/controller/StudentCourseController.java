package com.guidian.teaching.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guidian.teaching.common.dto.Page;
import com.guidian.teaching.common.dto.StudentCourseDto;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.common.lang.Const;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 学生选课表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/student/course")
public class StudentCourseController extends BaseController {

    /**
     * @Description 添加学生课程信息
     * @author dhxstart
     * @date 2021/6/17 22:15
     * @param studentCourse 学生课程实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/save")
    public BaseResult saveStudentCourse(@Validated @RequestBody StudentCourse studentCourse) {
        StudentCourse course = studentCourseService.getOne(new QueryWrapper<StudentCourse>().eq("course_id",
                studentCourse.getCourseId()));
        if (course != null && course.getStudentId().equals(studentCourse.getStudentId())) {
            return BaseResult.failure("该学生的这门课程成绩已存在，添加失败！");
        }

        studentCourse.setCreateTime(LocalDateTime.now());
        studentCourse.setUpdateTime(LocalDateTime.now());
        boolean flag = studentCourseService.save(studentCourse);
        return BaseResult.success(getCode(flag), getMsg(flag, "添加"), null);
    }

    /**
     * @Description 删除学生课程信息
     * @author dhxstart
     * @date 2021/6/19 9:55
     * @param map 学生编号数组
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/delete")
    public BaseResult deleteClbum(@RequestParam Map<String, String> map) {
        String[] courseIdArr = map.get("courseIds").split(",");
        String[] studentIdArr = map.get("studentIds").split(",");

        boolean flag = studentCourseService.removeStudentCourseByCourseIdsAndStudentIds(courseIdArr, studentIdArr);
        return BaseResult.success(getCode(flag), getMsg(flag, "删除"), null);
    }

    /**
     * @Description 获取当前登录得学生成绩信息
     * @author dhxstart
     * @date 2021/6/17 22:15
     * @param principal 用来表示任何实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getStudentCourseByStudentId")
    public BaseResult getStudentCourseByStudentId(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        final Student student = studentService.getById(user.getUserId());
        List<StudentCourse> studentCourses = studentCourseService.list(
                new QueryWrapper<StudentCourse>().eq("student_id", user.getUserId()));

        List<StudentCourseDto> studentCourseDtos = new ArrayList<>();

        studentCourses.forEach(item -> {
            Course course = courseService.getById(item.getCourseId());
            StudentCourseDto studentCourseDto = new StudentCourseDto();
            studentCourseDto.setStudentId(student.getStudentId());
            studentCourseDto.setStudentName(student.getStudentName());
            studentCourseDto.setCourseId(item.getCourseId());
            studentCourseDto.setCourseName(course.getCourseName());
            studentCourseDto.setScore(item.getScore());
            studentCourseDto.setUpdateTime(item.getUpdateTime());

            studentCourseDtos.add(studentCourseDto);
        });

        Page<StudentCourseDto> courseDtoPage = new Page<>(studentCourseDtos, studentCourseDtos.size(), 8, 1);
        return BaseResult.success(courseDtoPage);
    }

    /**
     * @Description 获取当前课程信息
     * @author dhxstart
     * @date 2021/6/22 17:26
     * @param params 请求头参数
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/info")
    public BaseResult getStudentCourseInfo(@RequestParam(required = false)Map<String, Object> params) {
        // 获取参数编号
        String courseId = String.valueOf(params.get("courseId"));
        String studentId = String.valueOf(params.get("studentId"));
        Integer score = Integer.parseInt(String.valueOf(params.get("score")));

        Course course = courseService.getById(courseId);
        Student student = studentService.getById(studentId);
        Clbum clbum = clbumService.getById(student.getClbumId());

        StudentCourseDto studentCourseDto = new StudentCourseDto();
        studentCourseDto.setStudentId(student.getStudentId());
        studentCourseDto.setStudentName(student.getStudentName());
        studentCourseDto.setGender(student.getGender());
        studentCourseDto.setClbumId(clbum.getClbumId());
        studentCourseDto.setCourseId(courseId);
        studentCourseDto.setCourseName(course.getCourseName());
        studentCourseDto.setScore(score);
        return BaseResult.success(studentCourseDto);
    }


    /**
     * @Description 获取多条学生课程信息记录
     * @author dhxstart
     * @date 2021/6/17 22:15
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/list")
    public BaseResult getStudentCourseAll(Principal principal) {
        User user = userService.getByUsername(principal.getName());

        int total;
        List<StudentCourse> studentCourses;
        if (user.getAuthority().equals(Const.STUDENT_AUTHORITY)) {
            total = studentCourseService.count(
                    new QueryWrapper<StudentCourse>().eq("student_id", user.getUserId()));
            studentCourses = studentCourseService.list(
                    new QueryWrapper<StudentCourse>().eq("student_id", user.getUserId()));
        } else {
            total = studentCourseService.count(
                    new QueryWrapper<StudentCourse>().eq("teacher_id", user.getUserId()));
            studentCourses = studentCourseService.list(
                    new QueryWrapper<StudentCourse>().eq("teacher_id", user.getUserId()));
        }

        List<StudentCourseDto> studentCourseDtoList = new ArrayList<>();
        studentCourses.forEach(item -> {
            Student studentOne = studentService.getOne(new QueryWrapper<Student>().eq("student_id", item.getStudentId()));
            Course courseOne = courseService.getOne(new QueryWrapper<Course>().eq("course_id", item.getCourseId()));

            StudentCourseDto studentCourseDto = new StudentCourseDto();
            studentCourseDto.setStudentId(studentOne.getStudentId());
            studentCourseDto.setStudentName(studentOne.getStudentName());
            studentCourseDto.setGender(studentOne.getGender());
            studentCourseDto.setClbumId(studentOne.getClbumId());
            studentCourseDto.setCourseId(courseOne.getCourseId());
            studentCourseDto.setCourseName(courseOne.getCourseName());
            studentCourseDto.setScore(item.getScore());
            studentCourseDto.setUpdateTime(item.getUpdateTime());

            studentCourseDtoList.add(studentCourseDto);
        });

        Page<StudentCourseDto> studentCourseDtoPage = new Page<>(studentCourseDtoList, total, 8, 1);

        return BaseResult.success(studentCourseDtoPage);
    }

    /**
     * @Description 更新一条学生课程信息记录
     * @author dhxstart
     * @date 2021/6/17 22:15
     * @param studentCourseDto 学生课程实体dto
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/update")
    public BaseResult updateStudentCourse(@Validated @RequestBody StudentCourseDto studentCourseDto) {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(studentCourseDto.getStudentId());
        studentCourse.setCourseId(studentCourseDto.getCourseId());
        studentCourse.setTeacherId(studentCourseDto.getTeacherId());
        studentCourse.setScore(studentCourseDto.getScore());
        studentCourse.setUpdateTime(LocalDateTime.now());

        boolean flag = studentCourseService.updateById(studentCourse);
        return BaseResult.success(getCode(flag), getMsg(flag, "更新"), null);
    }
}

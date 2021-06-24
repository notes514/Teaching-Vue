package com.guidian.teaching.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.dto.CourseDto;
import com.guidian.teaching.common.dto.StudentCourseDto;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.common.lang.Const;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.*;
import com.guidian.teaching.util.CourseUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

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
     * @param map 学生课程实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/save")
    public BaseResult saveStudentCourse(@Validated @RequestBody Map<String, String> map) {
        return saveOrUpdateStudentCourse(map, Const.SAVE);
    }

    /**
     * 删除学生课程信息
     *
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
     * @Description 通过课程编号获取课程信息
     * @author dhxstart
     * @date 2021/6/23 22:48
     * @param principal principal
     * @param courseId 课程编号
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/teacherCourseInfo/{courseId}")
    public BaseResult getTeacherCourseInfo(Principal principal, @PathVariable(name = "courseId") String courseId) {
        User user = userService.getByUsername(principal.getName());

        StudentCourse studentCourse = studentCourseService.getTeacherNotRepeating(courseId, user.getUserId());
        Course course = courseService.getById(courseId);
        Student student = studentService.getById(studentCourse.getStudentId());

        StudentCourseDto studentCourseDto = new StudentCourseDto();
        studentCourseDto.setStudentId(studentCourse.getStudentId());
        studentCourseDto.setTeacherId(studentCourse.getTeacherId());
        studentCourseDto.setClbumId(student.getClbumId());
        studentCourseDto.setCourseId(course.getCourseId());
        studentCourseDto.setCourseName(course.getCourseName());
        studentCourseDto.setCourseSection(studentCourse.getCourseSection());
        studentCourseDto.setCourseWhichDay(studentCourse.getCourseWhichDay());
        studentCourseDto.setUpdateTime(studentCourse.getUpdateTime());

        return BaseResult.success(studentCourseDto);
    }

    /**
     * 获取当前课程信息
     *
     * @author dhxstart
     * @date 2021/6/22 17:26
     * @param params 请求头参数
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/info")
    public BaseResult getStudentCourseInfo(@RequestParam(required = false)Map<String, Object> params) {
        String studentId = String.valueOf(params.get("studentId"));
        String courseId = String.valueOf(params.get("courseId"));

        Course course = courseService.getById(courseId);
        Student student = studentService.getById(studentId);
        StudentCourse studentCourse = studentCourseService.getStudentCourseByStudentIdAndCourseId(studentId, courseId);

        StudentCourseDto studentCourseDto = new StudentCourseDto();
        studentCourseDto.setStudentId(studentCourse.getStudentId());
        studentCourseDto.setCourseId(studentCourse.getCourseId());
        studentCourseDto.setStudentId(studentCourse.getStudentId());
        studentCourseDto.setClbumId(student.getClbumId());
        studentCourseDto.setCourseName(course.getCourseName());
        studentCourseDto.setScore(studentCourse.getScore());
        return BaseResult.success(studentCourseDto);
    }

    /**
     * 根据用户名获取课程信息
     *
     * @author dhxstart
     * @date 2021/6/20 15:23
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getCourseByUsernameInfo")
    public BaseResult getCourseByUsernameInfo(Principal principal) {
        User user = userService.getByUsername(principal.getName());

        Page<Course> coursePage;
        if (user.getAuthority().equals(Const.STUDENT_AUTHORITY)) {
            Student student = studentService.getById(user.getUserId());
            List<StudentCourse> studentCourseList = studentCourseService.list(
                    new QueryWrapper<StudentCourse>().eq("clbum_id", student.getClbumId()));

            coursePage = getCoursePage(studentCourseList, user);
        } else {
            List<StudentCourse> studentCourseList = studentCourseService.list(
                    new QueryWrapper<StudentCourse>().eq("teacher_id", user.getUserId()));

            coursePage = getCoursePage(studentCourseList, user);
        }
        return BaseResult.success(coursePage);
    }

    /**
     * 获取课程信息
     *
     * @author dhxstart
     * @date 2021/6/23 20:48
     * @param studentCourseList studentCourseList
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.guidian.teaching.entity.Course>
     */
    public Page<Course> getCoursePage(List<StudentCourse> studentCourseList, User user) {
        LinkedHashSet<String> courseIds = new LinkedHashSet<>();
        studentCourseList.forEach(studentCourse -> {
            courseIds.add(studentCourse.getCourseId());
        });

        List<Course> courseList = new ArrayList<>();
        Iterator courseId = courseIds.iterator();
        while (courseId.hasNext()) {
            StudentCourse studentCourse;
            if (user.getAuthority().equals(Const.STUDENT_AUTHORITY)) {
                studentCourse = studentCourseService.getStudentNotRepeating((String) courseId.next(),
                        user.getUserId());
            } else {
                studentCourse = studentCourseService.getTeacherNotRepeating((String) courseId.next(),
                        user.getUserId());
            }
            courseList.add(courseService.getById(studentCourse.getCourseId()));
        }

        Page<Course> coursePage = new Page<>();
        coursePage.setCurrent(1);
        coursePage.setSize(8);
        coursePage.setTotal(courseList.size());
        coursePage.setRecords(courseList);
        return coursePage;
    }

    /**
     * 获取多条学生课程信息记录
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

        Page page = new Page<StudentCourseDto>();
        page.setCurrent(1);
        page.setSize(8);
        page.setTotal(total);
        page.setRecords(studentCourseDtoList);
        return BaseResult.success(page);
    }

    /**
     * 获取当前登录得学生成绩信息
     * @author dhxstart
     * @date 2021/6/24 22:13
     * @param principal principal
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getCurrentElectiveInfo")
    public BaseResult getCurrentElectiveInfo(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        List<StudentCourse> studentCourses = studentCourseService.list(
                new QueryWrapper<StudentCourse>().eq("student_id", user.getUserId()));

        List<Course> courseList = new ArrayList<>();
        studentCourses.forEach(item -> {
            Course course = courseService.getById(item.getCourseId());
            courseList.add(course);
        });

        Page page = new Page<StudentCourseDto>();
        page.setCurrent(1);
        page.setSize(8);
        page.setTotal(courseList.size());
        page.setRecords(courseList);
        return BaseResult.success(page);
    }

    /**
     * 获取当前学生未选修的课程信息
     * @author dhxstart
     * @date 2021/6/24 22:49
     * @param principal principal
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getCurrentStudentNoCoursesInfo")
    public BaseResult getCurrentStudentNoCoursesInfo(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        List<StudentCourse> studentCourses = studentCourseService.list(
                new QueryWrapper<StudentCourse>().eq("student_id", user.getUserId()));

        List<String> courseIds = new ArrayList<>();
        studentCourses.forEach(studentCourse -> {
            courseIds.add(studentCourse.getCourseId());
        });

        System.out.println("courseIds：" + courseIds);

        List<Course> courseList = courseService.getCurrentStudentNoCourses(courseIds);

        Page page = new Page<StudentCourseDto>();
        page.setCurrent(1);
        page.setSize(8);
        page.setTotal(courseList.size());
        page.setRecords(courseList);
        return BaseResult.success(null);
    }

    /**
     * 此方法用于获取教师和学生的课程表
     * @author dhxstart
     * @date 2021/6/24 21:52
     * @param principal principal
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getCurriculumInfo")
    public BaseResult getCurriculumInfo(Principal principal) {
        User user = userService.getByUsername(principal.getName());

        List<StudentCourse> studentCourseList = new ArrayList<>();

        if (user.getAuthority().equals(Const.STUDENT_AUTHORITY)) {
            List<StudentCourse> studentCourses = studentCourseService.list(
                    new QueryWrapper<StudentCourse>().eq("student_id", user.getUserId()));
            studentCourses.forEach(studentCourse -> {
                StudentCourse studentNotRepeating = studentCourseService.getStudentNotRepeating(
                        studentCourse.getCourseId(), studentCourse.getStudentId());
                studentCourseList.add(studentNotRepeating);
            });
        } else {
            List<StudentCourse> studentCourses = studentCourseService.list(
                    new QueryWrapper<StudentCourse>().eq("teacher_id", user.getUserId()));
            studentCourses.forEach(studentCourse -> {
                StudentCourse teacherNotRepeating = studentCourseService.getTeacherNotRepeating(
                        studentCourse.getCourseId(), studentCourse.getTeacherId());
                studentCourseList.add(teacherNotRepeating);
            });
        }

        List<Course> courseList = new ArrayList<>();
        studentCourseList.forEach(studentCourse -> {
            courseList.add(courseService.getById(studentCourse.getCourseId()));
        });
        return BaseResult.success(getCourses(studentCourseList, courseList));
    }

    /**
     * 获取当前登录得学生成绩信息
     * @author dhxstart
     * @date 2021/6/24 22:13
     * @param principal principal
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getCurrentStudentCourseByScore")
    public BaseResult getCurrentStudentCourseByScore(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        final Student student = studentService.getById(user.getUserId());
        List<StudentCourse> studentCourses = studentCourseService.list(
                new QueryWrapper<StudentCourse>().eq("student_id", user.getUserId()));

        List<StudentCourseDto> studentCourseDtos = new ArrayList<>();
        studentCourses.forEach(item -> {
            if (item.getScore() != null) {
                Course course = courseService.getById(item.getCourseId());

                StudentCourseDto studentCourseDto = new StudentCourseDto();
                studentCourseDto.setStudentId(student.getStudentId());
                studentCourseDto.setStudentName(student.getStudentName());
                studentCourseDto.setCourseId(item.getCourseId());
                studentCourseDto.setCourseName(course.getCourseName());
                studentCourseDto.setScore(item.getScore());
                studentCourseDto.setUpdateTime(item.getUpdateTime());
                studentCourseDtos.add(studentCourseDto);
            }
        });

        Page page = new Page<StudentCourseDto>();
        page.setCurrent(1);
        page.setSize(8);
        page.setTotal(studentCourseDtos.size());
        page.setRecords(studentCourseDtos);
        return BaseResult.success(page);
    }

    /**
     * @Description 更新一条学生课程信息记录
     * @author dhxstart
     * @date 2021/6/17 22:15
     * @param map 学生课程Map实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/update")
    public BaseResult updateStudentCourse(@Validated @RequestBody Map<String, String> map) {
        return saveOrUpdateStudentCourse(map, Const.UPDATE);
    }

    /**
     * 更新用户选课表中的成绩
     *
     * @author dhxstart
     * @date 2021/6/24 21:12
     * @param map studentCourse信息实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/updateStudentCourseByScore")
    public BaseResult updateStudentByScore(@Validated @RequestBody Map<String, String> map) {
        String studentId = map.get("studentId");
        String courseId = map.get("courseId");
        Integer score = Integer.parseInt(map.get("score"));

        StudentCourse studentCourse = studentCourseService.getStudentCourseByStudentIdAndCourseId(studentId, courseId);
        studentCourse.setScore(score);
        studentCourse.setUpdateTime(LocalDateTime.now());

        boolean flag = studentCourseService.updateById(studentCourse);
        return BaseResult.success(getCode(flag), getMsg(flag, "更新"), null);
    }

    /**
     * @Description 此方法用于执行插入或删除操作
     * @author dhxstart
     * @date 2021/6/24 10:00
     * @param map map
     * @param flag flag
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    public BaseResult saveOrUpdateStudentCourse(Map<String, String> map, String flag) {
        String clbumId = map.get("clbumId");
        String courseId = map.get("courseId");
        String teacherId = map.get("teacherId");
        String courseSection = map.get("courseSection");
        String courseWhichDay = map.get("courseWhichDay");

        if (flag.equals(Const.UPDATE)) {
            List<StudentCourse> studentCourseList = studentCourseService.list(
                    new QueryWrapper<StudentCourse>().eq("course_id", courseId));
            studentCourseList.forEach(data -> {
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudentId(data.getStudentId());
                studentCourse.setCourseId(courseId);
                studentCourse.setTeacherId(teacherId);
                studentCourse.setCourseSection(courseSection);
                studentCourse.setCourseWhichDay(courseWhichDay);
                studentCourse.setCreateTime(LocalDateTime.now());
                studentCourse.setUpdateTime(LocalDateTime.now());
                studentCourse.setScore(null);

                studentCourseService.update(studentCourse, new QueryWrapper<StudentCourse>().eq(
                        "course_id", courseId));
            });
        } else {
            List<Student> students = studentService.list(new QueryWrapper<Student>().eq("clbum_id", clbumId));
            students.forEach(student -> {
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudentId(student.getStudentId());
                studentCourse.setCourseId(courseId);
                studentCourse.setTeacherId(teacherId);
                studentCourse.setCourseSection(courseSection);
                studentCourse.setCourseWhichDay(courseWhichDay);
                studentCourse.setCreateTime(LocalDateTime.now());
                studentCourse.setUpdateTime(LocalDateTime.now());
                studentCourse.setScore(null);

                studentCourseService.save(studentCourse);
            });
        }

        String msg = flag.equals(Const.SAVE) ? "添加" : "更新";
        return BaseResult.success(getCode(true), getMsg(true, msg), null);
    }

    /**
     * 通过集合获取并返回课程信息
     *
     * @author dhxstart
     * @date 2021/6/22 8:48
     * @param studentCourseList 学生选课集合
     * @param courseList 课程集合
     * @return java.util.List<com.guidian.teaching.common.dto.CourseDto>
     */
    public List<CourseDto> getCourses(List<StudentCourse> studentCourseList, List<Course> courseList) {
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

        for (int i = 0; i < studentCourseList.size(); i++) {
            String section = studentCourseList.get(i).getCourseSection();
            String[] courseWhichDays = studentCourseList.get(i).getCourseWhichDay().split(",");
            String courseName = courseList.get(i).getCourseName();

            CourseDto courseDto = new CourseDto();
            List<List<String>> courseLists = new ArrayList<>();
            for (String whichDay : courseWhichDays) {
                if (CourseUtils.getSectionIndex(section) == 0) {
                    courseNameList1.set(Integer.parseInt(whichDay), courseName);
                } else if (CourseUtils.getSectionIndex(section) == 1) {
                    courseNameList2.set(Integer.parseInt(whichDay), courseName);
                } else if (CourseUtils.getSectionIndex(section) == 2) {
                    courseNameList3.set(Integer.parseInt(whichDay), courseName);
                } else if (CourseUtils.getSectionIndex(section) == 3) {
                    courseNameList4.set(Integer.parseInt(whichDay), courseName);
                } else {
                    courseNameList5.set(Integer.parseInt(whichDay), courseName);
                }
            }
            courseDto.setSection(section);

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
        return courseDtoList;
    }
}
package com.guidian.teaching.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.common.lang.Const;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.Clbum;
import com.guidian.teaching.entity.Student;
import com.guidian.teaching.entity.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 学生表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/student")
public class StudentController extends BaseController {

    /**
     * @Description 添加学生信息
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param student 学生实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/save")
    public BaseResult saveStudent(@Validated @RequestBody Student student) {
        if (clbumService.getById(student.getClbumId()) == null) {
            return BaseResult.failure("没有该班级，添加失败！");
        }
        if (studentService.getById(student.getStudentId()) != null) {
            return BaseResult.failure("已存在该学生编号，添加失败！");
        }

        // 添加教师用户
        User user = new User();
        user.setUserId(student.getStudentId());
        user.setUsername(student.getStudentName());
        // 对默认密码进行加密
        String password = bCryptPasswordEncoder.encode(Const.DEFAULT_PASSWORD);
        user.setPassword(password);
        user.setAvatar(Const.AVATAR_URL);
        user.setAuthority(0);
        user.setCreateTime(LocalDateTime.now());

        boolean userFlag = userService.save(user);
        if (!userFlag) {
            return BaseResult.success(getCode(false), getMsg(false, "添加"), null);
        }

        student.setCreateTime(LocalDateTime.now());
        boolean flag = studentService.save(student);
        return BaseResult.success(getCode(flag), getMsg(flag, "添加"), null);
    }

    /**
     * @Description 删除学生信息
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param studentIds 学生编号数组
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/delete")
    public BaseResult deleteStudent(@RequestBody String[] studentIds) {
        boolean flag = studentService.removeByIds(Arrays.asList(studentIds));
        return BaseResult.success(getCode(flag), getMsg(flag, "删除"), null);
    }

    /**
     * @Description 获取多条学生信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/list")
    public BaseResult getStudentAllPage(String studentName) {
        Page<Student> studentPage = studentService.page(
                getPage(),
                new QueryWrapper<Student>().like(StrUtil.isNotBlank(studentName), "student_name", studentName)
        );
        return BaseResult.success(studentPage);
    }

    /**
     * @Description 根据编辑编号获取多条学生记录
     * @author dhxstart
     * @date 2021/6/18 14:52
     * @param clbumId 班级编号
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/getStudentAllByClbumId/{clbumId}")
    public BaseResult getStudentAllByClbumId(@PathVariable(name = "clbumId") String clbumId) {
        List<Student> studentList = studentService.list(new QueryWrapper<Student>().eq("clbum_id", clbumId));
        return BaseResult.success(studentList);
    }

    /**
     * @Description 获取单条学生信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param studentId 学生编号
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/info/{studentId}")
    public BaseResult getStudentInfo(@PathVariable(name = "studentId") String studentId) {
        Student student = studentService.getOne(new QueryWrapper<Student>().eq("student_id", studentId));
        return BaseResult.success(student);
    }

    /**
     * @Description 更新一条学生信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param student 学生实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/update")
    public BaseResult updateStudent(@Validated @RequestBody Student student) {
        student.setUpdateTime(LocalDateTime.now());
        // 更新数据
        boolean flag = studentService.updateById(student);
        return BaseResult.success(getCode(flag), getMsg(flag, "更新"), null);
    }
}

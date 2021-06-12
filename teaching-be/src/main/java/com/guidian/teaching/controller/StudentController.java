package com.guidian.teaching.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.Student;
import com.guidian.teaching.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 学生表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/student")
public class StudentController extends BaseController {
    @Autowired
    StudentService studentService;

    @GetMapping("/getStudentById")
    public BaseResult getStudentById() {
        return BaseResult.success(studentService.getOne(new QueryWrapper<Student>().eq("student_id", "1816030101119")));
    }

}

package com.guidian.teaching.controller.base;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.service.*;
import com.guidian.teaching.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @Description 基类控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:53
 */
public class BaseController {
    @Autowired
    public HttpServletRequest request;
    @Autowired
    public HttpServletResponse response;
    @Autowired
    public RedisUtils redisUtils;
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserService userService;
    @Autowired
    public AcademyService academyService;
    @Autowired
    public ProfessionService professionService;
    @Autowired
    public ClbumService clbumService;
    @Autowired
    public TeacherService teacherService;
    @Autowired
    public StudentService studentService;
    @Autowired
    public CourseService courseService;
    @Autowired
    public StudentCourseService studentCourseService;

    /**
     * @Description 获取分页页面
     * @author dhxstart
     * @date 2021/6/16 15:33
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page
     */
    public Page getPage() {
        int current = ServletRequestUtils.getIntParameter(request, "current", 1);
        int size = ServletRequestUtils.getIntParameter(request, "size", 8);
        return new Page(current, size);
    }

    /**
     * @Description 获取请求状态编码
     * @author dhxstart
     * @date 2021/6/16 16:31
     * @param flag 修改删除状态
     * @return java.lang.Integer
     */
    public Integer getCode(boolean flag) {
        return flag ? 200 : -1;
    }

    /**
     * @Description 获取请求提示信息
     * @author dhxstart
     * @date 2021/6/16 16:30
     * @param flag 修改删除状态
     * @param msg 提示信息
     * @return java.lang.String
     */
    public String getMsg(boolean flag, String msg) {
        return flag ? msg + "成功！" : msg + "失败！";
    }
}

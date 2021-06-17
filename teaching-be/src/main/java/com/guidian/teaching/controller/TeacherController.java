package com.guidian.teaching.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.common.lang.Const;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.Academy;
import com.guidian.teaching.entity.Clbum;
import com.guidian.teaching.entity.Teacher;
import com.guidian.teaching.entity.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @Description 老师表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController extends BaseController {

    /**
     * @Description 添加教师信息
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param teacher 教师实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/save")
    public BaseResult saveProfession(@Validated @RequestBody Teacher teacher) {
        if (academyService.getById(teacher.getAcademyId()) == null) {
            return BaseResult.failure("没有该院系编号，添加失败！");
        }
        if (teacherService.getById(teacher.getTeacherId()) != null) {
            return BaseResult.failure("已存在该教师编号，添加失败！");
        }

        // 添加教师用户
        User user = new User();
        user.setUserId(teacher.getTeacherId());
        user.setUsername(teacher.getTeacherName());
        // 对默认密码进行加密
        String password = bCryptPasswordEncoder.encode(Const.DEFAULT_PASSWORD);
        user.setPassword(password);
        user.setAvatar(Const.AVATAR_URL);
        user.setAuthority(1);
        user.setCreateTime(LocalDateTime.now());

        boolean userFlag = userService.save(user);
        if (!userFlag) {
            return BaseResult.success(getCode(false), getMsg(false, "添加"), null);
        }

        teacher.setCreateTime(LocalDateTime.now());
        boolean teacherFlag = teacherService.save(teacher);
        return BaseResult.success(getCode(teacherFlag), getMsg(teacherFlag, "添加"), null);
    }

    /**
     * @Description 删除教师信息
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param teacherIds 教师编号数组
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/delete")
    public BaseResult deleteProfession(@RequestBody String[] teacherIds) {
        boolean flag = teacherService.removeByIds(Arrays.asList(teacherIds));
        return BaseResult.success(getCode(flag), getMsg(flag, "删除"), null);
    }

    /**
     * @Description 获取多条教师信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/list")
    public BaseResult getProfessionAll(String teacherName) {
        Page<Teacher> teacherPage = teacherService.page(
                getPage(),
                new QueryWrapper<Teacher>().like(StrUtil.isNotBlank(teacherName), "teacher_name", teacherName)
        );
        return BaseResult.success(teacherPage);
    }

    /**
     * @Description 获取单条教师信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param teacherId 教师编号
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/info/{teacherId}")
    public BaseResult getProfessionInfo(@PathVariable(name = "teacherId") String teacherId) {
        Teacher teacher = teacherService.getOne(new QueryWrapper<Teacher>().eq("teacher_id", teacherId));
        return BaseResult.success(teacher);
    }

    /**
     * @Description 更新一条教师信息记录
     * @author dhxstart
     * @date 2021/6/16 17:23
     * @param teacher 教师实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/update")
    public BaseResult updateProfession(@Validated @RequestBody Teacher teacher) {
        teacher.setUpdateTime(LocalDateTime.now());
        // 更新数据
        boolean flag = teacherService.updateById(teacher);
        return BaseResult.success(getCode(flag), getMsg(flag, "更新"), null);
    }
}

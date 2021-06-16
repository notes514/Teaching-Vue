package com.guidian.teaching.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.Academy;
import com.guidian.teaching.service.AcademyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @Description 学院表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/academy")
public class AcademyController extends BaseController {

    /**
     * @Description 添加学院信息
     * @author dhxstart
     * @date 2021/6/16 8:45
     * @param academy 学院实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/save")
    public BaseResult saveAcademy(@Validated @RequestBody Academy academy) {
        // 设置创建时间
        academy.setCreateTime(LocalDateTime.now());
        // 插入数据
        academyService.save(academy);
        return BaseResult.success(academy);
    }

    /**
     * @Description 删除学院信息
     * @author dhxstart
     * @date 2021/6/16 8:46
     * @param academyIds 学院编号数组
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/delete")
    public BaseResult deleteAcademy(@RequestBody String[] academyIds) {
        boolean flag = academyService.removeByIds(Arrays.asList(academyIds));
        return BaseResult.success(getCode(flag), getMsg(flag, "删除"), null);
    }

    /**
     * @Description 获取多条学院信息记录
     * @author dhxstart
     * @date 2021/6/16 8:52
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/list")
    public BaseResult getAcademyAll(String academyName) {
        Page<Academy> academyPage = academyService.page(
                getPage(),
                new QueryWrapper<Academy>().like(StrUtil.isNotBlank(academyName), "academy_name", academyName)
        );
        return BaseResult.success(academyPage);
    }

    /**
     * @Description 获取单条学院信息记录
     * @author dhxstart
     * @date 2021/6/16 8:53
     * @param academyId 学院编号
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/info/{academyId}")
    public BaseResult getAcademInfo(@PathVariable(name = "academyId") String academyId) {
        Academy academy = academyService.getOne(new QueryWrapper<Academy>().eq("academy_id", academyId));
        return BaseResult.success(academy);
    }

    /**
     * @Description 更新一条学院信息记录
     * @author dhxstart
     * @date 2021/6/16 8:53
     * @param academy 学院实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/update")
    public BaseResult updateAcademy(@Validated @RequestBody Academy academy) {
        // 编辑更新时间
        academy.setUpdateTime(LocalDateTime.now());
        // 更新数据
        boolean flag = academyService.updateById(academy);
        return BaseResult.success(getCode(flag), getMsg(flag, "更新"), null);
    }
}
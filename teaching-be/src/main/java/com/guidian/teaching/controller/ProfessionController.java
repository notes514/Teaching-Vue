package com.guidian.teaching.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.Academy;
import com.guidian.teaching.entity.Profession;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @Description 专业表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/profession")
public class ProfessionController extends BaseController {

    /**
     * @Description 添加专业信息
     * @author dhxstart
     * @date 2021/6/16 16:01
     * @param profession 专业实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/save")
    public BaseResult saveProfession(@Validated @RequestBody Profession profession) {
        Academy academyId = academyService.getById(profession.getAcademyId());
        if (academyId == null) {
            return BaseResult.failure("没有该院系编号，添加失败！");
        }
        // 设置创建时间
        profession.setCreateTime(LocalDateTime.now());
        // 插入数据
        professionService.save(profession);
        return BaseResult.success(profession);
    }

    /**
     * @Description 删除专业信息
     * @author dhxstart
     * @date 2021/6/16 16:01
     * @param professionIds 专业编号数组
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/delete")
    public BaseResult deleteProfession(@RequestBody String[] professionIds) {
        boolean flag = professionService.removeByIds(Arrays.asList(professionIds));
        return BaseResult.success(getCode(flag), getMsg(flag, "删除"), null);
    }

    /**
     * @Description 获取多条专业信息记录
     * @author dhxstart
     * @date 2021/6/16 16:01
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/list")
    public BaseResult getProfessionAll(String professionName) {
        Page<Profession> academyPage = professionService.page(
                getPage(),
                new QueryWrapper<Profession>().like(StrUtil.isNotBlank(professionName),
                        "profession_name", professionName)
        );
        return BaseResult.success(academyPage);
    }

    /**
     * @Description 获取单条专业信息记录
     * @author dhxstart
     * @date 2021/6/16 16:01
     * @param professionId 学院编号
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/info/{professionId}")
    public BaseResult getProfessionInfo(@PathVariable(name = "professionId") String professionId) {
        Profession profession = professionService.getOne(new QueryWrapper<Profession>().eq("profession_id",
                professionId));
        return BaseResult.success(profession);
    }

    /**
     * @Description 更新一条专业信息记录
     * @author dhxstart
     * @date 2021/6/16 16:01
     * @param profession 学院实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/update")
    public BaseResult updateProfession(@Validated @RequestBody Profession profession) {
        profession.setUpdateTime(LocalDateTime.now());
        // 更新数据
        boolean flag = professionService.updateById(profession);
        return BaseResult.success(getCode(flag), getMsg(flag, "更新"), null);
    }
}

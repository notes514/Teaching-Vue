package com.guidian.teaching.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.Clbum;
import com.guidian.teaching.entity.Profession;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @Description 班级表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/clbum")
public class ClbumController extends BaseController {
    
    /**
     * @Description 添加班级信息
     * @author dhxstart
     * @date 2021/6/16 17:02
     * @param clbum 班级实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/save")
    public BaseResult saveProfession(@Validated @RequestBody Clbum clbum) {
        Profession profession = professionService.getById(clbum.getProfessionId());
        if (profession == null) {
            return BaseResult.failure("没有该专业编号，添加失败！");
        }
        clbum.setCreateTime(LocalDateTime.now());
        // 插入数据
        clbumService.save(clbum);
        return BaseResult.success(clbum);
    }

    /**
     * @Description 删除班级信息
     * @author dhxstart
     * @date 2021/6/16 17:02
     * @param clbumIds 班级编号数组
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/delete")
    public BaseResult deleteProfession(@RequestBody String[] clbumIds) {
        boolean flag = clbumService.removeByIds(Arrays.asList(clbumIds));
        return BaseResult.success(getCode(flag), getMsg(flag, "删除"), null);
    }

    /**
     * @Description 获取多条班级信息记录
     * @author dhxstart
     * @date 2021/6/16 17:02
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/list")
    public BaseResult getProfessionAll(String clbumName) {
        Page<Clbum> clbumPage = clbumService.page(
                getPage(),
                new QueryWrapper<Clbum>().like(StrUtil.isNotBlank(clbumName), "clbum_name", clbumName)
        );
        return BaseResult.success(clbumPage);
    }

    /**
     * @Description 获取单条班级信息记录
     * @author dhxstart
     * @date 2021/6/16 17:02
     * @param clbumId 班级编号
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/info/{clbumId}")
    public BaseResult getProfessionInfo(@PathVariable(name = "clbumId") String clbumId) {
        Clbum clbum = clbumService.getOne(new QueryWrapper<Clbum>().eq("clbum_id", clbumId));
        return BaseResult.success(clbum);
    }

    /**
     * @Description 更新一条班级信息记录
     * @author dhxstart
     * @date 2021/6/16 17:02
     * @param clbum 班级实体
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @PostMapping("/update")
    public BaseResult updateProfession(@Validated @RequestBody Clbum clbum) {
        clbum.setUpdateTime(LocalDateTime.now());
        // 更新数据
        boolean flag = clbumService.updateById(clbum);
        return BaseResult.success(getCode(flag), getMsg(flag, "更新"), null);
    }
}

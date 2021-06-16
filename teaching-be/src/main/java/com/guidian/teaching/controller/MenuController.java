package com.guidian.teaching.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guidian.teaching.common.dto.SystemMenuDto;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.Menu;
import com.guidian.teaching.entity.User;
import com.guidian.teaching.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 菜单列表控制处理器
 * @author dhxstart
 * @date 2021/6/15 17:17
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController extends BaseController {
    @Autowired
    MenuService menuService;

    /**
     * @Description 用户当前用户的菜单和权限信息
     * @author dhxstart
     * @date 2021/6/15 10:22
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @GetMapping("/nav")
    public BaseResult getNav() {
        List<SystemMenuDto> navs = menuService.getCurrentUserNav();
        return BaseResult.success(MapUtil.builder()
                .put("nav", navs)
                .put("authoritys", null)
                .map()
        );
    }
}

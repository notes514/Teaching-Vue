package com.guidian.teaching.service;

import com.guidian.teaching.common.dto.SystemMenuDto;
import com.guidian.teaching.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description 此服务类用于实现针对于tb_menu表的常用操作
 * @author dhxstart
 * @date 2021/6/15 17:18
 */
public interface MenuService extends IService<Menu> {

    /**
     * @Description 根据当前用户登录获取菜单列表信息
     * @author dhxstart
     * @date 2021/6/15 12:20
     * @return java.util.List<com.guidian.teaching.common.dto.SystemMenuDto>
     */
    List<SystemMenuDto> getCurrentUserNav();
}

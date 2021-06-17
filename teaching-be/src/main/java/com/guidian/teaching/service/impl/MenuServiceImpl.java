package com.guidian.teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guidian.teaching.common.dto.SystemMenuDto;
import com.guidian.teaching.entity.Menu;
import com.guidian.teaching.entity.User;
import com.guidian.teaching.mapper.MenuMapper;
import com.guidian.teaching.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guidian.teaching.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 此服务实现类用于实现针对于tb_menu表的常用操作
 * @author dhxstart
 * @date 2021/6/15 17:18
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;

    @Override
    public List<SystemMenuDto> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        List<Menu> menus = menuService.list(new QueryWrapper<Menu>().eq("authority", user.getAuthority()));
        System.out.println("menus：" + menus);
        // 转树状结构
        List<Menu> menuTree = buildTreeMenu(menus);
        // 实体转DTO
        return convert(menuTree);
    }

    private List<Menu> buildTreeMenu(List<Menu> menus) {
        List<Menu> finalMenus = new ArrayList<>();
        // 先各自寻找到各自的孩子
        menus.forEach(menu -> {
            menus.forEach(e -> {
                if (menu.getMenuId().equals(e.getParentMenuId())) {
                    menu.getChildren().add(e);
                }
            });
            // 提取出父节点
            if (menu.getParentMenuId() == 0L) {
                finalMenus.add(menu);
            }
        });
        return finalMenus;
    }

    private List<SystemMenuDto> convert(List<Menu> menuTree) {
        List<SystemMenuDto> menuDtos = new ArrayList<>();

        menuTree.forEach(m -> {
            SystemMenuDto dto = new SystemMenuDto();
            dto.setMenuId(m.getMenuId());
            dto.setName(m.getMenuName());
            dto.setTitle(m.getMenuTitle());
            dto.setIcon(m.getIcon());
            dto.setPath(m.getMenuPath());
            dto.setComponent(m.getComponent());

            if (m.getChildren() != null) {
                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }
            menuDtos.add(dto);
        });
        return menuDtos;
    }
}

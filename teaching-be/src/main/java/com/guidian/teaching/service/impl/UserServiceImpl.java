package com.guidian.teaching.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guidian.teaching.entity.User;
import com.guidian.teaching.mapper.UserMapper;
import com.guidian.teaching.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 此服务实现类用于实现针对于tb_user表的常用操作
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByUsername(String username) {
        return getOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(String userId) {
        //User user = userMapper.selectById(userId);
        String authority = "";

        return null;
    //    SysUser sysUser = sysUserMapper.selectById(userId);
        //
        //		//  ROLE_admin,ROLE_normal,sys:user:list,....
        //		String authority = "";
        //
        //		if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
        //			authority = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());
        //
        //		} else {
        //			// 获取角色编码
        //			List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
        //					.inSql("id", "select role_id from sys_user_role where user_id = " + userId));
        //
        //			if (roles.size() > 0) {
        //				String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
        //				authority = roleCodes.concat(",");
        //			}
        //
        //			// 获取菜单操作编码
        //			List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
        //			if (menuIds.size() > 0) {
        //
        //				List<SysMenu> menus = sysMenuService.listByIds(menuIds);
        //				String menuPerms = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
        //
        //				authority = authority.concat(menuPerms);
        //			}
        //
        //			redisUtil.set("GrantedAuthority:" + sysUser.getUsername(), authority, 60 * 60);
        //		}
        //
        //		return authority;
    }
}

package com.guidian.teaching.service;

import com.guidian.teaching.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description 此服务类用于实现针对于tb_user表的常用操作
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
public interface UserService extends IService<User> {

    /**
     * @Description 根据用户昵称获取用户信息
     * @author dhxstart
     * @date 2021/6/14 16:56
     * @param username 用户昵称
     * @return com.guidian.teaching.entity.User
     */
    User getByUsername(String username);

    /**
     * @Description 获取user表中的权限信息
     * @author dhxstart
     * @date 2021/6/14 16:59
     * @param userId 用户编号
     * @return java.lang.String
     */
    String getUserAuthorityInfo(String userId);
}

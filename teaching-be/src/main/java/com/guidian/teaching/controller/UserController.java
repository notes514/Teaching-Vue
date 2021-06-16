package com.guidian.teaching.controller;

import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.controller.base.BaseController;
import com.guidian.teaching.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @Description 用户表控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:54
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @GetMapping("/userInfo")
    public BaseResult getUserInfo(Principal principal) {
        return BaseResult.success(userService.getByUsername(principal.getName()));
    }
}

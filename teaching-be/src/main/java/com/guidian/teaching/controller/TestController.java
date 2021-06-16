package com.guidian.teaching.controller;

import cn.hutool.core.map.MapUtil;
import com.guidian.teaching.common.dto.SystemMenuDto;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.entity.User;
import com.guidian.teaching.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @author dhxstart
 * @date 2021/6/13 18:14
 */
@RestController
public class TestController {
    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public BaseResult test() {
        return BaseResult.success(userService.list());
    }

    @GetMapping("/test/pass")
    public BaseResult passEncode() {   // 密码加密
        String pass = bCryptPasswordEncoder.encode("123456");
        // 密码验证
        boolean matches = bCryptPasswordEncoder.matches("123456", pass);

        System.out.println("匹配结果：" + matches);

        return BaseResult.success(
                MapUtil.builder()
                        .put("pass", pass)
                        .put("marches", matches)
                        .build());
    }
}

package com.guidian.teaching.controller.base;


import com.guidian.teaching.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 基类控制处理器
 * @author dhxstart
 * @date 2021/6/11 20:53
 */
public class BaseController {
    @Autowired
    public HttpServletRequest request;
    @Autowired
    public HttpServletResponse response;
    @Autowired
    public RedisUtils redisUtils;
}

package com.guidian.teaching.security;

import com.guidian.teaching.common.exception.CaptchaException;
import com.guidian.teaching.common.lang.Const;
import com.guidian.teaching.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description 此类是图片验证码校验过滤器，在登录过滤器前
 * @author dhxstart
 * @date 2021/6/12 17:13
 */
@Slf4j
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        if (url.equals(Const.LOGIN_URL) && httpServletRequest.getMethod().equals(Const.METHOD_POST)) {
            try {
                log.info("获取到login链接，正在校验验证码 -- " + url);
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                log.info(e.getMessage());
                // 交给认证失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * @Description 此方法用于验证码校验
     * @author dhxstart
     * @date 2021/6/12 17:24
     * @param httpServletRequest 请求头
     */
    private void validate(HttpServletRequest httpServletRequest) throws CaptchaException {
        String code = httpServletRequest.getParameter("code");
        String token = httpServletRequest.getParameter("token");

        if (StringUtils.isBlank(code) || StringUtils.isBlank(token)) {
            throw new CaptchaException("验证码错误！");
        }

        if (!code.equals(redisUtils.hget(Const.CAPTCHA_KEY, token))) {
            throw new CaptchaException("验证码错误！");
        }
        // 从redis中移除token，保证一次性登录的安全性
        redisUtils.hdel(Const.CAPTCHA_KEY, token);
    }
}
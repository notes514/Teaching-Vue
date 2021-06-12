package com.guidian.teaching.security;

import com.guidian.teaching.common.exception.CaptchaException;
import com.guidian.teaching.common.lang.Const;
import com.guidian.teaching.util.RedisUtils;
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
 * @Description 此类是验证码过滤器
 * @author dhxstart
 * @date 2021/6/12 17:13
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        if (Const.LOGIN_URL.equals(url) && Const.METHOD_POST.equals(httpServletRequest.getMethod())) {
            try {
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // 交给认证失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
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
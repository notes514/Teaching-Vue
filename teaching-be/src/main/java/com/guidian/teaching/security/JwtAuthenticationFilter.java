package com.guidian.teaching.security;

import cn.hutool.core.util.StrUtil;
import com.guidian.teaching.entity.User;
import com.guidian.teaching.service.UserService;
import com.guidian.teaching.util.JwtUtils;
import com.guidian.teaching.util.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TreeSet;

/**
 * @Description 此类是自定义过滤类，用于识别JWT
 * @author dhxstart
 * @date 2021/6/12 22:25
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    UserService userService;
    @Autowired
    UserDetailServiceImpl userDetailService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("jwt 校验 filter");
        String jwt = request.getHeader(jwtUtils.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimsByToken(jwt);
        if (claim == null) {
            throw new JwtException("token存在异常！");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token已失效，请重新登录！");
        }

        String username = claim.getSubject();
        log.info("用户-{}，正在登陆！", username);
        // 获取用户权限信息
        User user = userService.getByUsername(username);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(username, null,
                userDetailService.getUserAuthority(user.getUserId()));

        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }
}

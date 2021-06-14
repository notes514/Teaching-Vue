package com.guidian.teaching.security;

import cn.hutool.json.JSONUtil;
import com.guidian.teaching.common.lang.BaseResult;
import com.guidian.teaching.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Description 此类是用户登录认证成功处理器
 * @author dhxstart
 * @date 2021/6/12 17:02
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        // 生成jwt，并放置到请求头中
        String jwt = jwtUtils.generateToken(authentication.getName());
        httpServletResponse.setHeader(jwtUtils.getHeader(), jwt);

        BaseResult result = BaseResult.success("登录成功！");
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();
    }
}

package com.guidian.teaching.common.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @Description 验证码异常类
 * @author dhxstart
 * @date 2021/6/12 17:26
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String message) {
        super(message);
    }
}

package com.guidian.teaching.common.exception;

import com.guidian.teaching.common.lang.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description 此类用于全局异常处理
 * @author dhxstart
 * @date 2021/6/12 14:45
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @Description 实体校验异常捕获
     * @author dhxstart
     * @date 2021/6/20 23:06
     * @param e MethodArgumentNotValidException
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResult handler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        ObjectError objectError = result.getAllErrors().stream().findFirst().get();
        log.error("实体校验异常：", objectError.getDefaultMessage());
        return BaseResult.failure(objectError.getDefaultMessage());
    }

    /**
     * @Description Assert异常处理
     * @author dhxstart
     * @date 2021/6/12 14:50
     * @param e Assert异常
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public BaseResult handler(IllegalArgumentException e) {
        log.error("Assert异常：", e);
        return BaseResult.failure(e.getMessage());
    }

    /**
     * @Description 运行时异常处理
     * @author dhxstart
     * @date 2021/6/12 14:47
     * @param e 运行时异常
     * @return com.guidian.teaching.common.lang.BaseResult
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public BaseResult handler(RuntimeException e) {
        log.error("运行时异常：", e);
        return BaseResult.failure(e.getMessage());
    }
}

package com.example.partnermatchingcore.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有异常
    @ExceptionHandler(RuntimeException.class)
    public R<?> handleRuntimeException(RuntimeException e) {
        // 控制台打印完整异常栈，精准定位问题
        log.error("系统异常：", e);
        return R.fail(e.getMessage());
    }

    // 捕获所有其他异常
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        log.error("未知异常：", e);
        return R.fail("系统异常，请联系管理员");
    }
}

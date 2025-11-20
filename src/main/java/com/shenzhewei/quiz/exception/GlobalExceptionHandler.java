package com.shenzhewei.quiz.exception;

import com.shenzhewei.quiz.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandle(Exception ex){
        log.error("发生异常:", ex);
        return Result.error("操作失败，请联系管理员！");
    }
}

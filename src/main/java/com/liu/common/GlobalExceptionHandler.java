package com.liu.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionHandler {
    /**
     * sql唯一性异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.error(e.getMessage());

        if(e.getMessage().contains("Duplicate entry")){
            String[] split = e.getMessage().split(" ");
            String msg=split[2]+"已存在";
            return Result.error(msg);
        }

        return Result.error("未知错误");
    }


    /**
     * 分类删除业务异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());

        return Result.error(ex.getMessage());
    }
}

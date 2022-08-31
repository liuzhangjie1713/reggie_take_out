package com.liu.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    //插入时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】。。。");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Long id = (Long) request.getSession().getAttribute("employee");
        if(id == null){
             id = (Long) request.getSession().getAttribute("user");
        }
        metaObject.setValue("createUser",id);
        metaObject.setValue("updateUser",id);
    }

    //更新时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】。。。");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime",LocalDateTime.now());
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Long id = (Long) request.getSession().getAttribute("employee");
        if(id == null){
            id = (Long) request.getSession().getAttribute("user");
        }
        metaObject.setValue("updateUser",id);
    }
}


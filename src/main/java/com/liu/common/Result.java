package com.liu.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果类
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;//编码：1成功，其他失败

    private String msg;//错误信息

    private T data;//数据

    private Map map = new HashMap();//动态数据

    public static <T> Result<T> success(T object){
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg){
        Result<T> result = new Result<T>();
        result.msg = msg;
        result.code = 0;
        return result;
    }

    public Result<T> add(String key, Object value){
        this.map.put(key, value);
        return this;
    }



}

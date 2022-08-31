package com.liu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

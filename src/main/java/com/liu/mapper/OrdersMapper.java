package com.liu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liu.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}

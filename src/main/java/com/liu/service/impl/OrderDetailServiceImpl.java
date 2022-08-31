package com.liu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.entity.OrderDetail;
import com.liu.mapper.OrderDetailMapper;
import com.liu.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
        implements OrderDetailService {
}

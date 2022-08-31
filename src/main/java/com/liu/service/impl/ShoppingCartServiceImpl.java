package com.liu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liu.entity.ShoppingCart;
import com.liu.mapper.ShoppingCartMapper;
import com.liu.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {
}

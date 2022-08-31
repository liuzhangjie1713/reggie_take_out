package com.liu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liu.entity.OrderDetail;
import com.liu.entity.Orders;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface OrdersService extends IService<Orders> {

    public void submit(Orders orders, HttpSession session);

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId);
}
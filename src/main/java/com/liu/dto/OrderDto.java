package com.liu.dto;

import com.liu.entity.OrderDetail;
import com.liu.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}

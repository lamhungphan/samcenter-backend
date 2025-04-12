package com.samcenter.service;

import com.samcenter.controller.request.OrderDetailRequest;
import com.samcenter.entity.OrderDetail;

public interface OrderDetailService extends BaseService<OrderDetail, Integer, OrderDetailRequest> {
    OrderDetail createOrderDetail(OrderDetailRequest request);
}

package com.samcenter.service;

import com.samcenter.controller.request.OrderRequest;
import com.samcenter.controller.response.OrderResponse;
import com.samcenter.entity.Order;

import java.util.List;

public interface OrderService extends BaseService<Order, Integer, OrderRequest> {
    public OrderResponse processPaymentAndCreateOrder(OrderRequest request);
    List<Order> getOrdersByUserId(Integer userId);
}

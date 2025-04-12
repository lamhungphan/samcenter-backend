package com.samcenter.service.impl;

import com.samcenter.controller.request.OrderDetailRequest;
import com.samcenter.entity.Order;
import com.samcenter.entity.OrderDetail;
import com.samcenter.entity.Product;
import com.samcenter.repository.OrderDetailRepository;
import com.samcenter.repository.OrderRepository;
import com.samcenter.repository.ProductRepository;
import com.samcenter.service.AbstractService;
import com.samcenter.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderDetailServiceImpl extends AbstractService<OrderDetail, Integer, OrderDetailRequest> implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        super(orderDetailRepository);
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    @Override
    public void update(OrderDetailRequest request) {
    }

    @Transactional
    @Override
    public OrderDetail createOrderDetail(OrderDetailRequest request) {
        log.info("Creating OrderDetail with orderId: {}", request.getOrderId()); // Kiểm tra orderId

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + request.getProductId()));

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order); // Gán Order trực tiếp
        orderDetail.setProduct(product);
        orderDetail.setQuantity(request.getQuantity());
        orderDetail.setPrice(request.getPrice());

        return orderDetailRepository.save(orderDetail);
    }

}

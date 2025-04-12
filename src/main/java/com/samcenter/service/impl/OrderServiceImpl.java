package com.samcenter.service.impl;

import com.samcenter.controller.request.OrderDetailRequest;
import com.samcenter.controller.request.OrderRequest;
import com.samcenter.controller.response.OrderResponse;
import com.samcenter.entity.Order;
import com.samcenter.entity.OrderDetail;
import com.samcenter.entity.OrderStatus;
import com.samcenter.entity.Product;
import com.samcenter.mapper.OrderMapper;
import com.samcenter.repository.OrderRepository;
import com.samcenter.repository.ProductRepository;
import com.samcenter.service.AbstractService;
import com.samcenter.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends AbstractService<Order, Integer, OrderRequest> implements OrderService {
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderServiceImpl(JpaRepository<Order, Integer> repository,
                            OrderMapper orderMapper,
                            ProductRepository productRepository,
                            OrderRepository orderRepository) {
        super(repository);
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void update(OrderRequest request) {

    }

    @Transactional
    public Order createOrderFromPayment(OrderRequest request) {
        Order order = orderMapper.toOrder(request);
        order.setStatus(OrderStatus.PENDING);
        return save(order);
    }

    @Transactional
    public OrderResponse processPaymentAndCreateOrder(OrderRequest request) {
        Order order = orderMapper.toOrder(request);
        order.setStatus(OrderStatus.PENDING);

        List<OrderDetailRequest> detailRequests = request.getOrderDetails();
        if (detailRequests == null || detailRequests.isEmpty()) {
            throw new RuntimeException("Order must have at least one detail");
        }

        double totalPrice = detailRequests.stream()
                .mapToDouble(detail -> detail.getPrice() * detail.getQuantity())
                .sum();
        order.setTotalPrice(totalPrice);

        // Tạo danh sách OrderDetail và gán trực tiếp vào Order
        List<OrderDetail> orderDetails = detailRequests.stream().map(detail -> {
            Product product = productRepository.findById(detail.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + detail.getProductId()));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order); // Gán order trực tiếp
            orderDetail.setProduct(product);
            orderDetail.setQuantity(detail.getQuantity());
            orderDetail.setPrice(detail.getPrice());

            return orderDetail;
        }).collect(Collectors.toList());

        order.setOrderDetails(orderDetails);

        // Lưu Order và toàn bộ OrderDetail nhờ cascade = ALL
        Order savedOrder = orderRepository.save(order);
        log.info("Saved order with ID: {}", savedOrder.getId());

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }
}

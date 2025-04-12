package com.samcenter.repository;

import com.samcenter.entity.Order;
import com.samcenter.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);
    List<Order> findByUser_IdOrderByOrderDateDesc(Integer userId);
    List<Order> findByUser_IdAndStatus(Integer userId, OrderStatus status);
}

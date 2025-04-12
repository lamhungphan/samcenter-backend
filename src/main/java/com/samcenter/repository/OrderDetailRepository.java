package com.samcenter.repository;

import com.samcenter.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrder_Id(Integer orderId);
    List<OrderDetail> findByProduct_Id(Integer productId);
}

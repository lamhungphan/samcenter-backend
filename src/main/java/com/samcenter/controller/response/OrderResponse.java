package com.samcenter.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.samcenter.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Integer id;
    private AccountResponse user;
    private Double totalPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderDetailResponse> orderDetails;
}

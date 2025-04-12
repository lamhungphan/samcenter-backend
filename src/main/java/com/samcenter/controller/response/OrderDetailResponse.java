package com.samcenter.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {

    private Integer id;
    private Integer orderId;
    private ProductResponse product;
    private Integer quantity;
    private Double price;
}

package com.samcenter.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRequest {

    private Integer orderId;

    @NotNull
    private Integer productId;

    @Positive
    private Integer quantity;

    @Positive
    private Double price;
}

package com.samcenter.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
}

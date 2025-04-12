package com.samcenter.mapper;

import com.samcenter.controller.request.CartRequest;
import com.samcenter.controller.response.CartResponse;
import com.samcenter.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "productId", target = "product.id")
    Cart toCart(CartRequest request);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    CartResponse toCartResponse(Cart cart);
}

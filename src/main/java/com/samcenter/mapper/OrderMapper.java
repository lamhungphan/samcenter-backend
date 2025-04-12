package com.samcenter.mapper;

import com.samcenter.controller.request.OrderRequest;
import com.samcenter.controller.response.OrderResponse;
import com.samcenter.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "userId", target = "user.id")
    Order toOrder(OrderRequest request);

    @Mapping(source = "id", target = "id")
    OrderResponse toOrderResponse(Order order);

    void updateOrder(@MappingTarget Order order, OrderRequest orderRequest);
}

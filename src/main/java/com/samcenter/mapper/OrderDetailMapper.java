package com.samcenter.mapper;

import com.samcenter.controller.request.OrderDetailRequest;
import com.samcenter.controller.response.OrderDetailResponse;
import com.samcenter.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "productId", target = "product.id")
    OrderDetail toOrderDetail(OrderDetailRequest request);

    OrderDetailResponse toOrderDetailResponse(OrderDetail orderDetail);
}

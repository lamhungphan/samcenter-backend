package com.samcenter.controller;

import com.samcenter.controller.request.OrderDetailRequest;
import com.samcenter.controller.response.ApiResponse;
import com.samcenter.controller.response.OrderDetailResponse;
import com.samcenter.controller.response.PageResponse;
import com.samcenter.entity.OrderDetail;
import com.samcenter.mapper.OrderDetailMapper;
import com.samcenter.service.OrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order-detail")
@RequiredArgsConstructor
@Tag(name = "Order Detail Controller")
@Slf4j(topic = "ORDER-DETAIL-CONTROLLER")
@Validated
public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final OrderDetailMapper orderDetailMapper;

    @Operation(summary = "Get Order Detail List", description = "API retrieve order details from database")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderDetailResponse>>> getAllOrderDetails(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("get All Order Details");

        Page<OrderDetail> orderDetails = orderDetailService.getAll(keyword, sort, page, size);
        Page<OrderDetailResponse> response = orderDetails.map(orderDetailMapper::toOrderDetailResponse);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(response),"Order-detail list retrieved successfully"));
    }

    @Operation(summary = "Get Order Detail", description = "API retrieve order detail by ID from database")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(@PathVariable Integer id) {
        log.info("Fetching order detail with ID: {}", id);

        OrderDetail orderDetail = orderDetailService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(orderDetailMapper.toOrderDetailResponse(orderDetail), "Order detail retrieved successfully"));
    }

//    @Operation(summary = "Create Order Detail", description = "API add new order detail to database")
//    @PostMapping
//    public ResponseEntity<ApiResponse<OrderDetailResponse>> createOrderDetail(@Valid @RequestBody OrderDetailRequest request) {
//        log.info("Creating new order detail for order ID: {}, product ID: {}, quantity: {}",
//                request.getOrderId(), request.getProductId(), request.getQuantity());
//        OrderDetail savedOrderDetail = orderDetailService.createOrderDetail(request);
//        return ResponseEntity.ok(ApiResponse.success(orderDetailMapper.toOrderDetailResponse(savedOrderDetail), "Order detail created successfully"));
//    }

    @Operation(summary = "Update Order Detail", description = "API update order detail in database")
    @PreAuthorize("hasRole('DIRECTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateOrderDetail(@PathVariable Integer id, @Valid @RequestBody OrderDetailRequest request) {
        log.info("Updating order detail with ID: {}", id);
        OrderDetail orderDetail = orderDetailService.getById(id);
        orderDetail.setQuantity(request.getQuantity());
        orderDetail.setPrice(request.getPrice());
        orderDetailService.save(orderDetail);
        return ResponseEntity.ok(ApiResponse.success(null, "Order detail updated successfully"));
    }

//    @Operation(summary = "Delete Order Detail", description = "API delete order detail from database")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<Void>> deleteOrderDetail(@PathVariable Integer id) {
//        log.info("Deleting order detail with ID: {}", id);
//
//        orderDetailService.delete(id);
//        return ResponseEntity.ok(ApiResponse.success(null, "Order detail deleted successfully"));
//    }
}
package com.samcenter.controller;

import com.samcenter.controller.request.OrderRequest;
import com.samcenter.controller.response.ApiResponse;
import com.samcenter.controller.response.OrderResponse;
import com.samcenter.controller.response.PageResponse;
import com.samcenter.entity.Order;
import com.samcenter.mapper.OrderMapper;
import com.samcenter.service.OrderService;
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

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "Order Controller")
@Slf4j(topic = "ORDER-CONTROLLER")
@Validated
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Operation(summary = "Get Order List", description = "API retrieve order from database")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getAllOrder(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("get All Orders");
        Page<Order> order = orderService.getAll(keyword, sort, page, size);
        Page<OrderResponse> response = order.map(orderMapper::toOrderResponse);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(response), "Order list retrieved successfully"));
    }

    @Operation(summary = "Get Order Detail", description = "API retrieve order detail by ID from database")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Integer id) {
        log.info("get order");
        Order order = orderService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(orderMapper.toOrderResponse(order), "Order retrieved successfully"));
    }

    @Operation(summary = "Get Orders by User ID", description = "API to retrieve orders by user ID from database")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByUser(@PathVariable Integer userId) {
        log.info("Get orders by userId: {}", userId);
        List<Order> orders = orderService.getOrdersByUserId(userId);
        List<OrderResponse> response = orders.stream()
                .map(orderMapper::toOrderResponse)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(response, "Orders by user retrieved successfully"));
    }

    @Operation(summary = "Create Order from Payment", description = "API to create a new order with details after successful payment")
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest request) {
        log.info("Creating order for user ID: {}", request.getUserId());
        OrderResponse orderResponse = orderService.processPaymentAndCreateOrder(request);
        return ResponseEntity.ok(ApiResponse.success(orderResponse, "Order created successfully after payment"));
    }

    @Operation(summary = "Update Order", description = "API update order in database")
    @PreAuthorize("hasRole('DIRECTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateOrder(@PathVariable Integer id, @RequestBody OrderRequest request) {
        log.info("update order");
        Order order = orderService.getById(id);
        orderMapper.updateOrder(order, request);
        orderService.save(order);
        return ResponseEntity.ok(ApiResponse.success(null, "Order updated successfully"));
    }

    @Operation(summary = "Delete Order", description = "API delete order from database")
    @PreAuthorize("hasRole('DIRECTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Integer id) {
        log.info("delete order");
        orderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Order deleted successfully"));
    }

}

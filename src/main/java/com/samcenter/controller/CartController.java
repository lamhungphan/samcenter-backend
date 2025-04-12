package com.samcenter.controller;

import com.samcenter.controller.request.CartRequest;
import com.samcenter.controller.response.ApiResponse;
import com.samcenter.controller.response.CartResponse;
import com.samcenter.entity.Cart;
import com.samcenter.mapper.CartMapper;
import com.samcenter.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Controller")
@Slf4j(topic = "CART-CONTROLLER")
@Validated
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @Operation(summary = "Get Cart", description = "Retrieve cart for a user")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartResponse>>> getCart(@PathVariable Integer userId) {
        log.info("get cart for user {}", userId);

        List<Cart> cartItems = cartService.getByUserId(userId);
        List<CartResponse> response = cartItems.stream()
                .map(cartMapper::toCartResponse)
                .toList();

        return ResponseEntity.ok(ApiResponse.success(response, "Cart retrieved successfully"));
    }

    @Operation(summary = "Add Item to Cart", description = "Add a product to the user's cart")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody CartRequest request) {
        log.info("add product {} to cart of user {}", request.getProductId(), request.getUserId());

        Cart cart = cartService.addToCart(request);
        return ResponseEntity.ok(ApiResponse.success(cartMapper.toCartResponse(cart), "Product added to cart successfully"));
    }

    @Operation(summary = "Update Item Quantity in Cart", description = "Update the quantity of a product in the user's cart")
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartResponse>> updateCart(@Valid @RequestBody CartRequest request) {
        log.info("Update product {} quantity to {} in cart of user {}",
                request.getProductId(), request.getQuantity(), request.getUserId());

        Cart cart = cartService.updateCart(request);
        return ResponseEntity.ok(ApiResponse.success(cartMapper.toCartResponse(cart), "Cart updated successfully"));
    }

    @Operation(summary = "Remove Item from Cart", description = "Remove a product from the user's cart")
    @DeleteMapping("/remove/{userId}/{productId}")
    public ResponseEntity<ApiResponse<Void>> removeFromCart(@PathVariable Integer userId, @PathVariable Integer productId) {
        log.info("remove product {} from cart of user {}", productId, userId);

        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(null, "Product removed from cart successfully"));
    }

    @Operation(summary = "Clear Cart", description = "Remove all products from the user's cart")
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Integer userId) {
        log.info("clear cart for user {}", userId);

        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Cart cleared successfully"));
    }
}

package com.samcenter.unit;

import com.samcenter.controller.request.CartRequest;
import com.samcenter.entity.Cart;
import com.samcenter.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {

    @Autowired
    private CartService cartService;

    private final Integer userId = 2;
    private final Integer productId = 20;

    @Test
    void testAddToCart_ProductInStock() {
        CartRequest request = new CartRequest(userId, productId, 2);
        Cart result = cartService.addToCart(request);
        assertNotNull(result);
        assertEquals(2, result.getQuantity());
    }

    @Test
    void testAddSameProductMultipleTimes() {
        CartRequest request = new CartRequest(userId, productId, 1);
        cartService.addToCart(request);
        Cart updated = cartService.addToCart(request);
        assertTrue(updated.getQuantity() >= 2);
    }

    @Test
    void testRemoveProductFromCart() {
        CartRequest request = new CartRequest(userId, productId, 1);
        cartService.addToCart(request);
        cartService.removeFromCart(userId, productId);
        List<Cart> carts = cartService.getByUserId(userId);
        boolean exists = carts.stream().anyMatch(c -> c.getProduct().getId().equals(productId));
        assertFalse(exists);
    }

    @Test
    void testRemoveProductTwice() {
        CartRequest request = new CartRequest(userId, productId, 1);
        cartService.addToCart(request);
        cartService.removeFromCart(userId, productId);
        assertThrows(RuntimeException.class, () -> {
            cartService.removeFromCart(userId, productId);
        });
    }

    @Test
    void testClearCart() {
        cartService.clearCart(userId);
        List<Cart> carts = cartService.getByUserId(userId);
        assertEquals(0, carts.size());
    }
}


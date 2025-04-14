package com.samcenter.unit;

import com.samcenter.controller.request.CartRequest;
import com.samcenter.entity.Account;
import com.samcenter.entity.Cart;
import com.samcenter.entity.Product;
import com.samcenter.exception.ResourceNotFoundException;
import com.samcenter.repository.AccountRepository;
import com.samcenter.repository.CartRepository;
import com.samcenter.repository.ProductRepository;
import com.samcenter.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private final Integer userId = 2;
    private final Integer productId = 20;

    @Mock
    private Account mockUser;

    @Mock
    private Product mockProduct;

    @Mock
    private Cart mockCartItem;

    @BeforeEach
    void setUp() {
        // Mocking the user and product repositories
        when(accountRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
    }

//    @Test
//    void testAddToCart_ProductInStock() {
//        CartRequest request = new CartRequest(userId, productId, 2);
//
//        // When no existing cart item for user and product, create a new one
//        when(cartRepository.findByUserAndProduct(mockUser, mockProduct)).thenReturn(Optional.empty());
//        when(cartRepository.save(any(Cart.class))).thenReturn(mockCartItem);
//
//        Cart result = cartService.addToCart(request);
//
//        assertNotNull(result);
//        assertEquals(2, result.getQuantity());  // Mock cartItem should have 2 quantity
//        verify(cartRepository, times(1)).save(any(Cart.class)); // Verifying that save was called
//    }
//
//    @Test
//    void testAddSameProductMultipleTimes() {
//        CartRequest request = new CartRequest(userId, productId, 1);
//
//        // When cart item already exists, increase quantity
//        when(cartRepository.findByUserAndProduct(mockUser, mockProduct)).thenReturn(Optional.of(mockCartItem));
//        when(cartRepository.save(any(Cart.class))).thenReturn(mockCartItem);
//
//        Cart updated = cartService.addToCart(request);
//
//        assertNotNull(updated);
//        assertTrue(updated.getQuantity() >= 2);  // Ensure quantity has increased
//        verify(cartRepository, times(1)).save(any(Cart.class)); // Verifying save was called
//    }

    @Test
    void testRemoveProductFromCart() {
        CartRequest request = new CartRequest(userId, productId, 1);

        // Mock cart item already exists in the cart
        when(cartRepository.findByUserAndProduct(mockUser, mockProduct)).thenReturn(Optional.of(mockCartItem));

        // Call addToCart to ensure cart is updated
        cartService.addToCart(request);

        // Mock delete behavior
        doNothing().when(cartRepository).delete(mockCartItem);

        // Perform removal of the product from the cart
        cartService.removeFromCart(userId, productId);

        // Ensure delete was called
        verify(cartRepository, times(1)).delete(mockCartItem);
    }

    @Test
    void testRemoveProductTwice() {
        CartRequest request = new CartRequest(userId, productId, 1);

        // Mocking existing cart item
        when(cartRepository.findByUserAndProduct(mockUser, mockProduct)).thenReturn(Optional.of(mockCartItem));
        doNothing().when(cartRepository).delete(mockCartItem);

        cartService.addToCart(request);
        cartService.removeFromCart(userId, productId);

        // Verify if calling remove again throws the expected exception
        when(cartRepository.findByUserAndProduct(mockUser, mockProduct)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            cartService.removeFromCart(userId, productId);
        });
    }

    @Test
    void testClearCart() {
        // Simulating cart containing items
        when(cartRepository.findByUser(mockUser)).thenReturn(Arrays.asList(mockCartItem));

        // Mock the deleteAll behavior
        doNothing().when(cartRepository).deleteAll(anyList());

        cartService.clearCart(userId);

        // Verifying if deleteAll was called
        verify(cartRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void testGetCartItems_Empty() {
        when(cartRepository.findByUser(mockUser)).thenReturn(Collections.emptyList());

        List<Cart> cartItems = cartService.getCartItems(mockUser);

        assertTrue(cartItems.isEmpty());
    }

    @Test
    void testGetCartItems_NotEmpty() {
        when(cartRepository.findByUser(mockUser)).thenReturn(Collections.singletonList(mockCartItem));

        List<Cart> cartItems = cartService.getCartItems(mockUser);

        assertFalse(cartItems.isEmpty());
        assertEquals(1, cartItems.size());
    }
}





package com.samcenter.service.impl;

import com.samcenter.controller.request.CartRequest;
import com.samcenter.entity.Account;
import com.samcenter.entity.Cart;
import com.samcenter.entity.Product;
import com.samcenter.exception.ResourceNotFoundException;
import com.samcenter.repository.AccountRepository;
import com.samcenter.repository.CartRepository;
import com.samcenter.repository.ProductRepository;
import com.samcenter.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<Cart> getCartItems(Account user) {
        log.info("Fetching cart items for user: {}", user.getId());
        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            log.warn("No cart found for user {}", user.getId());
        }

        log.debug("Found {} cart items for user: {}", cartItems.size(), user.getId());
        return cartItems;
    }

    @Override
    public Cart updateCart(CartRequest request) {
        log.info("Updating cart with request: userId={}, productId={}, quantity={}",
                request.getUserId(), request.getProductId(), request.getQuantity());

        Account user = accountRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", request.getUserId());
                    return new ResourceNotFoundException("User not found");
                });

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", request.getProductId());
                    return new ResourceNotFoundException("Product not found");
                });

        Cart cartItem = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> {
                    log.error("Cart item not found for user {} and product {}", request.getUserId(), request.getProductId());
                    return new ResourceNotFoundException("Cart item not found");
                });

        cartItem.setQuantity(request.getQuantity());
        Cart updatedCart = cartRepository.save(cartItem);  // Save the updated cart item
        log.info("Updated cart item, new quantity: {}", updatedCart.getQuantity());

        return updatedCart;
    }

    @Override
    @Transactional
    public void removeFromCart(Integer userId, Integer productId) {
        log.info("Removing product {} from cart of user {}", productId, userId);
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", productId);
                    return new ResourceNotFoundException("Product not found");
                });

        Cart cartItem = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> {
                    log.error("Cart item not found for user {} and product {}", userId, productId);
                    return new ResourceNotFoundException("Cart item not found");
                });

        cartRepository.delete(cartItem);
        log.info("Removed product {} from cart of user {}", productId, userId);
    }

    @Override
    public List<Cart> getByUserId(Integer userId) {
        log.info("Fetching cart for user {}", userId);
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        List<Cart> cart = cartRepository.findByUser(user);
        log.debug("Found cart for user {}: {}", userId, cart);
        return cart;
    }

    @Override
    @Transactional
    public Cart addToCart(CartRequest request) {
        log.info("Adding to cart with request: userId={}, productId={}, quantity={}",
                request.getUserId(), request.getProductId(), request.getQuantity());
        Account user = accountRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", request.getUserId());
                    return new ResourceNotFoundException("User not found");
                });

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", request.getProductId());
                    return new ResourceNotFoundException("Product not found");
                });

        Cart cartItem = cartRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    log.info("Creating new cart item for user {} and product {}",
                            request.getUserId(), request.getProductId());
                    return new Cart(user, product, 0);
                });

        cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        Cart savedCart = cartRepository.save(cartItem);
        log.info("Added to cart, new quantity: {}", savedCart.getQuantity());
        return savedCart;
    }

    @Override
    @Transactional
    public void clearCart(Integer userId) {
        log.info("Clearing cart for user {}", userId);
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Can't found user with ID: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        List<Cart> cartItems = cartRepository.findByUser(user);
        if (!cartItems.isEmpty()) {
            cartRepository.deleteAll(cartItems);
            log.info("Cleared {} cart items for user {}", cartItems.size(), userId);
        } else {
            log.warn("No cart items found to clear for user {}", userId);
        }
    }
}

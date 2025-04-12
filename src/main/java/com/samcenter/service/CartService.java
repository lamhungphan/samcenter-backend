package com.samcenter.service;

import com.samcenter.controller.request.CartRequest;
import com.samcenter.entity.Account;
import com.samcenter.entity.Cart;

import java.util.List;

public interface CartService {
    public List<Cart> getCartItems(Account user);

    public Cart updateCart(CartRequest request);

    List<Cart> getByUserId(Integer userId);

    Cart addToCart(CartRequest request);

    void clearCart(Integer userId);

    void removeFromCart(Integer userId, Integer productId);
}

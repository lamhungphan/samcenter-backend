package com.samcenter.repository;

import com.samcenter.entity.Account;
import com.samcenter.entity.Cart;
import com.samcenter.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findByUser(Account user);

    Optional<Cart> findByUserAndProduct(Account user, Product product);

    void deleteByUser(Account user);

    Optional<Cart> findByUserIdAndProductId(Integer userId, Integer productId);
}

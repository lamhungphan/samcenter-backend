package com.samcenter.repository;

import com.samcenter.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "FROM Product u WHERE lower(u.name) LIKE :keyword OR lower(u.description) LIKE :keyword " +
            "OR lower(u.category.name) LIKE :keyword ")
    Page<Product> findByKeyword(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    Page<Product> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);
}

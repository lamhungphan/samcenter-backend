package com.samcenter.controller;

import com.samcenter.controller.request.ProductRequest;
import com.samcenter.controller.response.ApiResponse;
import com.samcenter.controller.response.PageResponse;
import com.samcenter.controller.response.ProductResponse;
import com.samcenter.entity.Product;
import com.samcenter.mapper.ProductMapper;
import com.samcenter.service.ProductService;
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
@RequestMapping("/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Product Controller")
@Slf4j(topic = "PRODUCT-CONTROLLER")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Get product list", description = "API retrieve product from database")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Get product list");

        Page<Product> products = productService.getAll(keyword, sort, page, size);
        Page<ProductResponse> respPage = products.map(productMapper::toProductResponse);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(respPage), "Product list retrieved successfully"));
    }

    @Operation(summary = "Get product detail", description = "API retrieve product detail by ID from database")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetail(@PathVariable Integer id) {
        log.info("Fetching product detail with ID: {}", id);

        Product product = productService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(productMapper.toProductResponse(product), "Product retrieved successfully"));
    }

    @Operation(summary = "Create product", description = "API add new product to database")
    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("Creating new product: {}", request);

        Product product = productMapper.toProduct(request);
        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(ApiResponse.success(productMapper.toProductResponse(savedProduct), "Product created successfully"));
    }

    @Operation(summary = "Update product", description = "API update product in database")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateProduct(@PathVariable Integer id, @RequestBody ProductRequest request) {
        log.info("Updating product with ID: {}", id);

        Product product = productService.getById(id);
        productMapper.updateProduct(product, request);
        productService.save(product);
        return ResponseEntity.ok(ApiResponse.success(null, "Product updated successfully"));
    }

    @Operation(summary = "Delete product", description = "API delete product from database")
    @PreAuthorize("hasRole('DIRECTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        log.info("Deleting product with ID: {}", id);

        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    @Operation(summary = "Find products by category ID", description = "API to retrieve products by category with pagination and sorting")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Get products by categoryId: {}", categoryId);

        Page<Product> products = productService.getProductByCategoryId(categoryId, sort, page, size);
        Page<ProductResponse> respPage = products.map(productMapper::toProductResponse);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(respPage), "Product list by category retrieved successfully"));
    }

    @Operation(summary = "Search products by keyword", description = "API to search products by keyword in name, description or category")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Search products with keyword: {}", keyword);

        Page<Product> products = productService.getProductByKeyword(keyword, sort, page, size);
        Page<ProductResponse> respPage = products.map(productMapper::toProductResponse);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(respPage), "Product search completed successfully"));
    }
}

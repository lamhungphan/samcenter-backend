package com.samcenter.controller;

import com.samcenter.controller.request.CategoryRequest;
import com.samcenter.controller.response.ApiResponse;
import com.samcenter.controller.response.CategoryResponse;
import com.samcenter.controller.response.PageResponse;
import com.samcenter.entity.Category;
import com.samcenter.mapper.CategoryMapper;
import com.samcenter.service.CategoryService;
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
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller")
@Slf4j(topic = "CATEGORY-CONTROLLER")
@Validated
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Get category list", description = "API retrieve category from database")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> getAllCategories(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("getAllCategories");

        Page<Category> categories = categoryService.getAll(keyword, sort, page, size);
        Page<CategoryResponse> response = categories.map(categoryMapper::toCategoryResponse);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(response), "Category list retrieved successfully"));
    }

    @Operation(summary = "Get category detail", description = "API retrieve category detail by ID from database")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(@PathVariable Integer id) {
        log.info("getCategory");

        Category category = categoryService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toCategoryResponse(category), "Category retrieved successfully"));
    }

    @Operation(summary = "Create category", description = "API add new category to database")
    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        log.info("createCategory");

        Category category = categoryMapper.toCategory(request);
        Category savedCategory = categoryService.save(category);
        return ResponseEntity.ok(ApiResponse.success(categoryMapper.toCategoryResponse(savedCategory), "Category created successfully"));
    }

    @Operation(summary = "Update category", description = "API update category to database")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('STAFF')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest request) {
        log.info("updateCategory");

        Category category = categoryService.getById(id);
        categoryMapper.updateCategory(category, request);
        categoryService.save(category);
        return ResponseEntity.ok(ApiResponse.success(null, "Category updated successfully"));
    }

    @Operation(summary = "Delete category", description = "API delete category from database")
    @PreAuthorize("hasRole('DIRECTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Integer id) {
        log.info("deleteCategory");

        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Category deleted successfully"));
    }
}

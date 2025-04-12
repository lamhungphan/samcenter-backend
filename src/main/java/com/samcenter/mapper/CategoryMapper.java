package com.samcenter.mapper;

import com.samcenter.controller.request.CategoryRequest;
import com.samcenter.controller.response.CategoryResponse;
import com.samcenter.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);

    @Mapping(target = "products", ignore = true)
    void updateCategory(@MappingTarget Category category, CategoryRequest request);
}


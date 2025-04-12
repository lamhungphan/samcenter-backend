package com.samcenter.service.impl;

import com.samcenter.controller.request.CategoryRequest;
import com.samcenter.entity.Category;
import com.samcenter.mapper.CategoryMapper;
import com.samcenter.repository.CategoryRepository;
import com.samcenter.service.AbstractService;
import com.samcenter.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends AbstractService<Category, Integer, CategoryRequest> implements CategoryService {
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository repository, CategoryMapper categoryMapper) {
        super(repository);
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void update(CategoryRequest  request) {
        Category category = getById(request.getId());
        categoryMapper.updateCategory(category, request);
        save(category);
    }
}

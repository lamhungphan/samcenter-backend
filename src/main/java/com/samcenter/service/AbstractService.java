package com.samcenter.service;

import com.samcenter.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class AbstractService<T, ID, RQ> implements BaseService<T, ID, RQ> {

    protected final JpaRepository<T, ID> repository;

    public AbstractService(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Page<T> getAll(String keyword, String sort, int page, int size) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("^([a-zA-Z]+)(asc|desc)?$");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                Sort.Direction direction = matcher.group(2) != null && matcher.group(2).equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;
                order = new Sort.Order(direction, columnName);
            }
        }
        Pageable pageable = PageRequest.of(Math.max(page - 1, 0), size, Sort.by(order));
        return repository.findAll(pageable);
    }

    @Override
    public T getById(ID id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Transactional
    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Transactional
    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }
}

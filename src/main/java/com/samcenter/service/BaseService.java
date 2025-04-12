package com.samcenter.service;

import org.springframework.data.domain.Page;

public interface BaseService<T, ID, RQ> {
    Page<T> getAll(String keyword, String sort, int page, int size);

    T getById(ID id);

    T save(T request);

    void update(RQ request);

    void delete(ID id);
}

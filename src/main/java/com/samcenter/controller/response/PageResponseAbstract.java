package com.samcenter.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public abstract class PageResponseAbstract implements Serializable {
    public int pageNumber;
    public int pageSize;
    public long totalPages;
    public long totalElements;
}

package com.samcenter.controller.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> extends PageResponseAbstract {
    private List<T> content;

    public PageResponse(Page<T> page) {
        super(page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
        this.content = page.getContent();
    }
}

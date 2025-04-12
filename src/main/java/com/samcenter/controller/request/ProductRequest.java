package com.samcenter.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private int quantity;
    private String size;
    private String description;
    private String image;
    private Double price;
    private LocalDate publishDate;
    private Integer categoryId;
}

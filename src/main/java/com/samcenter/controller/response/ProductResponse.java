package com.samcenter.controller.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private Integer id;
    private String name;
    private int quantity;
    private String size;
    private String description;
    private String image;
    private Double price;
    private LocalDate publishDate;
    private Integer categoryId;
}

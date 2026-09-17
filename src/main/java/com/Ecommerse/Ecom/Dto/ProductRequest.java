package com.Ecommerse.Ecom.Dto;

import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    private String description;
    private String price;
    private Integer stockQuantity;
    private String category;
    private String imageUrl;

}

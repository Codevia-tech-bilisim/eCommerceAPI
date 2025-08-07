package com.huseyinsen.dto.product;

import com.huseyinsen.dto.category.CategoryResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String sku;
    private Integer stockQuantity;
    private List<String> imageUrls;
    private CategoryResponse category;
}
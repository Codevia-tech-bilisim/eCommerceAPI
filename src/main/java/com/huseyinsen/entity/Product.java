package com.huseyinsen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private int stockQuantity;

    private String sku;

    private Double weight;

    private String dimensions;

    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    // Bir ürün bir kategoriye aittir
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();
}
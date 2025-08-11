package com.huseyinsen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hangi sepete ait
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // Ürün bilgisi (Product entity ile ilişki)
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private BigDecimal price; // ürün fiyatı
    private BigDecimal subtotal; // quantity * price
}
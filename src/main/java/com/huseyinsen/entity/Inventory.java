package com.huseyinsen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Product product;

    private int quantity;

    private int minThreshold; // düşük stok uyarısı eşiği

    private BigDecimal Price;

    private int stock;

    @Version
    private Long version; // optimistic locking için

    private LocalDateTime lastUpdated;


}
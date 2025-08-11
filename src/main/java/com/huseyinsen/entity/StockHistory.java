package com.huseyinsen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Inventory inventory;

    private String action; // örn: "ADD", "REMOVE", "RESERVE", "RELEASE"

    private int quantityChanged;

    private LocalDateTime actionDate;

    private String performedBy; // admin, system, vb.
}
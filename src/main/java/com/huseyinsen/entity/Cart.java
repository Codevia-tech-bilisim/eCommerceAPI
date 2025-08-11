package com.huseyinsen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kullanıcı ile bire bir ilişki
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Sepetteki ürünler
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    private String sessionId; // login olmayan kullanıcılar için oturum takibi


}
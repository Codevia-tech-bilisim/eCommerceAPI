package com.huseyinsen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kullanıcı ile ilişki
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Siparişteki ürünler
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Column(precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(precision = 19, scale = 4)
    private BigDecimal tax;

    @Column(precision = 19, scale = 4)
    private BigDecimal shippingCost;

    @Column(precision = 19, scale = 4)
    private BigDecimal discount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Adresler
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_address_id")
    private Address billingAddress;

    public void setShippingAddress(String addressLine) {
        Address address = new Address();
        address.setStreet(addressLine); // Address entity'ne göre uyarlayın
        this.shippingAddress = address;
    }

    public void setBillingAddress(String addressLine) {
        Address address = new Address();
        address.setStreet(addressLine); // Address entity'ndeki alanlara göre doldur
        this.billingAddress = address;
    }


}
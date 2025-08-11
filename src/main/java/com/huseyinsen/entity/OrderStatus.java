package com.huseyinsen.entity;

public enum OrderStatus {
    PENDING,      // Sipariş alındı, ödeme bekleniyor
    CONFIRMED,    // Ödeme onaylandı
    PROCESSING,   // Hazırlanıyor
    SHIPPED,      // Kargoya verildi
    DELIVERED,    // Teslim edildi
    CANCELLED     // İptal edildi
}
package com.huseyinsen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_audit_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long paymentId;

    private String action; // Örn: CREATED, REFUNDED, FAILED

    @Column(length = 1000)
    private String details;

    private LocalDateTime timestamp;
}
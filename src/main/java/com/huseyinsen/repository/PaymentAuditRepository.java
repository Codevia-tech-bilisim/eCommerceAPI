package com.huseyinsen.repository;

import com.huseyinsen.entity.PaymentAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAuditRepository extends JpaRepository<PaymentAuditLog, Long> {
    // İstersen özel sorgular ekleyebilirsin, örnek:
    // List<PaymentAuditLog> findByPaymentId(Long paymentId);
}
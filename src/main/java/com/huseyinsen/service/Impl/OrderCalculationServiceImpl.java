package com.huseyinsen.service.Impl;

import com.huseyinsen.dto.CreateOrderRequest;
import com.huseyinsen.entity.Inventory;
import com.huseyinsen.repository.InventoryRepository;
import com.huseyinsen.service.IOrderCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderCalculationServiceImpl implements IOrderCalculationService {

    private final InventoryRepository inventoryRepository;

    @Override
    public double calculateSubtotal(CreateOrderRequest request) {
        return request.getItems().stream()
                .mapToDouble(item -> {
                    var product = inventoryRepository.findByProductId(item.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Ürün stokta bulunamadı: " + item.getProductId()));
                    return product.getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()))
                            .doubleValue();                })
                .sum();
    }

    @Override
    public double calculateTax(double subtotal) {
        return subtotal * 0.18; // %18 KDV
    }

    @Override
    public double calculateShippingCost(double subtotal) {
        return subtotal > 500 ? 0 : 50;
    }

    @Override
    public double calculateDiscount(CreateOrderRequest request) {
        // Şu anda yok, ileride kupon sistemi veya promosyon eklenebilir
        return 0;
    }

    @Override
    public double calculateTotal(CreateOrderRequest request) {
        double subtotal = calculateSubtotal(request);
        double tax = calculateTax(subtotal);
        double shipping = calculateShippingCost(subtotal);
        double discount = calculateDiscount(request);
        return subtotal + tax + shipping - discount;
    }
}
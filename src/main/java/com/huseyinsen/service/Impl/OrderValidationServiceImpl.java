package com.huseyinsen.service.Impl;

import com.huseyinsen.dto.CreateOrderRequest;
import com.huseyinsen.entity.Inventory;
import com.huseyinsen.entity.User;
import com.huseyinsen.repository.InventoryRepository;
import com.huseyinsen.repository.OrderRepository;
import com.huseyinsen.service.IOrderValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderValidationServiceImpl implements IOrderValidationService {

    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    @Override
    public void validateMinimumOrderAmount(double totalAmount) {
        if (totalAmount < 50.0) {
            throw new IllegalArgumentException("Sipariş tutarı minimum 50 TL olmalıdır.");
        }
    }

    @Override
    public void validateMaxQuantityPerItem(CreateOrderRequest request) {
        int maxQty = 5;
        request.getItems().forEach(item -> {
            if (item.getQuantity() > maxQty) {
                throw new IllegalArgumentException("Bir üründen en fazla " + maxQty + " adet alabilirsiniz.");
            }
        });
    }

    @Override
    public void validateProductAvailability(CreateOrderRequest request) {
        request.getItems().forEach(item -> {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Ürün stokta bulunamadı: " + item.getProductId()));
            if (inventory.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Ürün stokta yeterli değil: " + item.getProductId());
            }
        });
    }

    @Override
    public void validateUserAddress(User user, Long addressId) {
        boolean hasAddress = user.getAddresses().stream()
                .anyMatch(address -> address.getId().equals(addressId));
        if (!hasAddress) {
            throw new IllegalArgumentException("Geçersiz adres seçildi.");
        }
    }

    @Override
    public void validatePaymentMethod(String paymentMethod) {
        List<String> validMethods = List.of("CREDIT_CARD", "BANK_TRANSFER", "CASH_ON_DELIVERY");
        if (!validMethods.contains(paymentMethod)) {
            throw new IllegalArgumentException("Geçersiz ödeme yöntemi.");
        }
    }

    @Override
    public void validateOrderModification(Long orderId) {
        var order = orderRepository.findById(orderId).orElseThrow();
        if (!order.getStatus().equals(com.huseyinsen.entity.OrderStatus.PENDING)) {
            throw new IllegalArgumentException("Yalnızca bekleyen siparişler üzerinde değişiklik yapılabilir.");
        }
    }
}
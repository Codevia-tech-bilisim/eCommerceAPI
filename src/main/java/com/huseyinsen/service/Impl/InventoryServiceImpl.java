package com.huseyinsen.service.Impl;

import com.huseyinsen.entity.Inventory;
import com.huseyinsen.entity.StockHistory;
import com.huseyinsen.repository.InventoryRepository;
import com.huseyinsen.repository.StockHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl {

    private final InventoryRepository inventoryRepository;
    private final StockHistoryRepository stockHistoryRepository;

    /**
     * Stok kontrolü
     */
    public boolean checkStock(Long productId, int requestedQuantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stok bulunamadı"));
        return inventory.getQuantity() >= requestedQuantity;
    }

    /**
     * Stok rezervasyonu (payment pending)
     */
    @Transactional
    public void reserveStock(Long productId, int quantity, String performedBy) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stok bulunamadı"));

        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Yetersiz stok");
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);

        saveHistory(inventory, "RESERVE", quantity, performedBy);

        if (inventory.getQuantity() <= inventory.getMinThreshold()) {
            // Burada email/log ile low stock alert tetiklenebilir
            System.out.println("Düşük stok uyarısı: " + inventory.getProduct().getName());
        }
    }

    /**
     * Stok artırma/azaltma
     */
    @Transactional
    public void updateStock(Long productId, int quantity, String action, String performedBy) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stok bulunamadı"));

        if ("REMOVE".equalsIgnoreCase(action) && inventory.getQuantity() < quantity) {
            throw new RuntimeException("Yetersiz stok");
        }

        if ("ADD".equalsIgnoreCase(action)) {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else if ("REMOVE".equalsIgnoreCase(action)) {
            inventory.setQuantity(inventory.getQuantity() - quantity);
        }

        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);

        saveHistory(inventory, action, quantity, performedBy);
    }

    private void saveHistory(Inventory inventory, String action, int quantity, String performedBy) {
        StockHistory history = StockHistory.builder()
                .inventory(inventory)
                .action(action)
                .quantityChanged(quantity)
                .actionDate(LocalDateTime.now())
                .performedBy(performedBy)
                .build();
        stockHistoryRepository.save(history);
    }
}
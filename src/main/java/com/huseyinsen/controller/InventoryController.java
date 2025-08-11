package com.huseyinsen.controller;

import com.huseyinsen.service.Impl.InventoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryServiceImpl inventoryService;

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkStock(@RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(inventoryService.checkStock(productId, quantity));
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveStock(@RequestParam Long productId, @RequestParam int quantity) {
        inventoryService.reserveStock(productId, quantity, "system");
        return ResponseEntity.ok("Stok rezerve edildi");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateStock(
            @RequestParam Long productId,
            @RequestParam int quantity,
            @RequestParam String action
    ) {
        inventoryService.updateStock(productId, quantity, action, "admin");
        return ResponseEntity.ok("Stok güncellendi");
    }
}
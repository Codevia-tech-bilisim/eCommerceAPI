package com.huseyinsen.controller;

import com.huseyinsen.dto.AddToCartRequest;
import com.huseyinsen.dto.CartResponse;
import com.huseyinsen.dto.UpdateCartItemRequest;
import com.huseyinsen.service.Impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.AccessDeniedException;

@RequestMapping("")
@RestController
@RequiredArgsConstructor
public class CartController {

    final private CartServiceImpl cartService;

    @PostMapping("/api/cart/items")
    public ResponseEntity<Void> addItem(@RequestBody AddToCartRequest request,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        cartService.addToCart(request, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/cart")
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }

    @PutMapping("/api/cart/items/{itemId}")
    public ResponseEntity<Void> updateItem(@PathVariable Long itemId,
                                           @RequestBody UpdateCartItemRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) throws java.nio.file.AccessDeniedException {
        cartService.updateItemQuantity(itemId, request.getQuantity(), userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/cart/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId,
                                           @AuthenticationPrincipal UserDetails userDetails) throws java.nio.file.AccessDeniedException {
        cartService.removeItem(itemId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/cart")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
        return ResponseEntity.ok().build();
    }



}

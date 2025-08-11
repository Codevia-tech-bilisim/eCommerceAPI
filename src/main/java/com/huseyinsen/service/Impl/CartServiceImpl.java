package com.huseyinsen.service.Impl;

import com.huseyinsen.dto.AddToCartRequest;
import com.huseyinsen.dto.CartItemResponse;
import com.huseyinsen.dto.CartResponse;
import com.huseyinsen.entity.Cart;
import com.huseyinsen.entity.CartItem;
import com.huseyinsen.entity.Product;
import com.huseyinsen.entity.User;
import com.huseyinsen.repository.CartItemRepository;
import com.huseyinsen.repository.CartRepository;
import com.huseyinsen.repository.ProductRepository;
import com.huseyinsen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public void addToCart(AddToCartRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> Cart.builder()
                        .user(user)
                        .build());


        Product product = productRepository.findById(request.getProductId()).orElseThrow();

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Stokta yeterli ürün yok");
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setSubtotal(
                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        } else {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            item.setPrice(product.getPrice());
            item.setSubtotal(
                    product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()))
            );            item.setCart(cart);
            cart.getItems().add(item);
        }

        cartRepository.save(cart);
    }


    public CartResponse getCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseThrow();

        List<CartItemResponse> itemResponses = cart.getItems().stream().map(item -> {
            CartItemResponse response = new CartItemResponse();
            response.setProductId(item.getProduct().getId());
            response.setProductName(item.getProduct().getName());
            response.setQuantity(item.getQuantity());
            response.setPrice(item.getPrice());
            response.setSubtotal(item.getSubtotal());
            return response;
        }).toList();

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)           // BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);    // toplama

        CartResponse cartResponse = new CartResponse();
        cartResponse.setItems(itemResponses);
        cartResponse.setTotalAmount(totalAmount);
        return cartResponse;
    }

    public void updateItemQuantity(Long itemId, int quantity, String userEmail) throws AccessDeniedException {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();

        if (!item.getCart().getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("Yetkisiz erişim");
        }

        Product product = item.getProduct();
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Stokta yeterli ürün yok");
        }

        item.setQuantity(quantity);
        item.setSubtotal(
                item.getPrice().multiply(BigDecimal.valueOf(quantity))
        );
        cartItemRepository.save(item);
    }


    public void removeItem(Long itemId, String userEmail) throws AccessDeniedException {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();

        if (!item.getCart().getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("Yetkisiz erişim");
        }

        cartItemRepository.delete(item);
    }

    public void clearCart(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseThrow();

        cart.getItems().clear();
        cartRepository.save(cart);
    }


}

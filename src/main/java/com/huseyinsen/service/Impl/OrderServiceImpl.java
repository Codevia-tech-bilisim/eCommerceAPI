package com.huseyinsen.service.Impl;

import com.huseyinsen.dto.CreateOrderRequest;
import com.huseyinsen.dto.OrderItemResponse;
import com.huseyinsen.dto.OrderRequest;
import com.huseyinsen.dto.OrderResponse;
import com.huseyinsen.entity.*;
import com.huseyinsen.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;
@Service
@RequiredArgsConstructor
public class OrderServiceImpl {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Transactional
    public void createOrderWithException(CreateOrderRequest request, String userEmail) {
        // Normal order oluşturma logic (opsiyonel: veritabanına kaydet)
        Order order = new Order();
        order.setUser(userRepository.findByEmail(userEmail).orElseThrow());
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        // Test için exception fırlat
        throw new RuntimeException("Test exception: Transaction rollback kontrolü");
    }


    public OrderResponse createOrder(CreateOrderRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseThrow();

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Sepetiniz boş");
        }

        BigDecimal subtotal = cart.getItems().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.18)); // %18 KDV
        BigDecimal shippingCost = subtotal.compareTo(BigDecimal.valueOf(500)) > 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(50);
        BigDecimal discount = BigDecimal.ZERO;  // doğru atama

        BigDecimal total = subtotal
                .add(tax)
                .add(shippingCost)
                .subtract(discount);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());
        order.setTotalAmount(total);
        order.setTax(tax);
        order.setShippingCost(shippingCost);
        order.setDiscount(discount);

        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException(product.getName() + " stokta yok");
            }
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProductName(product.getName());
            oi.setQuantity(cartItem.getQuantity());
            oi.setUnitPrice(cartItem.getPrice());
            return oi;
        }).toList();

        order.setItems(orderItems);
        orderRepository.save(order);

        // Sepeti boşalt
        cart.getItems().clear();
        cartRepository.save(cart);

        // Response
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .totalAmount(total)
                .tax(tax)
                .shippingCost(shippingCost)
                .discount(discount)
                .items(orderItems.stream().map(oi ->
                        new OrderItemResponse(oi.getProductName(), oi.getQuantity(), oi.getUnitPrice())
                ).toList())
                .build();
    }




    public Page<OrderResponse> getUserOrders(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        return orderRepository.findByUser(user, pageable)
                .map(order -> OrderResponse.builder()
                        .orderId(order.getId())
                        .status(order.getStatus().name())
                        .totalAmount(order.getTotalAmount())
                        .tax(order.getTax())
                        .shippingCost(order.getShippingCost())
                        .discount(order.getDiscount())
                        .build());
    }

    public OrderResponse getOrderDetail(Long orderId, String userEmail) throws AccessDeniedException {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("Bu siparişi görüntüleme yetkiniz yok");
        }
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .items(order.getItems().stream()
                        .map(oi -> new OrderItemResponse(oi.getProductName(), oi.getQuantity(), oi.getUnitPrice()))
                        .toList())
                .build();
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        orderRepository.save(order);
    }

    public void cancelOrder(Long orderId, String userEmail) throws AccessDeniedException {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("Siparişi iptal etme yetkiniz yok");
        }
        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Kargolanmış veya teslim edilmiş sipariş iptal edilemez");
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private void validateMinimumOrderAmount(BigDecimal totalAmount) {
        BigDecimal minOrderAmount = new BigDecimal("50.00"); // Örnek minimum tutar
        if (totalAmount.compareTo(minOrderAmount) < 0) {
            throw new IllegalArgumentException("Sipariş tutarı minimum " + minOrderAmount + " TL olmalıdır.");
        }
    }

    private void validateMaxQuantityPerItem(OrderRequest request) {
        int maxQty = 5; // her ürün için maksimum 5 adet
        request.getItems().forEach(item -> {
            if (item.getQuantity() > maxQty) {
                throw new IllegalArgumentException("Bir üründen en fazla " + maxQty + " adet alabilirsiniz.");
            }
        });
    }

    private void validateProductAvailability(OrderRequest request) {
        request.getItems().forEach(item -> {
            Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Ürün stokta bulunamadı: " + item.getProductId()));
            if (inventory.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException("Ürün stokta yeterli değil: " + item.getProductId());
            }
        });
    }

    private void validateUserAddress(User user, Long addressId) {
        boolean hasAddress = user.getAddresses().stream()
                .anyMatch(address -> address.getId().equals(addressId));
        if (!hasAddress) {
            throw new IllegalArgumentException("Geçersiz adres seçildi.");
        }
    }

    private void validatePaymentMethod(String paymentMethod) {
        List<String> validMethods = List.of("CREDIT_CARD", "BANK_TRANSFER", "CASH_ON_DELIVERY");
        if (!validMethods.contains(paymentMethod)) {
            throw new IllegalArgumentException("Geçersiz ödeme yöntemi.");
        }
    }

    public void validateOrderModification(Order order) {
        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalArgumentException("Yalnızca bekleyen siparişler üzerinde değişiklik yapılabilir.");
        }
    }


}
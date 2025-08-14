package com.huseyinsen.controller;

import com.huseyinsen.dto.CreateOrderRequest;
import com.huseyinsen.dto.OrderResponse;
import com.huseyinsen.service.Impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderService;

    @Operation(summary = "Yeni sipariş oluşturur",
            description = "Kullanıcının sepetindeki ürünleri siparişe dönüştürür",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sipariş başarılı",
                            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Geçersiz istek"),
                    @ApiResponse(responseCode = "401", description = "Yetkisiz erişim")
            })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.createOrder(request, userDetails.getUsername()));
    }


}
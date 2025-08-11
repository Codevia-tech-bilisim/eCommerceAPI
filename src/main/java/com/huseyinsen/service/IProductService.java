package com.huseyinsen.service;

import com.huseyinsen.dto.ProductFilter;
import com.huseyinsen.dto.ProductRequest;
import com.huseyinsen.dto.ProductResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;

public interface IProductService {
    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(ProductFilter filter, Pageable pageable);

    ProductResponse createProduct(@Valid ProductRequest request);

    ProductResponse updateProduct(Long id, @Valid ProductRequest request);

    void deleteProduct(Long id);

    void reduceStock(Long productId, int quantity) throws BadRequestException;

    void addImageToProduct(Long productId, List<String> imageUrls);

    Page<ProductResponse> searchProducts(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    List<ProductResponse> searchByName(String keyword);
}
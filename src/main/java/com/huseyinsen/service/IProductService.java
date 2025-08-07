package com.huseyinsen.service;

import com.huseyinsen.dto.product.ProductFilter;
import com.huseyinsen.dto.product.ProductRequest;
import com.huseyinsen.dto.product.ProductResponse;
import com.huseyinsen.entity.Product;
import jakarta.validation.Valid;
import org.hibernate.query.Page;
import java.util.List;

import java.awt.print.Pageable;

public interface IProductService {
    ProductResponse getProductById(Long id);
    Page<ProductResponse> getAllProducts(ProductFilter filter, Pageable pageable);
    ProductResponse createProduct(@Valid ProductRequest request);
    ProductResponse updateProduct(Long id, @Valid ProductRequest request);
    void deleteProduct(Long id);
    void reduceStock(Long productId, int quantity);
    void addImageToProduct(Long productId, List<String> imageUrls);
    Page<Product> searchProducts(ProductFilter filter, Pageable pageable);
    Page<ProductResponse> searchProducts(String keyword, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);


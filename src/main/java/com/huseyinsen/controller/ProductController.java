package com.huseyinsen.controller;

import com.huseyinsen.dto.ProductFilter;
import com.huseyinsen.dto.ProductRequest;
import com.huseyinsen.dto.ProductResponse;
import com.huseyinsen.service.IFileStorageService;
import com.huseyinsen.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {


    @Autowired
    private IFileStorageService fileStorageService;

    private final IProductService productService;


    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        // Filtre nesnesini oluştur
        ProductFilter filter = new ProductFilter();
        filter.setName(name);
        filter.setCategoryId(categoryId);
        filter.setMinPrice(minPrice != null ? BigDecimal.valueOf(minPrice) : null);
        filter.setMaxPrice(maxPrice != null ? BigDecimal.valueOf(maxPrice) : null);

        // Sort objesi oluştur
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        // Pageable nesnesi oluştur
        Pageable pageable = PageRequest.of(page, size, sort);

        // Servis çağrısı
        Page<ProductResponse> products = productService.getAllProducts(filter, pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchByName(keyword));
    }

    @PutMapping("/{id}/images")
    public ResponseEntity<?> uploadProductImage(@PathVariable Long id,
                                                @RequestParam("image") MultipartFile imageFile) {
        try {
            String storedFileName = fileStorageService.storeFile(imageFile);

            productService.addImageToProduct(id, List.of(storedFileName));

            return ResponseEntity.ok("Image uploaded successfully: " + storedFileName);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<?> uploadProductImages(@PathVariable Long id,
                                                 @RequestParam("images") List<MultipartFile> images) {
        try {
            List<String> savedFileNames = fileStorageService.saveFiles(id, images);
            productService.addImageToProduct(id, savedFileNames);
            return ResponseEntity.ok("Images uploaded successfully: " + savedFileNames);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Image upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort
    ) {
        // Sort parametresini çöz
        Sort sortObj = Sort.by(Arrays.stream(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                })
                .toList());

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<ProductResponse> response = productService.searchProducts(keyword, categoryId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(response);
    }

}

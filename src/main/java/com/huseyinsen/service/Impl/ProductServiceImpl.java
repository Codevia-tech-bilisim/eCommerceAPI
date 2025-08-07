import com.huseyinsen.dto.product.ProductFilter;
import com.huseyinsen.dto.product.ProductRequest;
import com.huseyinsen.dto.product.ProductResponse;
import com.huseyinsen.entity.Category;
import com.huseyinsen.entity.Product;
import com.huseyinsen.entity.ProductStatus;
import com.huseyinsen.exception.ResourceNotFoundException;
import com.huseyinsen.repository.CategoryRepository;
import com.huseyinsen.repository.ProductRepository;
import com.huseyinsen.service.IProductService;
import com.huseyinsen.specification.ProductSpecification;
import com.huseyinsen.specification.SearchCriteria;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.hibernate.query.Page;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return modelMapper.map(product, ProductResponse.class);
    }

    @Override
    public Page<ProductResponse> getAllProducts(ProductFilter filter, Pageable pageable) {
        Specification<Product> spec = ProductSpecification.build(filter);
        return productRepository.findAll(spec, pageable)
                .map(product -> modelMapper.map(product, ProductResponse.class));
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = modelMapper.map(request, Product.class);
        product.setCategory(category);
        product.setSku(generateSKU(request.getName()));
        product.setStatus(ProductStatus.ACTIVE);
        product.setDeleted(false);

        Product saved = productRepository.save(product);
        return modelMapper.map(saved, ProductResponse.class);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        modelMapper.map(request, product);
        product.setCategory(category);
        Product updated = productRepository.save(product);
        return modelMapper.map(updated, ProductResponse.class);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        product.setDeleted(true);
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    @Override
    public void reduceStock(Long productId, int quantity) {
        Product product = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        int updatedStock = product.getStockQuantity() - quantity;
        if (updatedStock < 0) throw new BadRequestException("Stock not sufficient");
        product.setStockQuantity(updatedStock);
        if (updatedStock == 0) product.setStatus(ProductStatus.OUT_OF_STOCK);
        productRepository.save(product);
    }

    private String generateSKU(String name) {
        return name.substring(0, Math.min(name.length(), 3)).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 8);
    }


    @Override
    public void addImageToProduct(Long productId, List<String> imageUrls) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.getImageUrls() == null) {
            product.setImageUrls(new ArrayList<>());
        }

        product.getImageUrls().addAll(imageUrls);
        productRepository.save(product);
    }

    @Override
    public Page<Product> searchProducts(ProductFilter filter, Pageable pageable) {
        List<Specification<Product>> specs = new ArrayList<>();

        if (filter.getMinPrice() != null) {
            specs.add(new ProductSpecification(new SearchCriteria("price", ">=", filter.getMinPrice())));
        }

        if (filter.getMaxPrice() != null) {
            specs.add(new ProductSpecification(new SearchCriteria("price", "<=", filter.getMaxPrice())));
        }

        if (filter.getCategoryId() != null) {
            specs.add(new ProductSpecification(new SearchCriteria("category.id", ":", filter.getCategoryId())));
        }

        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            specs.add(new ProductSpecification(new SearchCriteria("name", ":", filter.getKeyword())));
        }

        Specification<Product> resultSpec = specs.stream()
                .reduce(Specification::and)
                .orElse(null);

        return productRepository.findAll(resultSpec, pageable);
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        System.out.println("DB'den veri alınıyor...");
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(Long id, Product updatedProduct) {
        Product product = productRepository.findById(id).orElseThrow();
        // update işlemi
        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        // ...
        return productRepository.save(product); // DB ve cache ayrımı yok
    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }



}
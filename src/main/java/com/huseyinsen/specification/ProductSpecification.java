package com.huseyinsen.specification;

import com.huseyinsen.dto.ProductFilter;
import com.huseyinsen.entity.Product;
import com.huseyinsen.specification.SearchCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification implements Specification<Product> {

    private final SearchCriteria criteria;

    public ProductSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">=")) {
            return builder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<=")) {
            return builder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

    // Statik build metodu
    public static Specification<Product> build(ProductFilter filter) {
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
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            specs.add(new ProductSpecification(new SearchCriteria("name", ":", filter.getName())));
        }

        Specification<Product> result = specs.stream()
                .reduce(Specification::and)
                .orElse(null);

        return result;
    }


}
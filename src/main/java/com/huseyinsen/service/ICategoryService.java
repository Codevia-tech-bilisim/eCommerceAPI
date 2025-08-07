package com.huseyinsen.service;

import com.huseyinsen.dto.category.CategoryRequest;
import com.huseyinsen.dto.category.CategoryResponse;

import java.util.List;

public interface ICategoryService {

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}
package com.example.shopapp.services;

import java.util.List;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);

    Category getCategory(Long id);

    List<Category> getAllCategories();

    Category updateCategory(Long id, CategoryDTO category);

    void deleteCategory(Long id);
}

package com.webapplication.crossport.service;

import com.webapplication.crossport.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void saveCategory(Category category);
    Category getCategoryById(Integer id);
    void deleteCategory(Integer id);
}

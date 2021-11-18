package com.webapplication.crossport.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.respository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void saveCategory(Category category) {
        this.categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Integer id) {
        Optional<Category> optional = categoryRepository.findById(id);
        Category category;
        if (optional.isPresent()) {
            category = optional.get();
        } else {
            throw new RuntimeException("Category not found for id :: " + id);
        }
        return category;
    }

    @Override
    public void deleteCategory(Integer id) {
        this.categoryRepository.deleteById(id);
    }
}

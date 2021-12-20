package com.webapplication.crossport.domain.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.ui.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import com.webapplication.crossport.infra.models.Category;
import com.webapplication.crossport.infra.repository.CategoryRepository;
import org.springframework.stereotype.Service;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findByOrderByNameAsc();
    }

    public List<Category> getShopCategories(Category selectedCategory) {
        List<Category> categories = getAllCategories();
        if (selectedCategory != null) {
            categories.remove(selectedCategory);
            categories.add(0, selectedCategory);
        }
        return categories;
    }

    public void saveCategory(Category category) {
        this.categoryRepository.save(category);
    }

    public void addCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null || Objects.equals(categoryDTO.getCategoryName(), "")) {
            throw new RuntimeException("Category name cannot be empty");
        }

        Category sameCategory = getFirstByName(categoryDTO.getCategoryName());

        if (sameCategory != null) {
            throw new RuntimeException("Same category already exists.");
        }

        Category category = new Category();
        category.setName(categoryDTO.getCategoryName());

        saveCategory(category);
    }

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

    public void deleteCategory(Integer id, boolean confirm) {
        Category category = getCategoryById(id);

        if (!category.getArticles().isEmpty() && !confirm) {
            throw new RuntimeException("You cannot delete this category as it has articles bound.");
        }

        // Remove category from all articles bound
        for (Article article : category.getArticles()) {
            article.getCategories().remove(category);
        }
        category.getArticles().clear();

        categoryRepository.delete(category);
    }

    public Category getFirstByName(String name) {
        return categoryRepository.findFirstByName(name);
    }
}

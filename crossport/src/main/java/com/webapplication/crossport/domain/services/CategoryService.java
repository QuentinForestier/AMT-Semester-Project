package com.webapplication.crossport.domain.services;

import java.util.List;
import java.util.Optional;
import com.webapplication.crossport.infra.models.Article;
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

    public void deleteCategory(Integer id) {
        Category category = getCategoryById(id);
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

package com.webapplication.crossport.services;

import com.webapplication.crossport.domain.services.CategoryService;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Category;
import com.webapplication.crossport.infra.repository.CategoryRepository;
import com.webapplication.crossport.ui.dto.CategoryDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Testing category service. Only methode that add more logic than just call category repository are tested
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Autowired
    private CategoryService categoryServiceDB;

    @Mock
    private CategoryRepository categoryRepository;

    //region GetAllCategories

    @Test
    public void getCategories_Success() {
        List<Category> categories = new ArrayList<>();

        Category category1 = new Category();
        category1.setId(1);
        category1.setName("ski");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("chaussure");

        categories.add(category2);
        categories.add(category1);

        Mockito.when(categoryRepository.findByOrderByNameAsc()).thenReturn(categories);

        List<Category> categoriesResult = categoryService.getAllCategories();

        Assertions.assertEquals(categories, categoriesResult);
    }

    @Test
    public void getCategories_Fail() {
        List<Category> categories = new ArrayList<>();

        Mockito.when(categoryRepository.findByOrderByNameAsc()).thenReturn(categories);

        List<Category> categoriesResult = categoryService.getAllCategories();

        Assertions.assertEquals(categories, categoriesResult);
    }

    //endregion

    //region GetShopCategories

    @Test
    public void getShopCategoriesNoCategorySelected_Success() {
        List<Category> categories = new ArrayList<>();

        Category category1 = new Category();
        category1.setId(1);
        category1.setName("ski");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("chaussure");

        categories.add(category2);
        categories.add(category1);

        Mockito.when(categoryRepository.findByOrderByNameAsc()).thenReturn(categories);

        List<Category> categoriesResult = categoryService.getShopCategories(null);

        Assertions.assertEquals(categories, categoriesResult);
    }

    @Test
    public void getShopCategoriesCategorySelected_Success() {
        List<Category> categories = new ArrayList<>();
        List<Category> categoriesWithSelection = new ArrayList<>();

        Category category1 = new Category();
        category1.setId(1);
        category1.setName("ski");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("chaussure");

        categories.add(category2);
        categories.add(category1);
        categoriesWithSelection.add(category1);
        categoriesWithSelection.add(category2);

        Mockito.when(categoryRepository.findByOrderByNameAsc()).thenReturn(categories);

        List<Category> categoriesResult = categoryService.getShopCategories(category1);

        Assertions.assertEquals(categoriesWithSelection, categoriesResult);
    }

    @Test
    public void getShopCategoriesNoCategorySelected_Fail() {
        List<Category> categories = new ArrayList<>();

        Mockito.when(categoryRepository.findByOrderByNameAsc()).thenReturn(categories);

        List<Category> categoriesResult = categoryService.getShopCategories(null);

        Assertions.assertEquals(categories, categoriesResult);
    }

    //endregion

    //region GetCategory

    @Test
    public void getCategoryById_Success() {
        Optional<Category> optional = Optional.of(new Category());

        Mockito.when(categoryRepository.findById(1)).thenReturn(optional);

        boolean success = true;

        try {
            categoryService.getCategoryById(1);
        } catch (Exception e) {
            success = false;
        }

        Assertions.assertTrue(success);
    }

    @Test
    public void getCategoryById_Fail() {
        Optional<Category> optional = Optional.empty();
        Mockito.when(categoryRepository.findById(1)).thenReturn(optional);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.getCategoryById(1);
        });

        Assertions.assertEquals("Category not found for id :: 1", thrown.getMessage());
    }

    @Test
    public void getCategoryByName_Success() {
        Category category = new Category();
        category.setId(1);
        category.setName("skis");

        Mockito.when(categoryRepository.findFirstByName("skis")).thenReturn(category);

        boolean success = true;

        try {
            categoryService.getFirstCategoryByName("skis");
        } catch (Exception e) {
            success = false;
        }

        Assertions.assertTrue(success);
    }

    @Test
    public void getCategoryByName_Fail() {
        Mockito.when(categoryRepository.findFirstByName("skis")).thenThrow(new RuntimeException());

        Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.getFirstCategoryByName("skis");
        });
    }

    //endregion

    //region AddCategory

    @Test
    public void addCategory_Success() {
        Category category = new Category();
        category.setName("skis");
        categoryServiceDB.saveCategory(category);

        Assertions.assertNotNull(categoryServiceDB.getFirstCategoryByName("skis"));
    }

    @Test
    public void addEmptyCategory_Fail() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("");

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.addCategory(categoryDTO);
        });

        Assertions.assertEquals("Category name cannot be empty", thrown.getMessage());
    }

    @Test
    public void addNullCategory_Fail() {
         RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.addCategory(null);
        });

        Assertions.assertEquals("Category name cannot be empty", thrown.getMessage());
    }

    @Test
    public void addExistantCategory_Fail() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName("skis");

        Category category = new Category();
        category.setName(categoryDTO.getCategoryName());

        Mockito.when(categoryService.getFirstCategoryByName("skis")).thenReturn(category);

        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            categoryService.addCategory(categoryDTO);
        });

        Assertions.assertEquals("Same category already exists.", thrown.getMessage());
    }

    //endregion

    //region DeleteCategory

    @Test
    public void deleteCategory_Success() {
        Category cat = new Category();
        Article a1 = new Article();
        Article a2 = new Article();
        a1.addCategory(cat);
        a2.addCategory(cat);

        Optional<Category> optional = Optional.of(cat);
        Mockito.when(categoryRepository.findById(1)).thenReturn(optional);

        Assertions.assertEquals(cat.getArticles().size(), 2);
        Assertions.assertEquals(a1.getCategories().size(), 1);
        Assertions.assertEquals(a1.getCategories().size(), 1);

        categoryService.deleteCategory(1, true);

        Assertions.assertTrue(cat.getArticles().isEmpty());
        Assertions.assertTrue(a1.getCategories().isEmpty());
        Assertions.assertTrue(a1.getCategories().isEmpty());
    }

    @Test
    public void deleteCategoryWithBadId_Fail() {
        Category cat = new Category();
        Article a1 = new Article();
        Article a2 = new Article();
        a1.addCategory(cat);
        a2.addCategory(cat);

        Optional<Category> optional = Optional.of(cat);
        Mockito.when(categoryRepository.findById(1)).thenReturn(optional);

        Assertions.assertEquals(cat.getArticles().size(), 2);
        Assertions.assertEquals(a1.getCategories().size(), 1);
        Assertions.assertEquals(a1.getCategories().size(), 1);

        categoryService.deleteCategory(1, true);

        Assertions.assertTrue(cat.getArticles().isEmpty());
        Assertions.assertTrue(a1.getCategories().isEmpty());
        Assertions.assertTrue(a1.getCategories().isEmpty());
    }

    @Test
    public void deleteCategoryWithArticles_Fail() {
        Category cat = new Category();
        Article a1 = new Article();
        Article a2 = new Article();
        a1.addCategory(cat);
        a2.addCategory(cat);

        Optional<Category> optional = Optional.of(cat);
        Mockito.when(categoryRepository.findById(1)).thenReturn(optional);

        Assertions.assertEquals(cat.getArticles().size(), 2);
        Assertions.assertEquals(a1.getCategories().size(), 1);
        Assertions.assertEquals(a1.getCategories().size(), 1);

        categoryService.deleteCategory(1, true);

        Assertions.assertTrue(cat.getArticles().isEmpty());
        Assertions.assertTrue(a1.getCategories().isEmpty());
        Assertions.assertTrue(a1.getCategories().isEmpty());
    }

    @Test
    public void deleteCategoryWithArticles_Success() {
        Category cat = new Category();
        Article a1 = new Article();
        Article a2 = new Article();
        a1.addCategory(cat);
        a2.addCategory(cat);

        Optional<Category> optional = Optional.of(cat);
        Mockito.when(categoryRepository.findById(1)).thenReturn(optional);

        Assertions.assertEquals(cat.getArticles().size(), 2);
        Assertions.assertEquals(a1.getCategories().size(), 1);
        Assertions.assertEquals(a1.getCategories().size(), 1);

        categoryService.deleteCategory(1, true);

        Assertions.assertTrue(cat.getArticles().isEmpty());
        Assertions.assertTrue(a1.getCategories().isEmpty());
        Assertions.assertTrue(a1.getCategories().isEmpty());
    }

    //endregion
}

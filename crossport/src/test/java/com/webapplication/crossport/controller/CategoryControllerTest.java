package com.webapplication.crossport.controller;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CategoryService;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/**
 * Testing routes from article controller.
 * @author Herzig Melvyn
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private CategoryService categoryService;

    @Test
    @WithAnonymousUser
    public void AsVisitor_getCategories_Fail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void AsUser_getCategories_Fail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void AsAdmin_getCategories_success() throws Exception {
        List<Category> mockCategories = new ArrayList<>();
        Category category1 = new Category();
        category1.setName("category1");

        Category category2 = new Category();
        category2.setName("category2");

        Category empty = new Category();
        empty.setName("empty");

        mockCategories.add(category1);
        mockCategories.add(category2);
        mockCategories.add(empty);

        Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attribute("listCategories", hasSize(3)))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is("category1"))
                        )
                )))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is("category2"))
                        )
                )))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is("empty"))
                        )
                )));
    }

    @Test
    @WithAnonymousUser
    public void AsVisitor_getCategory_Fail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));

    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void AsUser_getCategory_Fail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories/1"))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void AsAdmin_getCategory_success() throws Exception {
        Category category = new Category();
        category.setName("category");

        List<Article> articlesNotInCategory = new ArrayList<>();
        for (int i = 1; i <= 5; ++i) {
            Article article = new Article();
            article.setId(i);
            article.setName("test item " + i);
            article.setDescription("test item desc " + i);
            article.setPrice(i * 100.0);

            if (i % 2 == 0) {
                article.getCategories().add(category);
                category.getArticles().add(article);
            } else {
                articlesNotInCategory.add(article);
            }
        }

        Mockito.when(articleService.getArticlesNotInCategory(category)).thenReturn(articlesNotInCategory);
        Mockito.when(categoryService.getCategoryById(1)).thenReturn(category);

        mvc.perform(MockMvcRequestBuilders.get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("category"))
                .andExpect(model().attribute("category",
                        allOf(
                                hasProperty("name", is("category"))
                        )
                ))
                .andExpect(model().attribute("articlesNotInCategory", hasSize(3)))
                .andExpect(model().attribute("articlesNotInCategory", hasItem(
                        allOf(
                                hasProperty("name", is("test item 1"))
                        )
                )))
                .andExpect(model().attribute("articlesNotInCategory", hasItem(
                        allOf(
                                hasProperty("name", is("test item 3"))
                        )
                )))
                .andExpect(model().attribute("articlesNotInCategory", hasItem(
                        allOf(
                                hasProperty("name", is("test item 5"))
                        )
                )));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void AsAdmin_submitCategory_Success() throws Exception {
        List<Category> mockCategories = new ArrayList<>();
        Category category1 = new Category();
        category1.setName("category1");
        mockCategories.add(category1);

        Category category2 = new Category();
        category2.setName("category2");
        mockCategories.add(category2);

        Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                        .with(csrf())
                .param("categoryName", "category2"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attribute("listCategories", hasSize(2)))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is("category1"))
                        )
                )))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is("category2"))
                        )
                )));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void AsAdmin_submitCategoryWithSameName_Fail() throws Exception {
        List<Category> mockCategories = new ArrayList<>();
        Category category1 = new Category();
        category1.setName("category1");
        mockCategories.add(category1);

        Category category2 = new Category();
        category2.setName(category1.getName());
        mockCategories.add(category2);

        Mockito.when(categoryService.getFirstByName(category1.getName())).thenReturn(mockCategories.get(0));
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(category1));

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/categories")
                        .with(csrf())
                .param("categoryName", "category1"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().hasErrors());

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mav = mvcResult.getModelAndView();

        MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("categories"));
        BindingResult br = (BindingResult) mav.getModel().get("org.springframework.validation.BindingResult.categoryData");
        assertTrue(br.getAllErrors().stream().anyMatch(o -> o.getObjectName().equals("globalError")));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void AsAdmin_deleteCategoryWithoutArticle_Fail() throws Exception {

    }
}

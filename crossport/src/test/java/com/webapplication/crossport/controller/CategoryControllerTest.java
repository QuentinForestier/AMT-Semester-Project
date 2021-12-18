package com.webapplication.crossport.controller;

import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.domain.services.CategoryService;
import com.webapplication.crossport.infra.models.Category;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

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
        Category skis = new Category();
        skis.setName("skis");
        Category snowboards = new Category();
        snowboards.setName("snowboards");
        Category empty = new Category();
        empty.setName("empty");

        mockCategories.add(skis);
        mockCategories.add(snowboards);
        mockCategories.add(empty);

//        List<Article> mockArticles = new ArrayList<>();
//        for (int i = 0; i < 5; ++i) {
//            Article article = new Article();
//            article.setId(i);
//            article.setName("test item " + i);
//            article.setDescription("test item desc " + i);
//            article.setPrice(i * 100.0);
//
//            if (i % 2 == 0) {
//                article.getCategories().add(skis);
//                skis.getArticles().add(article);
//            } else {
//                article.getCategories().add(snowboards);
//                snowboards.getArticles().add(article);
//            }
//            mockArticles.add(article);
//        }

        Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attribute("listCategories", hasSize(3)))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is("skis"))
                        )
                )))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is("snowboards"))
                        )
                )))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is("empty"))
                        )
                )));
    }
}

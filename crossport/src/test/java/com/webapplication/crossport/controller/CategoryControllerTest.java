package com.webapplication.crossport.controller;

import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.domain.services.CategoryService;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Category;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * Testing routes from article controller.
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
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

    //region GetAllCategories

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
    public void AsAdmin_getCategories_Success() throws Exception {
        List<Category> mockCategories = new ArrayList<>();
        Category snowboards = new Category();
        snowboards.setId(1);
        snowboards.setName("snowboards");

        Category skis = new Category();
        skis.setId(2);
        skis.setName("skis");

        Category empty = new Category();
        empty.setId(3);
        empty.setName("empty");

        mockCategories.add(snowboards);
        mockCategories.add(skis);
        mockCategories.add(empty);

        Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attribute("listCategories", hasSize(mockCategories.size())))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is(snowboards.getName()))
                        )
                )))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is(skis.getName()))
                        )
                )))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is(empty.getName()))
                        )
                )));
    }

    //endregion

    //region GetACategory

    @Test
    @WithAnonymousUser
    public void AsVisitor_getACategory_Fail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void AsUser_getACategory_Fail() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories/1"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void AsAdmin_getACategory_Success() throws Exception {
        Category snowboards = new Category();
        snowboards.setId(1);
        snowboards.setName("snowboards");

        List<Article> articlesNotInCategory = new ArrayList<>();
        for (int i = 1; i <= 5; ++i) {
            Article article = new Article();
            article.setId(i);
            article.setName("test item " + i);
            article.setDescription("test item desc " + i);
            article.setPrice(i * 100.0);

            if (i % 2 == 0) {
                article.getCategories().add(snowboards);
                snowboards.getArticles().add(article);
            } else {
                articlesNotInCategory.add(article);
            }
        }

        Mockito.when(articleService.getArticlesNotInCategory(snowboards)).thenReturn(articlesNotInCategory);
        Mockito.when(categoryService.getCategoryById(1)).thenReturn(snowboards);

        mvc.perform(MockMvcRequestBuilders.get("/categories/{id}", snowboards.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("category"))
                .andExpect(model().attribute("category",
                        allOf(
                                hasProperty("name", is(snowboards.getName()))
                        )
                ))
                .andExpect(model().attribute("articlesNotInCategory", hasSize(articlesNotInCategory.size())))
                .andExpect(model().attribute("articlesNotInCategory", hasItem(
                        allOf(
                                hasProperty("name", is(articlesNotInCategory.get(0).getName()))
                        )
                )))
                .andExpect(model().attribute("articlesNotInCategory", hasItem(
                        allOf(
                                hasProperty("name", is(articlesNotInCategory.get(1).getName()))
                        )
                )))
                .andExpect(model().attribute("articlesNotInCategory", hasItem(
                        allOf(
                                hasProperty("name", is(articlesNotInCategory.get(2).getName()))
                        )
                )));
    }

    //endregion

    //region SubmitCategory

    @Test
    @WithAnonymousUser
    public void AsVisitor_submitCategory_Fail() throws Exception {
        Category skis = new Category();
        skis.setId(1);
        skis.setName("skis");

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                .param("categoryName", skis.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void AsUser_submitCategory_Fail() throws Exception {
        Category skis = new Category();
        skis.setId(1);
        skis.setName("skis");

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                .param("categoryName", skis.getName()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void AsAdmin_submitCategory_Success() throws Exception {
        List<Category> mockCategories = new ArrayList<>();

        Category snowboards = new Category();
        snowboards.setId(1);
        snowboards.setName("snowboards");
        mockCategories.add(snowboards);

        Category skis = new Category();
        skis.setId(2);
        skis.setName("skis");
        mockCategories.add(skis);

        Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                .param("categoryName", skis.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"))
                .andExpect(model().attribute("listCategories", hasSize(mockCategories.size())))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is(snowboards.getName()))
                        )
                )))
                .andExpect(model().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is(skis.getName()))
                        )
                )));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void AsAdmin_submitCategoryWithSameName_Fail() throws Exception {
        Category snowboards = new Category();
        snowboards.setId(1);
        snowboards.setName("snowboards");

        Mockito.doThrow(new RuntimeException("error"))
                .when(categoryService).addCategory(ArgumentMatchers.any());

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.post("/categories")
                .param("categoryName", snowboards.getName()))
                .andExpect(status().isOk())
                .andExpect(view().name("categories"));

        MvcResult mvcResult = resultActions.andReturn();
        ModelAndView mav = mvcResult.getModelAndView();

        MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("categories"));
        BindingResult br = (BindingResult) mav.getModel().get("org.springframework.validation.BindingResult.categoryDTO");
        assertTrue(br.getAllErrors().stream().anyMatch(o -> o.getObjectName().equals("Error")));
    }

    //endregion

    //region DeleteCategory

    @Test
    @WithAnonymousUser
    public void AsVisitor_deleteCategory_Fail() throws Exception {
        Category snowboards = new Category();
        snowboards.setId(1);
        snowboards.setName("snowboards");

        mvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", snowboards.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void AsUser_deleteCategory_Fail() throws Exception {
        Category snowboards = new Category();
        snowboards.setId(1);
        snowboards.setName("snowboards");

        mvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", snowboards.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void AsAdmin_deleteCategoryWithoutArticle_Success() throws Exception {
        List<Category> mockCategories = new ArrayList<>();
        Category snowboards = new Category();
        snowboards.setId(1);
        snowboards.setName("snowboards");
        mockCategories.add(snowboards);

        Category skis = new Category();
        skis.setId(2);
        skis.setName("skis");
        mockCategories.add(skis);

        Mockito.when(categoryService.getCategoryById(1)).thenReturn(snowboards);
        Mockito.when(articleService.getCategoryArticles(snowboards)).thenReturn(new ArrayList<>());

        mockCategories.remove(mockCategories.get(0));
        Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", snowboards.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories"))
                .andExpect(flash().attribute("listCategories", hasSize(1)))
                .andExpect(flash().attribute("listCategories", hasItem(
                        allOf(
                                hasProperty("name", is(skis.getName()))
                        )
                )));
    }

    @Test
    @WithMockUser(roles={"ADMIN"})
    public void AsAdmin_deleteCategoryWithArticle_Fail() throws Exception {
        Category snowboards = new Category();
        snowboards.setId(1);
        snowboards.setName("snowboards");

        String delError = "You cannot delete this category as it has articles bound.";

        Mockito.doThrow(new RuntimeException(delError))
                .when(categoryService).deleteCategory(1, false);

        mvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", snowboards.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/" + snowboards.getId()))
                .andExpect(flash().attribute("delError", delError));
    }

    // TODO: à refaire
    @Test
    @WithMockUser(roles={"ADMIN"})
    public void AsAdmin_deleteCategoryWithArticleConfirm_Success() throws Exception {
        /*List<Category> mockCategories = new ArrayList<>();
        mockCategories.remove(mockCategories.get(0));
        Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);
        doAnswer((i)-> {
            mockCategories.remove(0);
            return null;
        }).when(categoryService.deleteCategory(1, true));
        mvc.perform(MockMvcRequestBuilders.delete("/categories/{id}", snowboards.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/categories/" + snowboards.getId()));*/
    }

    //endregion

    //region AddArticleToCategory

    @Test
    @WithAnonymousUser
    public void AsVisitor_addArticleCategory_Fail() throws Exception {
        Category skis = new Category();
        skis.setId(2);
        skis.setName("skis");

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                        .param("categoryName", skis.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void AsUser_addArticleCategory_Fail() throws Exception {
        Category skis = new Category();
        skis.setId(2);
        skis.setName("skis");

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                        .param("categoryName", skis.getName()))
                .andExpect(status().is4xxClientError());
    }

    //endregion

    //region RemoveArticleToCategory

    @Test
    @WithAnonymousUser
    public void AsVisitor_removeArticleCategory_Fail() throws Exception {
        Category skis = new Category();
        skis.setId(2);
        skis.setName("skis");

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                        .param("categoryName", skis.getName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void AsUser_removeArticleCategory_Fail() throws Exception {
        Category skis = new Category();
        skis.setId(2);
        skis.setName("skis");

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                        .param("categoryName", skis.getName()))
                .andExpect(status().is4xxClientError());
    }

    //endregion
}

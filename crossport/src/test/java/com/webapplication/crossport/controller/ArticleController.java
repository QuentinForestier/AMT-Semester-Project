package com.webapplication.crossport.controller;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CategoryService;
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
public class ArticleController {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ArticleService articleService;

	@MockBean
	private CategoryService categoryService;

	@Test
	@WithAnonymousUser
	public void AsVisitor_getArticles_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/articles/manage"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));

	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void AsUser_getArticles_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/articles/manage"))
				.andExpect(status().is4xxClientError());

	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void AsAdmin_getArticles_success() throws Exception {

		List<Article> mockArticles = new ArrayList<>();
		Category skis = new Category();
		skis.setName("skis");
		Category snowboards = new Category();
		snowboards.setName("snowboards");

		for(int i = 1; i <= 6; i++) {
			Article article = new Article();
			article.setId(i);
			article.setName("test name " + i);
			article.setDescription("test desc " + i);
			article.setPrice(i * 10.);

			if(i % 2 == 0) {
				article.getCategories().add(skis);
				skis.getArticles().add(article);
			} else {
				article.getCategories().add(snowboards);
				snowboards.getArticles().add(article);
			}
			article.setImgExtension(".png");
			mockArticles.add(article);
		}

		Mockito.when(articleService.getAllArticles()).thenReturn(mockArticles);


		mvc.perform(MockMvcRequestBuilders.get("/articles/manage"))
				.andExpect(status().isOk())
				.andExpect(view().name("manageArticles"))
				.andExpect(model().attribute("listArticles", hasSize(6)))
				.andExpect(model().attribute("listArticles", hasItem(
						allOf(
								hasProperty("name", is("test name 1")),
								hasProperty("description", is("test desc 1")),
								hasProperty("price", is(10.0))
						)
				)))
				.andExpect(model().attribute("listArticles", hasItem(
						allOf(
								hasProperty("name", is("test name 5")),
								hasProperty("description", is("test desc 5")),
								hasProperty("price", is(50.0))
						)
				)))
				.andExpect(model().attribute("listArticles", hasItem(
						allOf(
								hasProperty("name", is("test name 4")),
								hasProperty("description", is("test desc 4")),
								hasProperty("price", is(40.0))
						)
				)));

	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void getCategoryPage_Admin_getAllCategories() throws Exception {
		        Category skis = new Category();
		        skis.setId(1);
		        skis.setName("skis");

		        Category snowboards = new Category();
		        snowboards.setId(2);
		        snowboards.setName("snowboards");

		        Category empty = new Category();
		        empty.setId(3);
		        empty.setName("empty");

		List<Category> mockCategories = new ArrayList<>();
		        mockCategories.add(skis);
		        mockCategories.add(snowboards);
		        mockCategories.add(empty);

		        List<Article> mockArticles = new ArrayList<>();
		        for (int i = 0; i < 5; ++i) {
		            Article article = new Article();
		            article.setId(i);
		            article.setName("test item " + i);
		            article.setDescription("test item desc " + i);
		            article.setPrice(i * 100.0);

		            if (i % 2 == 0) {
		                article.getCategories().add(skis);
		                skis.getArticles().add(article);
		            } else {
		                article.getCategories().add(snowboards);
		                snowboards.getArticles().add(article);
		            }
		            mockArticles.add(article);
		        }

		Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);

		        mvc.perform(MockMvcRequestBuilders.get("/categories"))
		                .andExpect(status().isOk())
		                .andExpect(view().name("categories"))
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

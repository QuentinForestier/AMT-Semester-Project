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

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testing shop controller routes
 * @author Herzig Melvyn
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ShopControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ArticleService articleService;

	@MockBean
	private CategoryService categoryService;

	@Test
	@WithMockUser(roles = {"USER"})
	public void getHomePage_Registered_Success() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/shop"))
				.andExpect(status().isOk())
				.andExpect(view().name("shop"));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void getHomePage_Admin_Success() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/shop"))
				.andExpect(status().isOk())
				.andExpect(view().name("shop"));
	}

	@Test
	@WithAnonymousUser
	public void AsVisitor_getShopWithAllArticles_Success() throws Exception {

		List<Article> mockArticles = new ArrayList<>();
		List<Category> mockCategories = new ArrayList<>();

		Category skis = new Category();
		skis.setName("skis");
		mockCategories.add(skis);
		Category snowboards = new Category();
		snowboards.setName("snowboards");
		mockCategories.add(snowboards);
		Category unusedCat = new Category();
		snowboards.setName("unused");
		mockCategories.add(unusedCat);


		for(int i = 1; i <= 6; i++) {
			Article article = new Article();
			article.setId(i);
			article.setName("test name " + i);
			article.setDescription("test desc " + i);
			article.setPrice(i * 10.);

			if(i % 2 == 0) { // 2 4 6
				article.getCategories().add(skis);
				skis.getArticles().add(article);
			} else if (i % 3 == 0){ // 3 6
				article.getCategories().add(snowboards);
				snowboards.getArticles().add(article);
			}
			article.setImgExtension(".png");
			mockArticles.add(article);
		}

		Mockito.when(articleService.getAllArticles()).thenReturn(mockArticles);
		Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);


		mvc.perform(MockMvcRequestBuilders.get("/shop"))
				.andExpect(status().isOk())
				.andExpect(view().name("shop"))
				.andExpect(model().attribute("listCategories", hasSize(3)))
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
								hasProperty("name", is("test name 4")),
								hasProperty("description", is("test desc 4")),
								hasProperty("price", is(40.0)),
								hasProperty("categories",  hasItem(
										allOf(
												hasProperty("name", is("skis"))
										)
								))
						)
				)))
				.andExpect(model().attribute("listArticles", hasItem(
						allOf(
								hasProperty("name", is("test name 6")),
								hasProperty("description", is("test desc 6")),
								hasProperty("price", is(60.0)),
								hasProperty("categories",   hasItem(
										allOf(
												hasProperty("name", is("skis"))
										)))
						)

				)));

	}

	@Test
	@WithAnonymousUser
	public void AsVisitor_getShopWithArticlesFromCategory_Success() throws Exception {

		List<Article> mockArticles = new ArrayList<>();
		List<Category> mockCategories = new ArrayList<>();

		Category skis = new Category();
		skis.setName("skis");
		mockCategories.add(skis);
		Category snowboards = new Category();
		snowboards.setName("snowboards");
		mockCategories.add(snowboards);
		Category unusedCat = new Category();
		snowboards.setName("unused");
		mockCategories.add(unusedCat);


		for(int i = 1; i <= 6; i++) {
			Article article = new Article();
			article.setId(i);
			article.setName("test name " + i);
			article.setDescription("test desc " + i);
			article.setPrice(i * 10.);

			if(i % 2 == 0) { // 2 4 6
				article.getCategories().add(skis);
				skis.getArticles().add(article);
			}
			if (i % 3 == 0){ // 3 6
				article.getCategories().add(snowboards);
				snowboards.getArticles().add(article);
			}
			article.setImgExtension(".png");
			mockArticles.add(article);
		}

		Mockito.when(articleService.getAllArticles()).thenReturn(mockArticles);
		Mockito.when(categoryService.getAllCategories()).thenReturn(mockCategories);
		Mockito.when(categoryService.getCategoryById(1)).thenReturn(skis);
		Mockito.when(categoryService.getCategoryById(2)).thenReturn(snowboards);
		Mockito.when(articleService.getCategoryArticles(skis)).thenReturn(new ArrayList<>(skis.getArticles()));
		Mockito.when(articleService.getCategoryArticles(snowboards)).thenReturn(new ArrayList<>(snowboards.getArticles()));

		mvc.perform(MockMvcRequestBuilders.get("/shop").param("idCategory", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("shop"))
				.andExpect(model().attribute("listCategories", hasSize(3)))
				.andExpect(model().attribute("listArticles", hasSize(3)))
				.andExpect(model().attribute("listArticles", hasItem(
						allOf(
								hasProperty("name", is("test name 2")),
								hasProperty("description", is("test desc 2")),
								hasProperty("price", is(20.0))
						)
				)))
				.andExpect(model().attribute("listArticles", hasItem(
						allOf(
								hasProperty("name", is("test name 6")),
								hasProperty("description", is("test desc 6")),
								hasProperty("price", is(60.0))
						)
				)))
				.andExpect(model().attribute("listArticles", not(hasItem(
						allOf(
								hasProperty("name", is("test name 1")),
								hasProperty("description", is("test desc 1")),
								hasProperty("price", is(10.0))
						)
				))));

		mvc.perform(MockMvcRequestBuilders.get("/shop").param("idCategory", "2"))
				.andExpect(status().isOk())
				.andExpect(view().name("shop"))
				.andExpect(model().attribute("listCategories", hasSize(3)))
				.andExpect(model().attribute("listArticles", hasSize(2)))
				.andExpect(model().attribute("listArticles", hasItem(
						allOf(
								hasProperty("name", is("test name 6")),
								hasProperty("description", is("test desc 6")),
								hasProperty("price", is(60.0))
						)
				)))
				.andExpect(model().attribute("listArticles", not(hasItem(
						allOf(
								hasProperty("name", is("test name 2")),
								hasProperty("description", is("test desc 2")),
								hasProperty("price", is(20.0))
						)
				))));
	}


	@Test
	@WithAnonymousUser
	public void AsVisitor_getItem_Success() throws Exception {

		Category skis = new Category();
		skis.setName("skis");

		Article article = new Article();
		article.setId(1);
		article.setName("test name 1");
		article.setDescription("test desc 1");
		article.setPrice(10.);
		article.getCategories().add(skis);

		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		mvc.perform(MockMvcRequestBuilders.get("/shop/1").param("idCategory", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("article"))
				.andExpect(model().attribute("article", article));
	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void AsUser_getItem_Success() throws Exception {

		Category skis = new Category();
		skis.setName("skis");

		Article article = new Article();
		article.setId(1);
		article.setName("test name 1");
		article.setDescription("test desc 1");
		article.setPrice(10.);
		article.getCategories().add(skis);

		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		mvc.perform(MockMvcRequestBuilders.get("/shop/1").param("idCategory", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("article"))
				.andExpect(model().attribute("article", article));
	}

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void AsAdmin_getItem_Success() throws Exception {

		Category skis = new Category();
		skis.setName("skis");

		Article article = new Article();
		article.setId(1);
		article.setName("test name 1");
		article.setDescription("test desc 1");
		article.setPrice(10.);
		article.getCategories().add(skis);

		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		mvc.perform(MockMvcRequestBuilders.get("/shop/1").param("idCategory", "1"))
				.andExpect(status().isOk())
				.andExpect(view().name("article"))
				.andExpect(model().attribute("article", article));
	}

	@Test
	@WithAnonymousUser
	public void AsVisitor_getItemBadId_Fail() throws Exception {

		Category skis = new Category();
		skis.setName("skis");

		Article article = new Article();
		article.setId(1);
		article.setName("test name 1");
		article.setDescription("test desc 1");
		article.setPrice(10.);
		article.getCategories().add(skis);

		Mockito.when(articleService.getArticleById(1)).thenThrow(RuntimeException.class);

		mvc.perform(MockMvcRequestBuilders.get("/shop/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("shop"));
	}

}

package com.webapplication.crossport.controller;

import com.webapplication.crossport.config.images.ImageConfiguration;
import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Category;
import com.webapplication.crossport.ui.dto.ArticleDTO;
import org.apache.commons.compress.utils.IOUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

/**
 * Testing routes from article controller.
 * @author Herzig Melvyn
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ArticleService articleService;

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


		mvc.perform(MockMvcRequestBuilders.get("/articles"))
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
	@WithAnonymousUser
	public void AsVisitor_getArticlesEdit_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/articles/edit"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/login"));

		mvc.perform(MockMvcRequestBuilders.post("/articles/edit"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@WithMockUser(roles = {"USER"})
	public void AsUser_getArticlesEdit_Fail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/articles/edit"))
				.andExpect(status().is4xxClientError());

		mvc.perform(MockMvcRequestBuilders.post("/articles/edit"))
				.andExpect(status().is4xxClientError());

	}


	@Test
	@WithMockUser(roles={"ADMIN"})
	public void AsAdmin_getEditForm_Success() throws Exception {

		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setPrice(10.);

		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		mvc.perform(MockMvcRequestBuilders.get("/articles/edit?id=1"))
				.andExpect(status().isOk())
				.andExpect(view().name("editArticle"))
				.andExpect(model().attribute("id", 1))
				.andExpect(model().attribute("articleData",
						allOf(
								hasProperty("articleName", is("test name")),
								hasProperty("articleDesc", is("test desc")),
								hasProperty("articlePrice", is(10.0)),
								hasProperty("articleStock", is(false))
						)
				));
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void AsAdmin_submitEditFormWithSameName_Fail() throws Exception {

		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setPrice(10.);

		Article article2 = new Article();
		article2.setId(2);
		article2.setName(article.getName());
		article2.setDescription("a desc");
		article2.setInStock(true);
		article2.setPrice(12.);

		ArticleDTO ad = new ArticleDTO();
		ad.setArticleDesc(article.getDescription());
		ad.setArticleName(article.getName());
		ad.setArticlePrice(article.getPrice());
		ad.setArticleStock(article.isInStock());
		ad.setImgPath(article.getImgPath());

		MockMultipartFile image
				= new MockMultipartFile("image", "", "img", new byte[]{});

		Mockito.doThrow(new RuntimeException("Two article cannot have the same name")).
				when(articleService).modifyArticle(
						ArgumentMatchers.any(),
						ArgumentMatchers.any(),
						ArgumentMatchers.any(),
						ArgumentMatchers.any());
		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.multipart("/articles")
						.file(image)
						.param("Submit", "Submit")
						.param("articleName", article.getName())
						.param("articleDesc", article.getDescription())
						.param("articlePrice", Double.toString(article.getPrice()))
						.param("articleStock", "true")

				)
				.andExpect(status().isOk());

		MvcResult mvcResult = resultActions.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("newArticle"));
		BindingResult br = (BindingResult)mav.getModel().get("org.springframework.validation.BindingResult.articleDTO");
		assertTrue(br.getAllErrors().stream().anyMatch(o -> o.getObjectName().equals("Error")));

	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void AsAdmin_submitEditFormWith0Price_Fail() throws Exception {

		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setPrice(0.);


		Mockito.when(articleService.findFirstByName("test name")).thenReturn(article);
		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		MockMultipartFile image
				= new MockMultipartFile("image", "", "img", new byte[]{});

		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.multipart("/articles/edit?id=1")
						.file(image)
						.param("Submit", "Submit")
						.param("articleName", article.getName())
						.param("articleDesc", article.getDescription())
						.param("articlePrice", Double.toString(article.getPrice()))
						.param("articleStock", "true")

				)
				.andExpect(status().isOk())
				.andExpect(model().hasErrors());

		MvcResult mvcResult = resultActions.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("editArticle"));
		BindingResult br = (BindingResult)mav.getModel().get("org.springframework.validation.BindingResult.articleData");
		assertTrue(br.getAllErrors().stream().anyMatch(o -> o.getObjectName().equals("priceError")));

	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void AsAdmin_submitEditFormWithBadFileType_Fail() throws Exception {

		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setPrice(0.);


		Mockito.when(articleService.findFirstByName("test name")).thenReturn(article);
		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		MockMultipartFile image
				= new MockMultipartFile("image", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

		ResultActions resultActions = mvc.perform(MockMvcRequestBuilders.multipart("/articles/edit?id=1")
						.file(image)
						.param("Submit", "Submit")
						.param("articleName", article.getName())
						.param("articleDesc", article.getDescription())
						.param("articlePrice", Double.toString(article.getPrice()))
						.param("articleStock", "true")

				)
				.andExpect(status().isOk())
				.andExpect(model().hasErrors());

		MvcResult mvcResult = resultActions.andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		MatcherAssert.assertThat(mav.getViewName(), Matchers.equalTo("editArticle"));
		BindingResult br = (BindingResult)mav.getModel().get("org.springframework.validation.BindingResult.articleData");
		assertTrue(br.getAllErrors().stream().anyMatch(o -> o.getObjectName().equals("extError")));

	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void AsAdmin_editArticle_Success() throws Exception {


		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setPrice(10.);

		Mockito.when(articleService.findFirstByName("test name")).thenReturn(article);
		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		File fileItem = new File("src/main/resources/static/images/1.jpg");
		FileInputStream input = new FileInputStream(fileItem);
		MockMultipartFile image = new MockMultipartFile("image",
				fileItem.getName(), "image/png", IOUtils.toByteArray(input));

		String newName = "new article name";
		String newDesc = "new article desc";
		Boolean newDisponibility = true;
		String newExtension = ".jpg";


		MockMultipartHttpServletRequestBuilder builder =
				MockMvcRequestBuilders.multipart("/articles");
		builder.with(request -> {
			request.setMethod("PUT");
			return request;
		});

		mvc.perform( builder
						.param("id", article.getId().toString())
						.param("Submit", "Submit")
						.param("articleName", newName)
						.param("articleDesc", newDesc)
						.param("articleStock", newDisponibility.toString()))
				.andExpect(status().isOk())
				.andExpect(view().name("manageArticles"));

		Article articleModified = articleService.getArticleById(1);
		assertEquals(articleModified.getName(), newName);
		assertEquals(articleModified.getDescription(), newDesc);
		assertEquals(articleModified.isInStock(), (boolean) newDisponibility);
		assertEquals(articleModified.getImgExtension(), newExtension);
	}

	@Test
	@WithMockUser(roles={"ADMIN"})
	public void AsAdmin_removeArticleImage_Success() throws Exception {

		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setImgExtension(".jpg");
		article.setPrice(10.);

		Mockito.when(articleService.findFirstByName("test name")).thenReturn(article);
		Mockito.when(articleService.getArticleById(1)).thenReturn(article);

		String newName = "new article name";

		File directory = new File(ImageConfiguration.uploadDir);
		if (!directory.exists()) {
			directory.mkdir();
		}
		File fileSrc = new File("src/main/resources/static/images/1.jpg");
		File fileDst = new File(ImageConfiguration.uploadDir + "/1.jpg");
		InputStream in = new BufferedInputStream(new FileInputStream(fileSrc));
		OutputStream out = new BufferedOutputStream(new FileOutputStream(fileDst));

		byte[] buffer = new byte[1024];
		int lengthRead;
		while ((lengthRead = in.read(buffer)) > 0) {
			out.write(buffer, 0, lengthRead);
			out.flush();
		}
		in.close();
		out.close();

		mvc.perform(MockMvcRequestBuilders.put("/articles")
						.param("id", article.getId().toString())
						.param("DeleteImage", "DeleteImage")
						.param("articleName", newName)
						.param("articleDesc", article.getDescription())
						.param("articlePrice", Double.toString(article.getPrice()))
						.param("articleStock", "true"))
				.andExpect(status().isOk())
				.andExpect(view().name("editArticle"))
				.andExpect(model().attribute("articleData",
				allOf(
						hasProperty("articleName", is(newName))
					)
				));

		directory.delete();

		Article articleModified = articleService.getArticleById(1);
		assertNull(articleModified.getImgExtension());
	}
}

package com.webapplication.crossport.services;

import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.domain.services.FileService;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Category;
import com.webapplication.crossport.infra.repository.ArticleRepository;
import com.webapplication.crossport.infra.repository.CategoryRepository;
import com.webapplication.crossport.ui.dto.ArticleDTO;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing article service
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ArticleServiceTest {


	@InjectMocks
	private ArticleService as;

	@Mock
	private ArticleRepository ar;

	@Mock
	private CategoryRepository cr;

	@Mock
	private FileService fs;

	@Test
	public void getAllArticles_NoArticle() {
		Mockito.when(ar.findAll()).thenThrow(RuntimeException.class);

		List<Article> list = as.getAllArticles();

		assertTrue(list.isEmpty());
	}

	@Test
	public void getAllArticles_WithArticle() {

		List<Article> returnedArticles = new LinkedList<>();
		returnedArticles.add(new Article());
		returnedArticles.add(new Article());

		Mockito.when(ar.findAll()).thenReturn(returnedArticles);

		List<Article> list = as.getAllArticles();

		assertEquals(list.size(), 2);
	}

	@Test
	public void getCategoryArticles_NoArticle() {

		Category cat = new Category();

		Mockito.when(ar.findArticlesByCategoriesContaining(cat)).thenThrow(RuntimeException.class);

		List<Article> list = as.getCategoryArticles(cat);

		assertTrue(list.isEmpty());
	}

	@Test
	public void getCategoryArticles_WithArticle() {

		Category cat = new Category();

		List<Article> returnedArticles = new LinkedList<>();
		returnedArticles.add(new Article());
		returnedArticles.add(new Article());

		Mockito.when(ar.findArticlesByCategoriesContaining(cat)).thenReturn(returnedArticles);

		List<Article> list = as.getCategoryArticles(cat);

		assertEquals(list.size(), 2);
	}

	@Test
	public void getArticlesNotInCategory_NoArticle() {

		Category cat = new Category();

		Mockito.when(ar.findArticlesByCategoriesContaining(cat)).thenThrow(RuntimeException.class);

		List<Article> list = as.getCategoryArticles(cat);

		assertTrue(list.isEmpty());
	}

	@Test
	public void getArticlesNotInCategory_WithArticle() {

		Category cat = new Category();

		List<Article> returnedArticles = new LinkedList<>();
		returnedArticles.add(new Article());
		returnedArticles.add(new Article());

		Mockito.when(ar.findArticlesByCategoriesContaining(cat)).thenReturn(returnedArticles);

		List<Article> list = as.getCategoryArticles(cat);

		assertEquals(list.size(), 2);
	}

	@Test
	public void getArticleById_badId() {

		Optional<Article> optional = Optional.empty();

		Mockito.when(ar.findById(1)).thenReturn(optional);

		boolean success = false;

		try {
			Article article = as.getArticleById(1);
		} catch (Exception e) {
			if (e.getMessage().equals("Article not found for id :: 1")){
				success = true;
			}
		}

		assertTrue(success);
	}

	@Test
	public void getArticleById_goodId() {

		Optional<Article> optional = Optional.of(new Article());

		Mockito.when(ar.findById(1)).thenReturn(optional);

		boolean success = true;

		try {
			as.getArticleById(1);
		} catch (Exception e) {
			success = false;
		}

		assertTrue(success);
	}

	@Test
	public void findFirstByName_badName() {

		Mockito.when(ar.findFirstByName("a name")).thenReturn(null);

		boolean success = false;

		try {
			as.findFirstByName("a name");
		} catch (Exception e) {
			if (e.getMessage().equals("Article not found for name :: a name")){
				success = true;
			}
		}

		assertTrue(success);
	}

	@Test
	public void findFirstByName_goodName() {

		Mockito.when(ar.findFirstByName("a name")).thenReturn(new Article());

		boolean success = true;

		try {
			as.findFirstByName("a name");
		} catch (Exception e) {
			success = false;
		}

		assertTrue(success);
	}

	@Test
	public void removeCategory() {
		Category cat = new Category();
		Article article = new Article();

		Optional<Article> optional = Optional.of(article);
		Mockito.when(ar.findById(1)).thenReturn(optional);

		article.addCategory(cat);

		assertTrue(!cat.getArticles().isEmpty());

		as.removeCategory(1, cat);

		assertTrue(article.getCategories().isEmpty());
		assertTrue(cat.getArticles().isEmpty());
	}

	@Test
	public void addCategory() {
		Category cat = new Category();
		Article article = new Article();

		Optional<Article> optional = Optional.of(article);
		Mockito.when(ar.findById(1)).thenReturn(optional);

		as.addCategory(1, cat);

		assertTrue(!article.getCategories().isEmpty());
		assertTrue(!cat.getArticles().isEmpty());
	}

	@Test
	public void  modifyArticle_BadPrice_Fail() {
		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setArticleName("test name");
		articleDTO.setArticleDesc("test desc");
		articleDTO.setArticleStock(false);
		articleDTO.setArticlePrice(-1.);
		articleDTO.setImgPath(null);

		boolean threw = false;

		try {
			as.modifyArticleImage(null, articleDTO, null, 1);
		} catch(RuntimeException e) {
			threw = true;
		}
		assertTrue(threw);
	}

	@Test
	public void  modifyArticle_ExistingName_Fail() {
		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setArticleName("test name");
		articleDTO.setArticleDesc("test desc");
		articleDTO.setArticleStock(false);
		articleDTO.setArticlePrice(-1.);
		articleDTO.setImgPath(null);

		Mockito.when(ar.findFirstByName(articleDTO.getArticleName())).thenReturn(new Article());

		boolean threw = false;

		try {
			as.modifyArticleImage(null, articleDTO, null, 1);
		} catch(RuntimeException e) {
			threw = true;
		}
		assertTrue(threw);
	}

	@Test
	public void  modifyArticle_badFile_Fail() {
		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setArticleName("test name");
		articleDTO.setArticleDesc("test desc");
		articleDTO.setArticleStock(false);
		articleDTO.setArticlePrice(-1.);
		articleDTO.setImgPath(null);

		Mockito.when(ar.findFirstByName(articleDTO.getArticleName())).thenReturn(null);

		MockMultipartFile text
				= new MockMultipartFile("image", "hello.txt",  MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

		boolean threw = false;

		try {
			as.modifyArticleImage(null, articleDTO, text, 1);
		} catch(RuntimeException e) {
			threw = true;
		}
		assertTrue(threw);
	}

	@Test
	public void  modifyArticle_Success() {
		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setPrice(10.);
		article.setImgExtension(null);

		String newName = "new name";
		String newDesc = "new desc";
		boolean newStock = true;
		double newPrice = 20.;

		ArticleDTO articleDTO = new ArticleDTO();
		articleDTO.setArticleName(newName);
		articleDTO.setArticleDesc(newDesc);
		articleDTO.setArticleStock(newStock);
		articleDTO.setArticlePrice(newPrice);

		Mockito.when(ar.findFirstByName(articleDTO.getArticleName())).thenReturn(null);

		MockMultipartFile image
				= new MockMultipartFile("image", "hello.png",  MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());

		Mockito.when(fs.isAnAuthorizedExtension(ArgumentMatchers.any())).thenReturn(true);
		Mockito.when(fs.getExtension(ArgumentMatchers.any())).thenReturn(".png");

		as.modifyArticle(article, articleDTO, image, article.getId());

		assertEquals(article.getName(), newName);
		assertEquals(article.getDescription(), newDesc);
		assertEquals(article.getPrice(), newPrice, 0.000001);
		assertEquals(article.isInStock(), newStock);
		assertEquals(article.getImgPath(), article.getId().toString() + ".png" );
	}

	@Test
	public void modifyArticleImage_badFile_Fail(){

		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setPrice(10.);
		article.setImgExtension(null);

		boolean threw = false;

		MockMultipartFile text
				= new MockMultipartFile("image", "hello.txt",  MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

		try {
			as.modifyArticleImage(article, null, text, 0);
		} catch(RuntimeException e) {
			threw = true;
		}
		assertTrue(threw);
	}

	@Test
	public void modifyArticleImage_Success(){

		Article article = new Article();
		article.setId(1);
		article.setName("test name");
		article.setDescription("test desc");
		article.setInStock(false);
		article.setPrice(10.);
		article.setImgExtension(null);

		ArticleDTO adto = new ArticleDTO();

		MockMultipartFile text
				= new MockMultipartFile("image", "hello.png",  MediaType.IMAGE_PNG_VALUE, "Hello, World!".getBytes());

		Mockito.when(fs.isAnAuthorizedExtension(ArgumentMatchers.any())).thenReturn(true);
		Mockito.when(fs.getExtension(ArgumentMatchers.any())).thenReturn(".png");

		as.modifyArticleImage(article, adto, text, article.getId());

		assertEquals(article.getImgExtension(), ".png");
	}
}

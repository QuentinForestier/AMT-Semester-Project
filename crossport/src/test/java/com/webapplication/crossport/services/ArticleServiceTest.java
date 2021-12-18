package com.webapplication.crossport.services;

import com.webapplication.crossport.config.images.ImageConfiguration;
import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.repository.ArticleRepository;
import com.webapplication.crossport.ui.dto.ArticleDTO;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
public class ArticleServiceTest {


	@InjectMocks
	private ArticleService as;

	@Mock
	private ArticleRepository ar;

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

		as.modifyArticle(article, articleDTO, image, article.getId());

		assertEquals(article.getName(), newName);
		assertEquals(article.getDescription(), newDesc);
		assertEquals(article.getPrice(), newPrice, 0.000001);
		assertEquals(article.isInStock(), newStock);
		assertEquals(article.getImgPath(), "/" + ImageConfiguration.uploadDir + "/" + article.getId().toString() + ".png" );
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

		as.modifyArticleImage(article, adto, text, article.getId());

		assertEquals(article.getImgExtension(), ".png");
	}
}

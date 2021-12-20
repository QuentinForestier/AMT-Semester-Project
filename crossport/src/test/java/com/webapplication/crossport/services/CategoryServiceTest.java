package com.webapplication.crossport.services;

import com.webapplication.crossport.domain.services.CategoryService;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Category;
import com.webapplication.crossport.infra.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing category service. Only methode that add more logic than just call category repository are tested
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
	private CategoryService cs;

	@Mock
	private CategoryRepository cr;

	@Test
	public void getCategoryById_badId() {

		Optional<Category> optional = Optional.empty();

		Mockito.when(cr.findById(1)).thenReturn(optional);

		boolean success = false;

		try {
			cs.getCategoryById(1);
		} catch (Exception e) {
			if (e.getMessage().equals("Category not found for id :: 1")){
				success = true;
			}
		}

		assertTrue(success);
	}

	@Test
	public void getCategoryById_goodId() {

		Optional<Category> optional = Optional.of(new Category());

		Mockito.when(cr.findById(1)).thenReturn(optional);

		boolean success = true;

		try {
			cs.getCategoryById(1);
		} catch (Exception e) {
			success = false;
		}

		assertTrue(success);
	}

	@Test
	public void deleteCategory() {

		Category cat = new Category();
		Article a1 = new Article();
		Article a2 = new Article();
		a1.addCategory(cat);
		a2.addCategory(cat);

		Optional<Category> optional = Optional.of(cat);
		Mockito.when(cr.findById(1)).thenReturn(optional);

		assertEquals(cat.getArticles().size(), 2);
		assertEquals(a1.getCategories().size(), 1);
		assertEquals(a1.getCategories().size(), 1);

		cs.deleteCategory(1);

		assertTrue(cat.getArticles().isEmpty());
		assertTrue(a1.getCategories().isEmpty());
		assertTrue(a1.getCategories().isEmpty());

	}
}

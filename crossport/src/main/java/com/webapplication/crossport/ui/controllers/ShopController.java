package com.webapplication.crossport.ui.controllers;

import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Category;
import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.domain.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

/**
 * Controller charged to manage the shop with articles and categories
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Controller
@RequestMapping("/shop")
public class ShopController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("")
	public String getShop(@RequestParam(value = "idCategory", required = false) Integer idCategory, Model model) {
		// idCategory == null = All Articles / No Category

		List<Article> articles;
		Category selectedCategory = null;
		try {
			if (idCategory == null) {
				articles = articleService.getAllArticles();
			} else {
				selectedCategory = categoryService.getCategoryById(idCategory);
				articles = articleService.getCategoryArticles(selectedCategory);
			}
		} catch (Exception e) {
			return "shop";
		}

		List<Category> categories = categoryService.getShopCategories(selectedCategory);

		model.addAttribute("categorySelected", selectedCategory);
		model.addAttribute("listArticles", articles);
		model.addAttribute("listCategories", categories);
		return "shop";
	}

	@GetMapping("/articles/{id}")
	public String getById(@PathVariable(value = "id") Integer id, Model model) {
		Article article;
		try {
			article = articleService.getArticleById(id);
		} catch (Exception e) {
			return "shop";
		}
		model.addAttribute("article", article);
		return "article";
	}
}

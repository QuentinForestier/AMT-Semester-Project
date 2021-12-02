package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("")
	public String getShop(@RequestParam(value = "idCategory", required = false) Integer idCategory, Model model) {
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
			e.printStackTrace();
			return "shop?error";
		}

		List<Category> categories = categoryService.getAllCategories();
		if (selectedCategory != null) {
			categories.remove(selectedCategory);
			categories.add(0, selectedCategory);
		}

		model.addAttribute("categorySelected", selectedCategory);
		model.addAttribute("listArticles", articles);
		model.addAttribute("listCategories", categories);
		return "shop";
	}

	@GetMapping("/{id}")
	public String getById(@PathVariable(value = "id") Integer id, Model model) {
		Article article;
		try {
			article = articleService.getArticleById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "article?error";
		}
		model.addAttribute("article", article);
		return "article";
	}
}

package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/shop")
    public String viewShop(@RequestParam(value = "idCategory", required = false) Integer idCategory,
                           Model model) {
        if (idCategory == null) {
            model.addAttribute("idCategorySelected", null);
            model.addAttribute("listArticles", articleService.getAllArticles());
        } else {
            Category category = categoryService.getCategoryById(idCategory);
            model.addAttribute("idCategorySelected", category.getId());
            model.addAttribute("listArticles", articleService.getCategoryArticles(category));
        }
        model.addAttribute("listCategories", categoryService.getAllCategories());
        return "shop";
    }

    @GetMapping("/article")
    public String viewAnArticle(@RequestParam(value = "id") Integer id,
                                Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article";
    }
}

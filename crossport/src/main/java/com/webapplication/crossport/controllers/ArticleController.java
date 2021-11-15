package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/shop")
    public String viewShop(Model model) {
        model.addAttribute("listArticles", articleService.getAllArticles());
        model.addAttribute("article", new Article());
        return "shop";
    }

    @GetMapping("/article{id}")
    public String viewAnArticle(@RequestParam(value = "id", required = true) Integer id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article";
    }
}

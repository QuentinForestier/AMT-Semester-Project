package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/shop")
    public String viewHomePage(Model model) {
        model.addAttribute("listArticles", articleService.getAllArticles());
        model.addAttribute("article", new Article());
        return "shop";
    }
}

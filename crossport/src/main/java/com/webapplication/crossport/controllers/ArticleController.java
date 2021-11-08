package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @GetMapping("/shop")
    public String viewAllArticles(Model model)
    {
        return findPaginated(1, model);
    }

    @GetMapping("/shop/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 1;

        Page<Article> page = articleService.findPaginated(pageNo, pageSize);
        List<Article> listArticles = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listArticles", listArticles);
        return "shop";
    }
}

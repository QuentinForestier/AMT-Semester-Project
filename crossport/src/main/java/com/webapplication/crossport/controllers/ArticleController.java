package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class ArticleController {
    // TODO DPO - Utilisez Autowired, c'est cool (je le faisais également avant), mais ceci implique un petit arrachage
    //  de cheveux si vous faites des tests d'intégration comparé à l'utilisation des constructeurs.
    //  Je vous conseille, à moins que vous soyez au clair là-dessus, d'utiliser les constructeurs.
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/shop")
    public String viewShop(@RequestParam(value = "idCategory", required = false) Integer idCategory, Model model) {
        List<Article> articles;
        Category selectedCategory = null;
        try  {
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

    // TODO DPO - Top vos contrôleurs. Ils sont petits et simples. Nickel. Ce que je peux vous conseiller
    //  comme amélioration, c'est de nommer vos routes avec un 's' à la fin.
    @GetMapping("/article")
    public String viewAnArticle(@RequestParam(value = "id") Integer id, Model model) {
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

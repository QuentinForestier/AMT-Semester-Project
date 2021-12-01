package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CategoryService;
import com.webapplication.crossport.service.ArticleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = {"/articles", "/shop"})
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public String getCategory(@RequestParam(value = "idCategory", required = false) Integer idCategory, Model model) {
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

    @GetMapping("/manage")
    public String viewArticles(Model model) {
        model.addAttribute("listArticles", articleService.getAllArticles());
        return "manageArticles";
    }

    @GetMapping("/edit")
    public String editArticle(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Integer id) {
        ArticleData ad = new ArticleData();
        if (id != null) {
            Article a = articleService.getArticleById(id);
            ad.setArticleDesc(a.getDescription());
            ad.setArticleName(a.getName());
            ad.setArticleStock(a.isInStock());
            ad.setArticlePrice(a.getNullablePrice());
        }
        model.addAttribute("id", id);
        model.addAttribute("articleData", ad);
        return "editArticle";
    }

    @PostMapping("/edit")
    public String postArticle(final @ModelAttribute @Valid ArticleData articleData, final BindingResult bindingResult, final Model model, @RequestParam(value = "id", required = false) Integer id) {
        if (articleData.getArticlePrice() != null && articleData.getArticlePrice() <= 0) {
            bindingResult.addError(
                    new ObjectError("priceError", "The price of the article must be greater than 0"));
        }

        if (articleService.getArticleByName(articleData.getArticleName()) != null) {
            bindingResult.addError(
                    new ObjectError("nameError", "Two article cannot have the same name"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("articleData", articleData);
            return "editArticle";
        }

        Article article;
        if (id == null) {
            article = new Article();
        } else {
            article = articleService.getArticleById(id);
        }
        article.setPrice(articleData.getArticlePrice());
        article.setName(articleData.getArticleName());
        article.setDescription(articleData.getArticleDesc());
        article.setInStock(articleData.isArticleStock());
        articleService.modifyArticle(article);
        return "redirect:manage";
    }
}
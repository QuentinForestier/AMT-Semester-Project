package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.repository.ArticleRepository;
import com.webapplication.crossport.models.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Cart controller to insert / remove article
 * @author Herzig Melvyn
 */
@Controller
public class CartController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/addArticle{id}")
    public String addArticle(HttpServletRequest request, @RequestParam(value = "id", required = true) Integer id, Model model) {

        Cart cart = Cart.getCartInSession(request.getSession());

        try {
            Article article = articleService.getArticleById(id);
            cart.addToCart(1, article);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        //your controller code
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

}

package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Cart controller to insert / remove article
 *
 * @author Herzig Melvyn
 */
@Controller
public class CartController
{

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CartService cartService;

    @Autowired
    private HttpSession session;

    @GetMapping("/addArticle")
    public String addArticle(HttpServletRequest request, @RequestParam(value
            = "id") Integer id, @RequestParam(value = "quantity",
            defaultValue = "1") Integer quantity)
    {

        Cart cart = Cart.getCartInSession(request.getSession());

        try
        {
            Article article = articleService.getArticleById(id);
            cart.addToCart(quantity, article);
            saveCart(cart);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }

        //your controller code
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/clearCart")
    public String clearCart(HttpServletRequest request)
    {
        Cart cart = Cart.getCartInSession(request.getSession());
        cart.clear();

        saveCart(cart);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/removeArticle{id}")
    public String removeArticle(@RequestParam(value = "id") Integer id,
                                HttpServletRequest request)
    {
        Cart cart = Cart.getCartInSession(request.getSession());

        Article article = articleService.getArticleById(id);

        cart.removeArticle(article);

        saveCart(cart);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/updateQuantity/{id}/{quantity}")
    public String updateQuantity(@PathVariable(value = "id") Integer id,
                                 @PathVariable(value = "quantity") Integer quantity,
                                 HttpServletRequest request)
    {
        Cart cart = Cart.getCartInSession(request.getSession());
        Article article = articleService.getArticleById(id);

        if (quantity == 0)
        {
            cart.removeArticle(article);
        }
        else
        {
            cart.addToCart(quantity, article);
        }

        saveCart(cart);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/cart")
    public String viewCart(HttpServletRequest request, Model model)
    {

        model.addAttribute("cart", Cart.getCartInSession(request.getSession()));

        return "cart";
    }


    private void saveCart(Cart cart){
        if (session != null && session.getAttribute("member") != null)
        {
            cartService.save(cart);
        }
    }
}

package com.webapplication.crossport.controllers;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.CartArticle;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CartArticleService;
import com.webapplication.crossport.models.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Cart controller to insert / remove article
 *
 * @author Herzig Melvyn
 */
@Controller
@RequestMapping("/cart")
public class CartController
{
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CartService cartService;

    @Autowired
    CartArticleService cartArticleService;

    @Autowired
    private HttpSession session;

    @GetMapping("")
    public String viewCart(HttpServletRequest request, Model model)
    {
        Cart cartInSession = Cart.getContextCart(request.getSession());

        if (cartInSession.getId() != null)
            cartInSession = cartService.load(cartInSession.getId());

        model.addAttribute("cart", cartInSession);

        return "cart";
    }

    @PostMapping("/article")
    public String addArticle(HttpServletRequest request,
                             @RequestParam(value = "id") Integer id,
                             @RequestParam(value = "quantity",
                                     defaultValue = "1") Integer quantity)
    {

        Cart cart = Cart.getContextCart(request.getSession());
        if (cart.getId() != null)
            cart = cartService.load(cart.getId());

        try
        {
            Article article = articleService.getArticleById(id);
            CartArticle ca = cart.addToCart(quantity, article);
            saveCartArticle(ca);
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
        }

        //your controller code
        String referer = request.getHeader("Referer");
        String parameter = (referer.contains("added") ? "" :
                referer.contains("?") ? "&added" : "?added");
        return "redirect:" + referer + parameter;
    }

    @DeleteMapping("")
    public String clear(HttpServletRequest request)
    {
        Cart cart = Cart.getContextCart(request.getSession());
        cart.clear();

        saveCart(cart);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @DeleteMapping("/article/{id}")
    public String removeArticle(@PathVariable(value = "id") Integer id,
                                HttpServletRequest request)
    {
        Cart cart = Cart.getContextCart(request.getSession());
        if (cart.getId() != null)
            cart = cartService.load(cart.getId());

        CartArticle ca = cart.getCartArticleByArticle(articleService.getArticleById(id));

        cart.removeArticle(ca);

        saveCart(cart);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PutMapping("/article/{id}/{quantity}")
    public String updateQuantity(@PathVariable(value = "id") Integer id,
                                 @PathVariable(value = "quantity") Integer quantity,
                                 HttpServletRequest request)
    {
        Cart cart = Cart.getContextCart(request.getSession());
        if (cart.getId() != null)
            cart = cartService.load(cart.getId());

        CartArticle ca = cart.getCartArticleByArticle(articleService.getArticleById(id));

        if (quantity == 0)
        {
            cartArticleService.delete(ca);
            cart.removeArticle(ca);
        }
        else
        {
            ca.setQuantity(quantity);
        }

        saveCart(cart);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    private void saveCartArticle(CartArticle ca)
    {
        if (ca != null && session != null && session.getAttribute("member") != null)
        {
            cartArticleService.save(ca);
        }
    }

    private void saveCart(Cart cart)
    {
        if (session != null && session.getAttribute("member") != null)
        {
            cartService.save(cart);
        }
    }

}

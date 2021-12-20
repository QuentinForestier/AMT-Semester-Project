package com.webapplication.crossport.ui.controllers;

import com.webapplication.crossport.infra.models.Cart;
import com.webapplication.crossport.infra.models.CartArticle;
import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.domain.services.CartArticleService;
import com.webapplication.crossport.domain.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Cart controller to insert / remove article
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
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


    @GetMapping("")
    public String viewCart(Model model)
    {
        Cart cartInSession = cartService.getContextCart();

        model.addAttribute("cart", cartInSession);

        return "cart";
    }

    @PostMapping("/articles")
    public String addArticle(HttpServletRequest request,
                             @RequestParam(value = "id") Integer id,
                             @RequestParam(value = "quantity",
                                     defaultValue = "1") Integer quantity)
    {

        Cart cart = cartService.getContextCart();


        cartService.addToCart(cart, quantity,
                articleService.getArticleById(id));

        String referer = request.getHeader("Referer");
        String parameter = (referer.contains("added") ? "" :
                referer.contains("?") ? "&added" : "?added");
        return "redirect:" + referer + parameter;
    }

    @DeleteMapping("")
    public String clear(HttpServletRequest request)
    {
        Cart cart = cartService.getContextCart();

        cart.clear();

        cartService.save(cart);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @DeleteMapping("/articles/{id}")
    public String removeArticle(@PathVariable(value = "id") Integer id,
                                HttpServletRequest request)
    {
        Cart cart = cartService.getContextCart();

        CartArticle ca =
                cart.getCartArticleByArticle(articleService.getArticleById(id));

        cart.removeArticle(ca);

        cartService.save(cart);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PutMapping("/articles/{id}/quantity/{quantity}")
    public String updateQuantity(@PathVariable(value = "id") Integer id,
                                 @PathVariable(value = "quantity") Integer quantity,
                                 HttpServletRequest request)
    {

        Cart cart = cartService.getContextCart();
        cartService.updateQuantity(cart, quantity,
                articleService.getArticleById(id));
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}

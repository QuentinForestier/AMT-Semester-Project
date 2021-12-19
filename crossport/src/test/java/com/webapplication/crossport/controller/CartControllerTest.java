package com.webapplication.crossport.controller;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.CartArticle;
import com.webapplication.crossport.models.services.ArticleService;
import com.webapplication.crossport.models.services.CartArticleService;
import com.webapplication.crossport.models.services.CartService;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class that check access to cart page and functionnality
 *
 * @author Forestier Quentin
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest
{

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private CartArticleService cartArticleService;

    @MockBean
    private ArticleService articleService;


    @Test
    @WithAnonymousUser
    public void getCartPage_Anonymous_Success() throws Exception
    {
        Cart cart = new Cart();

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);

        mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"));

    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void getCartPage_Registered_Success() throws Exception
    {
        Cart cart = new Cart();

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);

        mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getCartPage_Admin_Sucess() throws Exception
    {
        Cart cart = new Cart();

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);

        mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"));
    }

    @Test
    @WithAnonymousUser
    public void getCartPage_WithArticle_Anonymous_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(1, tmp.getCartArticles().size());

    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void getCartPage_WithArticle_Registered_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(1, tmp.getCartArticles().size());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getCartPage_WithArticle_Admin_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(1, tmp.getCartArticles().size());
    }

    @Test
    @WithAnonymousUser
    public void addArticle_Anonymous_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/article")
                        .header("Referer", "/cart")
                        .with(csrf())
                        .param("id", "1")
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart?added"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(1, tmp.getCartArticles().size());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void addArticle_Registered_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/article")
                        .header("Referer", "/cart")
                        .with(csrf())
                        .param("id", "1")
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart?added"))
                .andReturn();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(1, tmp.getCartArticles().size());
    }

    @Test
    @WithAnonymousUser
    public void clearCart_Anonymous_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.delete("/cart")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(0, tmp.getCartArticles().size());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void clearCart_Registered_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.delete("/cart")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(0, tmp.getCartArticles().size());
    }

    @Test
    @WithAnonymousUser
    public void removeArticle_Anonymous_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.delete("/cart/article/1")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(0, tmp.getCartArticles().size());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void removeArticle_Registered_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.delete("/cart/article/1")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(0, tmp.getCartArticles().size());
    }

    @Test
    @WithAnonymousUser
    public void updateQuantity_Anonymous_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.updateQuantity(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.put("/cart" +
                                "/article/1/2")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(1,
                tmp.getCartArticles().stream().filter(obj -> obj.getQuantity() == 2 && obj.getArticle().getId() == 1).count());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void updateQuantity_Registered_Success() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        CartArticle ca = new CartArticle();
        ca.setArticle(article);
        ca.setQuantity(1);

        cart.addArticle(ca);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.updateQuantity(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.put("/cart" +
                                "/article/1/2")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");

        Assertions.assertEquals(1,
                tmp.getCartArticles().stream().filter(obj -> obj.getQuantity() == 2 && obj.getArticle().getId() == 1).count());
    }
}

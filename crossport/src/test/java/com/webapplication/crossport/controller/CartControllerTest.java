package com.webapplication.crossport.controller;

import com.webapplication.crossport.domain.services.ArticleService;
import com.webapplication.crossport.domain.services.CartArticleService;
import com.webapplication.crossport.domain.services.CartService;
import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Cart;
import com.webapplication.crossport.infra.models.CartArticle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class that check access to cart page and functionnality
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
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

    //region GetCart
    @Test
    @WithAnonymousUser
    public void getCartPage_Anonymous() throws Exception
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
    public void getCartPage_Registered() throws Exception
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
    public void getCartPage_Admin() throws Exception
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
    public void getCartPage_WithArticle_Anonymous() throws Exception
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
    public void getCartPage_WithArticle_Registered() throws Exception
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
    public void getCartPage_WithArticle_Admin() throws Exception
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

    //endregion

    //region AddArticle
    @Test
    @WithAnonymousUser
    public void addArticle_Anonymous() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
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
    public void addArticle_Registered() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
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
    public void addArticle_alreadyAddedArticle_Anonymous() throws Exception
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

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
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
        CartArticle caInCart = (CartArticle) tmp.getCartArticles().toArray()[0];

        Assertions.assertEquals(2, caInCart.getQuantity());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void addArticle_alreadyAddedArticle_Registered() throws Exception
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

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
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
        CartArticle caInCart = (CartArticle) tmp.getCartArticles().toArray()[0];

        Assertions.assertEquals(2, caInCart.getQuantity());
    }

    @Test
    @WithAnonymousUser
    public void addArticle_add2Articles_Anonymous() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        Article article2 = new Article();
        article2.setPrice(20.0);
        article2.setInStock(true);
        article2.setName("Test2");
        article2.setId(2);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);
        Mockito.when(articleService.getArticleById(2)).thenReturn(article2);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
                        .header("Referer", "/cart")
                        .with(csrf())
                        .param("id", "1")
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart?added"))
                .andReturn();

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
                        .header("Referer", "/cart")
                        .with(csrf())
                        .param("id", "2")
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

        Assertions.assertEquals(2, tmp.getCartArticles().size());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void addArticle_add2Articles_Registered() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);

        Article article2 = new Article();
        article2.setPrice(20.0);
        article2.setInStock(true);
        article2.setName("Test2");
        article2.setId(2);

        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);
        Mockito.when(articleService.getArticleById(2)).thenReturn(article2);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
                        .header("Referer", "/cart")
                        .with(csrf())
                        .param("id", "1")
                        .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart?added"))
                .andReturn();

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
                        .header("Referer", "/cart")
                        .with(csrf())
                        .param("id", "2")
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

        Assertions.assertEquals(2, tmp.getCartArticles().size());
    }

    @Test
    @WithAnonymousUser
    public void addArticle_Quantity0_Anonymous() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
                        .header("Referer", "/cart")
                        .with(csrf())
                        .param("id", "1")
                        .param("quantity", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart?added"));

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
    public void addArticle_Quantity0_Registered() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
                        .header("Referer", "/cart")
                        .with(csrf())
                        .param("id", "1")
                        .param("quantity", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart?added"));

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
    public void addArticle_NotInStock_Anonymous() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(false);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
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

        Assertions.assertEquals(0, tmp.getCartArticles().size());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    public void addArticle_NotInStock_Registered() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(false);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
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

        Assertions.assertEquals(0, tmp.getCartArticles().size());
    }

    @Test
    @WithAnonymousUser
    public void addArticle_WithoutPrice_Anonymous() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(null);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
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

        Assertions.assertEquals(0, tmp.getCartArticles().size());
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void addArticle_WithoutPrice_Registered() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(null);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.post("/cart" +
                                "/articles")
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

        Assertions.assertEquals(0, tmp.getCartArticles().size());
    }
    //endregion

    //region ClearCart
    @Test
    @WithAnonymousUser
    public void clearCart_Anonymous() throws Exception
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
    public void clearCart_Registered() throws Exception
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

    //endregion

    //region RemoveArticle
    @Test
    @WithAnonymousUser
    public void removeArticle_Anonymous() throws Exception
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

        mvc.perform(MockMvcRequestBuilders.delete("/cart/articles/1")
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
    public void removeArticle_Registered() throws Exception
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

        mvc.perform(MockMvcRequestBuilders.delete("/cart/articles/1")
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
    public void removeArticle_NotInCart_Anonymous() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.delete("/cart/articles/1")
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
    public void removeArticle_NotInCart_Registered() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);

        mvc.perform(MockMvcRequestBuilders.delete("/cart/articles/1")
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
    //endregion

    //region UpdateQuantity
    @Test
    @WithAnonymousUser
    public void updateQuantity_Anonymous() throws Exception
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
                                "/articles/1/quantity/2")
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
    public void updateQuantity_Registered() throws Exception
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
                                "/articles/1/quantity/2")
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
    @WithAnonymousUser
    public void updateQuantity_Quantity0_Anonymous() throws Exception
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

        Mockito.doNothing().when(cartArticleService).delete(any(CartArticle.class));

        mvc.perform(MockMvcRequestBuilders.put("/cart" +
                                "/articles/1/quantity/0")
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
    public void updateQuantity_Quantity0_Registered() throws Exception
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

        Mockito.doNothing().when(cartArticleService).delete(any(CartArticle.class));

        mvc.perform(MockMvcRequestBuilders.put("/cart" +
                                "/articles/1/quantity/0")
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
    public void updateQuantity_NotInCartArticle_Anonymous() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.updateQuantity(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);


        mvc.perform(MockMvcRequestBuilders.put("/cart" +
                                "/articles/1/quantity/2")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");
        CartArticle caTmp = (CartArticle) tmp.getCartArticles().toArray()[0];
        Assertions.assertEquals(2, caTmp.getQuantity());
    }

    @Test
    @WithMockUser(roles={"USER"})
    public void updateQuantity_NotInCartArticle_Registered() throws Exception
    {
        Cart cart = new Cart();

        Article article = new Article();
        article.setPrice(20.0);
        article.setInStock(true);
        article.setName("Test");
        article.setId(1);


        Mockito.when(cartService.load(1)).thenReturn(cart);
        Mockito.when(cartService.getContextCart()).thenReturn(cart);
        Mockito.when(cartService.updateQuantity(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();

        Mockito.when(cartService.addToCart(any(Cart.class),
                any(Integer.class), any(Article.class))).thenCallRealMethod();


        Mockito.when(articleService.getArticleById(1)).thenReturn(article);


        mvc.perform(MockMvcRequestBuilders.put("/cart" +
                                "/articles/1/quantity/2")
                        .header("Referer", "/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/cart"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/cart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("cart"))
                .andReturn();

        Cart tmp = (Cart) result.getModelAndView().getModel().get("cart");
        CartArticle caTmp = (CartArticle) tmp.getCartArticles().toArray()[0];
        Assertions.assertEquals(2, caTmp.getQuantity());
    }
    //endregion
}

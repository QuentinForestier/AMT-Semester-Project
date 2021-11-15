package com.webapplication.crossport.models;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Entity model for JPA. Represents a user cart.
 * @author Herzig Melvyn
 */
@Entity
@Table(name = "cart")
public class Cart {

    /**
     * Primary key, unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * Related cart articles
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_cart_article_id")
    private Set<CartArticle> cartArticles = new HashSet<>();

    /* -------------------------- GETTERS AND SETTERS -------------------------------*/

    /**
     * Gets cart id
     * @return Cart id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets new cart id
     * @param id New cart
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * For a given http request, gets the session cart. If none creates and return one.
     * @param session Context session
     * @return The session cart.
     */
    public static Cart getCartInSession(HttpSession session) {

        //CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");
        Cart cart = (Cart) session.getAttribute("myCart");

        if (cart == null) {
            cart = new Cart();

            session.setAttribute("myCart", cart);
        }

        return cart;
    }

    /**
     * Adds and articleId
     * @param quantity Quantity in carty for the given article
     * @param article Article to add
     */
    public void addToCart(int quantity, Article article) {

        Optional<CartArticle> cartArticle = cartArticles.stream().filter(ca -> ca.getArticle().getId() == article.getId()).findFirst();

        if(cartArticle.isPresent()) {
            
        }

    }


}

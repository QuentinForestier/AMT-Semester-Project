package com.webapplication.crossport.models;

import com.webapplication.crossport.service.exception.CartException;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     * @param articleId Given article
     * @throws CartException
     */
    public void addToCart(int quantity,int articleId) throws CartException {

    }


}

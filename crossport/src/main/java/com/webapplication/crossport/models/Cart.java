package com.webapplication.crossport.models;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Entity model for JPA. Represents a user cart.
 *
 * @author Herzig Melvyn
 */
@Entity
@Table(name = "cart")
public class Cart
{

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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch =
            FetchType.EAGER)
    @JoinColumn(name = "fk_cart_id")
    private Set<CartArticle> cartArticles = new HashSet<>();

    /* -------------------------- GETTERS AND SETTERS
    -------------------------------*/

    /**
     * Gets cart id
     *
     * @return Cart id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets new cart id
     *
     * @param id New cart
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    public Set<CartArticle> getCartArticles()
    {
        return cartArticles;
    }

    /**
     * <<<<<<< HEAD
     * For a given http request, gets the session cart. If none creates and
     * return one.
     *
     * @param session Context session
     * @return The session cart.
     */
    public static Cart getCartInSession(HttpSession session)
    {
        Member member = (Member) session.getAttribute("member");

        if (member == null)
        {
            if (session.getAttribute("tempCart") == null)
            {
                session.setAttribute("tempCart", new Cart());
            }
            return (Cart) session.getAttribute("tempCart");
        }

        if (member.getCart() == null)
        {
            member.setCart(new Cart());
        }

        return member.getCart();
    }


    /**
     * Adds a cartArticle. If article already exists in cart, quantity is
     * updated. If quantity is 0, article is removed.
     *
     * @param quantity Quantity in carty for the given article
     * @param article  Article to add
     */
    public void addToCart(int quantity, Article article)
    {

        Optional<CartArticle> cartArticle =
                cartArticles.stream().filter(ca -> Objects.equals(ca.getArticle().getId(), article.getId())).findFirst();

        if (cartArticle.isPresent())
        {

            if (quantity < 1)
            {
                cartArticles.remove(cartArticle.get());
            }
            else
            {
                cartArticle.get().setQuantity(quantity);
            }
        }
        else
        {
            if (quantity >= 1)
            {
                CartArticle newCartArticle = new CartArticle();
                newCartArticle.setArticle(article);
                newCartArticle.setQuantity(quantity);
                cartArticles.add(newCartArticle);
            }
        }
    }

    public void clear()
    {
        this.cartArticles.clear();
    }

    public void removeArticle(Article article)
    {
        this.cartArticles =
                this.cartArticles.stream()
                        .filter(ca -> !ca.getArticle().getId().equals(article.getId()))
                        .collect(Collectors.toSet());
    }
}

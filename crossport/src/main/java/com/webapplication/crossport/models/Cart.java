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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_cart_article_id")
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
            if(session.getAttribute("tempCart") == null){
                session.setAttribute("tempCart", new Cart());
            }
            return (Cart)session.getAttribute("tempCart");
        }

        if (member.getCart() == null)
        {
            member.setCart(new Cart());
        }

        return member.getCart();
    }

    /**
     * Adds and articleId
     *
     * @param quantity  Quantity in carty for the given article
     * @param articleId Given article
     * @throws CartException
     */
    public void addToCart(int quantity, int articleId) throws CartException
    {

    }


}

package com.webapplication.crossport.infra.models;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.*;

/**
 * Entity model for JPA. Represents a user cart.
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
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
     * For a given http request, gets the session cart. If none creates and
     * return one.
     *
     * @param session Context session
     * @return The session cart.
     */
    public static Cart getContextCart(HttpSession session)
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
     * @return The inserted or modified CartArticle. Null if removed
     */
    public CartArticle addToCart(int quantity, Article article)
    {

        Optional<CartArticle> cartArticle =
                cartArticles.stream().filter(ca -> Objects.equals(ca.getArticle().getId(), article.getId())).findFirst();

        CartArticle ca = null;

        if (cartArticle.isPresent())
        {
            ca = cartArticle.get();
            if (quantity < 1)
            {
                cartArticles.remove(ca);
                return null;
            }
            else
            {
                ca.addQuantity(quantity);
            }
        }
        else
        {
            if (quantity >= 1)
            {
                ca = new CartArticle();
                ca.setArticle(article);
                ca.setQuantity(quantity);
                cartArticles.add(ca);
            }
        }

        return ca;
    }

    public void clear()
    {
        this.cartArticles.clear();
    }

    public void removeArticle(CartArticle ca)
    {
        this.cartArticles.remove(ca);
    }

    public double getTotalPrice()
    {
        return cartArticles.stream().mapToDouble(a -> a.getArticle().getPrice() * a.getQuantity()).sum();
    }

    public void SyncCarts(Cart c)
    {
        for (CartArticle ca : c.getCartArticles())
        {
            this.addToCart(ca.getQuantity(), ca.getArticle());
        }
    }

    public CartArticle getCartArticleByArticle(Article article)
    {
        for(CartArticle ca : getCartArticles()){
            if(ca.getArticle().getId() == article.getId()){
                return ca;
            }
        }

        return null;
    }
}

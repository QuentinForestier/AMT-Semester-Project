package com.webapplication.crossport.infra.models;

import lombok.Getter;
import lombok.Setter;
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
@Getter
@Setter
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

    /* -------------------------- GETTERS AND SETTERS -------------------------------*/

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

    public void addArticle(CartArticle ca){
        this.cartArticles.add(ca);
    }

    public void removeArticle(CartArticle ca){
        this.cartArticles.remove(ca);
    }

    public void clear()
    {
        this.cartArticles.clear();
    }

    public double getTotalPrice()
    {
        return cartArticles.stream().mapToDouble(a -> a.getArticle().getPrice() * a.getQuantity()).sum();
    }


    public CartArticle getCartArticleByArticle(Article article)
    {
        for(CartArticle ca : getCartArticles()){
            if(Objects.equals(ca.getArticle().getId(), article.getId())){
                return ca;
            }
        }

        return null;
    }
}

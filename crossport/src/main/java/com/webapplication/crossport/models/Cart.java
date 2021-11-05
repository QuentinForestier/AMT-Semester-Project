package com.webapplication.crossport.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity model for JPA. Represents a user cart.
 * @author Herzig Melvyn
 */
@Entity
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
     * Sets new cartArticles
     * @param cartArticles New ralted cartArticles
     */
    public void setCartArticles(Set<CartArticle> cartArticles) {
        this.cartArticles = cartArticles;
    }
}

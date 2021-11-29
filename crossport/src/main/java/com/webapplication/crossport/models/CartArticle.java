package com.webapplication.crossport.models;

import javax.persistence.*;

/**
 * Entity model for JPA. Represents the association between a cart and an article.
 * @author Herzig Melvyn
 */
@Entity
@Table(name = "cart_article", uniqueConstraints = { @UniqueConstraint(columnNames = { "fk_cart_id", "fk_article_id" }) })
public class CartArticle {

    /**
     * Primary key, unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Quantity of related article
     */
    @Column(name = "quantity")
    private int quantity;

    /**
     * Related article
     */
    @ManyToOne
    @JoinColumn(name = "fk_article_id")
    private Article article;

    /**
     * Related article
     */
    @ManyToOne
    @JoinColumn(name = "fk_cart_id")
    private Cart cart;

    /* -------------------------- GETTERS AND SETTERS -------------------------------*/

    /**
     * Gets id
     * @return CartArticle id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets new id
     * @param id New id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets quantity
     * @return Quantity asked
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets a new quantity
     * @param quantity New quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the related article
     * @return Related article
     */
    public Article getArticle() {
        return article;
    }

    /**
     * Sets a new related article
     * @param article New article
     */
    public void setArticle(Article article) {
        this.article = article;
    }


    /**
     * Add desired quantity to existing quantity
     * @param quantity Quantity to add
     */
    public void addQuantity(Integer quantity)
    {
        this.quantity += quantity;
    }
}

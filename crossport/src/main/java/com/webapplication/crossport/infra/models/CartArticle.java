package com.webapplication.crossport.infra.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity model for JPA. Represents the association between a cart and an article.
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

    /**
     * Add desired quantity to existing quantity
     * @param quantity Quantity to add
     */
    public void addQuantity(Integer quantity)
    {
        this.quantity += quantity;
    }
}

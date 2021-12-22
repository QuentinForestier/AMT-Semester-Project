package com.webapplication.crossport.infra.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity model for JPA. Represents an article
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
@Table(name = "article")
public class Article {

    /**
     * Primary key, unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * unique description
     */
    @Column(name = "description")
    @NotNull
    private String description;

    /**
     * Name of the article
     */
    @Column(name = "name", unique = true)
    @NotNull
    private String name;

    /**
     * Price
     */
    @Column(name = "price")
    private Double price;

    /**
     * Path of image that represents the article
     */
    @Column(name = "imgExtension", length = 10)
    private String imgExtension;

    /**
     * Is article available ? If true then yes
     */
    @Column(name = "inStock")
    @NotNull
    private boolean inStock;

    /**
     * Related categories
     */
    @ManyToMany(mappedBy = "articles", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<Category> categories = new HashSet<>();

    /* -------------------------- M to M utilities -------------------------------*/

    /**
     * Add a category to the article and the article to the category.
     *
     * @param category Category to add.
     */
    public void addCategory(Category category) {
        this.categories.add(category);
        category.getArticles().add(this);
    }

    /**
     * Remove a category from the article and the article from the category.
     *
     * @param category Category to remove.
     */
    public void removeCategory(Category category) {
        this.categories.remove(category);
        category.getArticles().remove(this);
    }

    /* -------------------------- GETTERS AND SETTERS -------------------------------*/


    public Double getNullablePrice() {
        return price;
    }

    public String getImgPath() {
        if (imgExtension == null)
            return null;

        return id.toString() + imgExtension;
    }
}

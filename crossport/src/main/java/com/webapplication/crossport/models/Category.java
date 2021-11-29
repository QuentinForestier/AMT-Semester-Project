package com.webapplication.crossport.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity model for JPA. Represents a category for articles.
 * @author Herzig Melvyn
 */
@Entity
@Table(name = "category")
public class Category {

    /**
     * Primary key, unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * Category name
     */
    @Column(name = "name")
    private String name;

    /**
     * Related articles.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "category_article",
            joinColumns = {@JoinColumn(name = "fk_category_id")},
            inverseJoinColumns = {@JoinColumn(name = "fk_article_id")}
    )
    private Set<Article> articles = new HashSet<>();

    /* -------------------------- GETTERS AND SETTERS -------------------------------*/

    /**
     * Gets id
     * @return Category id
     */
    public Integer getId() {
        return id;
    }
    /**
     * Sets id
     * @return User id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets name
     * @return Category name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a name
     * @param name New name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the set of articles
     * @return Category articles
     */
    public Set<Article> getArticles() {return this.articles; }

    /**
     * Set related articles
     * @param articles New related articles
     */
    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }
}

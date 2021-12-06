package com.webapplication.crossport.infra.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity model for JPA. Represents a category for articles.
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
}

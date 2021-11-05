package com.webapplication.crossport.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity model for JPA. Represents an article
 * @author Herzig Melvyn
 */
@Entity
public class Article {

    /**
     * Primary key, unique description
     */
    @Id
    @Column(name = "description")
    private String description;

    /**
     * Name of the article
     */
    @Column(name = "name")
    private String name;

    /**
     * Price
     */
    @Column(name = "price")
    private Double price;

    /**
     * Path of image that represents the article
     */
    @Column(name = "imgPath")
    private String imgPath;

    /**
     * Is article available ? If true then yes
     */
    @Column(name = "inStock")
    private boolean inStock;

    /**
     * Related categories
     */
    @ManyToMany(mappedBy = "articles", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Category> categories = new HashSet<>();

    /* -------------------------- GETTERS AND SETTERS -------------------------------*/

    /**
     * Gets description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description
     * @param description New description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets article name
     * @return Article name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name
     * @param name New name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets price
     * @return Article price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets a new price
     * @param price New price
     */
    public void setPrice(Double price) {
        this.price = price;
    }


    /**
     * Gets image path
     * @return Image path
     */
    public String getImgPath() {
        return imgPath;
    }

    /**
     * sets a new image path
     * @param imgPath New path
     */
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    /**
     * Gets if article is available
     * @return True if article is available else false
     */
    public boolean isInStock() {
        return inStock;
    }

    /**
     * Changes article availability
     * @param inStock New availability
     */
    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }

    /**
     * Sets new related categories
     * @param categories New categories
     */
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}

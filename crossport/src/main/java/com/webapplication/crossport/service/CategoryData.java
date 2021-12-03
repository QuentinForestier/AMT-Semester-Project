package com.webapplication.crossport.service;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Implements a serializable object to receive categories creation
 *
 * @author Herzig Melvyn
 */
public class CategoryData implements Serializable {
    @NotBlank(message = "Category name cannot be empty")
    String categoryName;

    /**
     * Gets category's name.
     *
     * @return the name of the category
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the name of the category
     *
     * @param categoryName Category new name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
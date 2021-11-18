package com.webapplication.crossport.service;

import javax.validation.constraints.NotEmpty;

public class CreateCategoryData {
    @NotEmpty(message = "Name cannot be empty")
    private String categoryName;

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}

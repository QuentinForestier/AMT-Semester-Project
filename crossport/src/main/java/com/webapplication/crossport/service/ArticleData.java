package com.webapplication.crossport.service;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class ArticleData implements Serializable {
    @NotEmpty(message = "Article name cannot be empty")
    String articleName;
    String articleDesc;
    Double articlePrice;
    boolean articleStock;

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public String getArticleDesc() {
        return articleDesc;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc;
    }

    public Double getArticlePrice() {
        return articlePrice;
    }

    public void setArticlePrice(Double articlePrice) {
        this.articlePrice = articlePrice;
    }

    public boolean isArticleStock() {
        return articleStock;
    }

    public void setArticleStock(boolean articleStock) {
        this.articleStock = articleStock;
    }
}
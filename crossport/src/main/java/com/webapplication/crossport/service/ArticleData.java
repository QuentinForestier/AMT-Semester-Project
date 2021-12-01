package com.webapplication.crossport.service;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class ArticleData implements Serializable {
    @NotBlank(message = "Article name cannot be empty")
    private String articleName = "";
    private String articleDesc = "";
    private Double articlePrice;
    private boolean articleStock = true;
    private String imgPath;

    public String getArticleName() {
        return articleName == null ? "" : articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName == null ? "" : articleName;
    }

    public String getArticleDesc() {
        return articleDesc == null ? "" : articleDesc;
    }

    public void setArticleDesc(String articleDesc) {
        this.articleDesc = articleDesc == null ? "" : articleDesc;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
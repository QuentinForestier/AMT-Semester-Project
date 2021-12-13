package com.webapplication.crossport.ui.formdata;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Getter
@Setter
// TODO: Serializable possiblement inutile
public class ArticleDTO implements Serializable {
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
}
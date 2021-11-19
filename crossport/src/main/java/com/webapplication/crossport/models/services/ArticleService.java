package com.webapplication.crossport.models.services;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        try {
            return articleRepository.findAll();
        } catch (Exception e) {
            return new LinkedList<Article>();
        }
    }

    public List<Article> getCategoryArticles(Category category) {
        try {
            return articleRepository.findArticlesByCategoriesContaining(category);
        } catch (Exception e) {
            return new LinkedList<Article>();
        }
    }

    public Article getArticleById(Integer id) {
        Optional<Article> optional = articleRepository.findById(id);
        Article article = null;
        if (optional.isPresent()) {
            article = optional.get();
        } else {
            throw new RuntimeException("Article not found for id :: " + id);
        }
        return article;
    }
}

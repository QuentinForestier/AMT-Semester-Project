package com.webapplication.crossport.models.services;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import com.webapplication.crossport.models.repository.ArticleRepository;
import com.webapplication.crossport.models.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Article> getAllArticles() {
        try {
            return articleRepository.findAll();
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    public List<Article> getCategoryArticles(Category category) {
        try {
            return articleRepository.findArticlesByCategoriesContaining(category);
        } catch (Exception e) {
            return new LinkedList<>();
        }
    }

    public List<Article> getArticlesNotInCategory(Category category) {
        try {
            return articleRepository.findArticlesByCategoriesNotContaining(category);
        } catch (Exception e) {
            return new LinkedList<Article>();
        }
    }

    public Article getArticleById(Integer id) {
        Optional<Article> optional = articleRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Article not found for id :: " + id);
        }
    }

    public Article getArticleByName(String name) {
        return articleRepository.getArticleByName(name);
    }

    public void removeCategory(Integer idArticle, Category category) {
        Article article = getArticleById(idArticle);
        article.removeCategory(category);
        // Important to update BD
        articleRepository.saveAndFlush(article);
        categoryRepository.saveAndFlush(category);
    }

    public void addCategory(Integer idArticle, Category category) {
        Article article = getArticleById(idArticle);
        article.addCategory(category);
        // Important to update BD
        articleRepository.saveAndFlush(article);
        categoryRepository.saveAndFlush(category);
    }

    public void modifyArticle(Article article) {
        articleRepository.save(article);
    }
}
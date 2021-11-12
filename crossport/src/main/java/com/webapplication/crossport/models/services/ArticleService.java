package com.webapplication.crossport.models.services;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.respository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return (List<Article>) articleRepository.findAll();
    }
}

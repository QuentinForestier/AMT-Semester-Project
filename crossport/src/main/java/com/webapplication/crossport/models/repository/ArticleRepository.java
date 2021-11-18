package com.webapplication.crossport.models.repository;

import com.webapplication.crossport.models.Article;
import com.webapplication.crossport.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findArticlesByCategoriesContaining(Category category);
}

package com.webapplication.crossport.models.repository;

import com.webapplication.crossport.models.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface ArticleRepository extends CrudRepository<Article, Integer> {
}

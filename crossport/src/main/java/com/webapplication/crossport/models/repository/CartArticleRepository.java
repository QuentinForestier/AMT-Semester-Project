package com.webapplication.crossport.models.repository;

import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.CartArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartArticleRepository extends JpaRepository<CartArticle, Integer>
{
    List<CartArticle> findAllByCart(Cart cart);
}

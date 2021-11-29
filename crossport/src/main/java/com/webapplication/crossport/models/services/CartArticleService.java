package com.webapplication.crossport.models.services;

import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.CartArticle;
import com.webapplication.crossport.models.repository.CartArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartArticleService
{
    @Autowired
    private CartArticleRepository cartArticleRepository;

    public List<CartArticle> getCartArticleFromCart(Cart c)
    {
        return cartArticleRepository.findAllByCart(c);
    }

    public void save(CartArticle ca)
    {
        cartArticleRepository.save(ca);
    }

    public void delete(CartArticle ca)
    {
        cartArticleRepository.delete(ca);
    }
}

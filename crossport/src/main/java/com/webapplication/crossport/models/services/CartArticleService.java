package com.webapplication.crossport.models.services;

import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.CartArticle;
import com.webapplication.crossport.models.repository.CartArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CartArticleService
{
    @Autowired
    private CartArticleRepository cartArticleRepository;

    @Autowired
    private HttpSession session;

    public List<CartArticle> getCartArticleFromCart(Cart c)
    {
        return cartArticleRepository.findAllByCart(c);
    }

    public CartArticle save(CartArticle ca)
    {
        if (ca != null && session != null && session.getAttribute("member") != null) {
            return cartArticleRepository.save(ca);
        }
        return ca;
    }

    public void delete(CartArticle ca)
    {
        cartArticleRepository.delete(ca);
    }
}

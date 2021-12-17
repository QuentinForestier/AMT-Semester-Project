package com.webapplication.crossport.domain.services;

import com.webapplication.crossport.infra.models.Cart;
import com.webapplication.crossport.infra.models.CartArticle;
import com.webapplication.crossport.infra.repository.CartArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
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

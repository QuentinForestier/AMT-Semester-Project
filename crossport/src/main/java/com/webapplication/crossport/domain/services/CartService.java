package com.webapplication.crossport.domain.services;

import com.webapplication.crossport.infra.models.Cart;
import com.webapplication.crossport.infra.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Service
public class CartService
{
    @Autowired
    private CartRepository cartRepository;

    public void save(Cart cart)
    {
        cartRepository.save(cart);
    }

    public Cart load(int id)
    {
        return cartRepository.findById(id).orElse(new Cart());
    }
}

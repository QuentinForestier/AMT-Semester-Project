package com.webapplication.crossport.service;

import com.webapplication.crossport.models.Cart;
import com.webapplication.crossport.models.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

package com.webapplication.crossport.models.repository;

import com.webapplication.crossport.models.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, Integer>
{
}

package com.webapplication.crossport.infra.repository;

import com.webapplication.crossport.infra.models.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Repository
public interface CartRepository extends CrudRepository<Cart, Integer>
{
}

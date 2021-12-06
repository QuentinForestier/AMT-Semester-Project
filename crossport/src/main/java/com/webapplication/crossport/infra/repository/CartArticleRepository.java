package com.webapplication.crossport.infra.repository;

import com.webapplication.crossport.infra.models.Cart;
import com.webapplication.crossport.infra.models.CartArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Repository
public interface CartArticleRepository extends JpaRepository<CartArticle, Integer>
{
    List<CartArticle> findAllByCart(Cart cart);
}

package com.webapplication.crossport.domain.services;

import com.webapplication.crossport.infra.models.Article;
import com.webapplication.crossport.infra.models.Cart;
import com.webapplication.crossport.infra.models.CartArticle;
import com.webapplication.crossport.infra.models.Member;
import com.webapplication.crossport.infra.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Optional;

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

    @Autowired
    private CartArticleService cartArticleService;

    @Autowired
    private HttpSession session;

    /**
     * Save the cart in the database
     *
     * @param cart Cart to save
     * @return Cart saved
     */
    public Cart save(Cart cart)
    {
        if (session != null && session.getAttribute("member") != null)
        {
            return cartRepository.save(cart);
        }
        return cart;
    }

    /**
     * Load the cart from the database
     *
     * @param id id of the cart
     * @return Cart found
     */
    public Cart load(int id)
    {
        return cartRepository.findById(id).orElse(new Cart());
    }

    /**
     * For a given http request, gets the session cart. If none creates and
     * return one.
     *
     * @return The cart
     */
    public Cart getContextCart()
    {

        Member member = (Member) session.getAttribute("member");

        if (member == null)
        {
            if (session.getAttribute("tempCart") == null)
            {
                session.setAttribute("tempCart", new Cart());
            }
            return (Cart) session.getAttribute("tempCart");
        }

        if (member.getCart() == null)
        {
            member.setCart(new Cart());
        }

        return this.load(member.getCart().getId());
    }


    /**
     * Adds a cartArticle. If article already exists in cart, quantity is
     * updated. If quantity is 0, article is removed.
     *
     * @param quantity Quantity in cart for the given article
     * @param article  Article to add
     * @return Cart updated
     */
    public Cart addToCart(Cart cart, int quantity, Article article)
    {

        Optional<CartArticle> cartArticle =
                cart.getCartArticles().stream().filter(ca -> Objects.equals(ca.getArticle().getId(), article.getId())).findFirst();


        if (article.getNullablePrice() != null && article.isInStock())
        {
            CartArticle ca;

            if (cartArticle.isPresent())
            {
                ca = cartArticle.get();
                if (quantity < 1)
                {
                    cart.removeArticle(ca);
                    return null;
                }
                else
                {
                    ca.addQuantity(quantity);
                }
            }
            else
            {
                if (quantity >= 1)
                {
                    ca = new CartArticle();
                    ca.setArticle(article);
                    ca.setQuantity(quantity);
                    cart.addArticle(ca);
                }
            }

            save(cart);
        }

        return cart;
    }

    /**
     * Put all of the articles of one cart in another
     * If article already exist in first cart, add the quantity
     *
     * @param c1 Cart that will be the result
     * @param c2 Cart to merge
     */
    public void SyncCarts(Cart c1, Cart c2)
    {
        for (CartArticle ca : c2.getCartArticles())
        {
            this.addToCart(c1, ca.getQuantity(), ca.getArticle());
        }
    }

    /**
     * Update the quantity of an article in the cart
     *
     * @param cart     Cart who contains article to update
     * @param quantity New quantity
     * @param article  article wanted
     * @return Updated cart
     */
    public Cart updateQuantity(Cart cart, Integer quantity, Article article)
    {
        CartArticle ca =
                cart.getCartArticleByArticle(article);

        if (ca != null)
        {
            if (quantity == 0)
            {
                cartArticleService.delete(ca);
                cart.removeArticle(ca);
            }
            else
            {
                ca.setQuantity(quantity);
            }

            save(cart);
        }
        return cart;
    }

}

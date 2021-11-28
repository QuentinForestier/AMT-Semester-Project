package com.webapplication.crossport.models;

import javax.persistence.*;

/**
 * Entity model for JPA. Represents a web connected user
 *
 * @author Herzig Melvyn
 */
@Entity
@Table(name = "member")
public class Member
{

    /**
     * Primary key, unique identifier
     */
    @Id
    @Column(name = "id")
    private Integer id;

    // TODO DPO - Par expérience, j'évite un maximum de mettre des commentaires sur une propriété
    //  qui a déjà un nom explicite. Le but est de pouvoir lire rapidement l'objet et de savoir
    //  s'il a quelque chose de spécial (getter et setters spéciaux, propriétés, ...)
    //  Par exemple, sur un projet, on a des objets qui possèdent 20 propriétés (sans compter les annotations spring) et dans le cas d'un e-commerce comme digitec,
    //  un article en a une flopée. Si on fait un petit calcul rapide si vous étiez dans ce cas:
    //  20 (nb propriétés) * 4 (commentaires + code par propriétés) + 2 (retours à la ligne)= 160 lignes de code à lire
    //  votre patience risque d'en prendre un coup à terme...
    /**
     * User name
     */
    private String username;


    /**
     * User's cart
     */
    @OneToOne(cascade={CascadeType.ALL})
    @JoinColumn(name = "fk_cart_id")
    private Cart cart;


    // Constructor

    public Member(){
        cart = new Cart();
    }

    /* -------------------------- GETTERS AND SETTERS
    -------------------------------*/

    /**
     * Gets id
     *
     * @return User id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Sets id
     *
     * @return User id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Gets user name
     *
     * @return User name
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets user name
     *
     * @param name New name
     */
    public void setUsername(String name)
    {
        this.username = name;
    }

    public Cart getCart()
    {
        return this.cart;
    }

    public void setCart(Cart newCart)
    {
        this.cart = newCart;
    }

}

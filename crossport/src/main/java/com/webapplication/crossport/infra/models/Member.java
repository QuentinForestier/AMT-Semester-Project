package com.webapplication.crossport.infra.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Entity model for JPA. Represents a web connected user
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Getter
@Setter
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

    /**
     * Constructor
     */
    public Member(){
        cart = new Cart();
    }
}

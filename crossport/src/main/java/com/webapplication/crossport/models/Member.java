package com.webapplication.crossport.models;

import javax.persistence.*;

/**
 * Entity model for JPA. Represents a web connected user
 * @author Herzig Melvyn
 */
@Entity
@Table(name = "member")
public class Member {

    /**
     * Primary key, unique identifier
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    /**
     * User name
     */
    private String username;

    /**
     * Role, if false common user else admin
     */
    private String role;

    /**
     * User's cart
     */
    @OneToOne
    @JoinColumn(name = "fk_cart_id")
    private Cart cart;

    /* -------------------------- GETTERS AND SETTERS -------------------------------*/

    /**
     * Gets id
     * @return User id
     */
    public Integer getId() {
        return id;
    }
    /**
     * Sets id
     * @return User id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets user name
     * @return User name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets user name
     * @param name New name
     */
    public void setUsername(String name) {
        this.username = name;
    }

    /**
     * Gets role
     * @return True if user is administrator
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets role
     * @param role New role
     */
    public void setRole(String role) {
        role = role;
    }


}

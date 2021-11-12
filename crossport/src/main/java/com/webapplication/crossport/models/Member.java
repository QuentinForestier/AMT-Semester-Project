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
    @Column(name = "name")
    private String name;

    /**
     * Role, if false common user else admin
     */
    @Column(name = "isAdmin")
    private boolean isAdmin;

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
    public String getName() {
        return name;
    }

    /**
     * Sets user name
     * @param name New name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets isAdmin
     * @return True if user is administrator
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Sets isAdmin
     * @param admin New role
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


}

package com.webapplication.crossport.service;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * Implements a serializable object to receive/send user login data
 * @author Herzig Melvyn
 */
public class MemberLoginData implements Serializable {

    /**
     * Username
     */
    @NotEmpty(message = "Username can not be empty")
    String username;

    /**
     * Password
     */
    @NotEmpty(message = "Password can not be empty")
    String password;

    /**
     * Gets the username
     * @return Username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets a new username
     * @param username New username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password
     * @param password New password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

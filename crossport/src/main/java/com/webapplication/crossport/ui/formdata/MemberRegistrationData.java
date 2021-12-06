package com.webapplication.crossport.ui.formdata;

import javax.validation.constraints.NotEmpty;

/**
 * Implements a serializable object to receive/send user registration data
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
public class MemberRegistrationData {

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
     * Password confirmation.
     */
    @NotEmpty(message = "Password confirmation can not be empty")
    private String passwordConfirmation;

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


    /**
     * Gets password confirmation
     * @return Password confirmation
     */
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    /**
     * Sets the password confirmation
     * @param password Password confirmation
     */
    public void setPasswordConfirmation(String password) {
        this.passwordConfirmation = password;
    }
}

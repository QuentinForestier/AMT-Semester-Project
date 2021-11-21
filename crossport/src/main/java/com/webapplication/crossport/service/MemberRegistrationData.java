package com.webapplication.crossport.service;

import javax.validation.constraints.NotEmpty;

/**
 * Implements a serializable object to receive/send user registration data
 * @author Herzig Melvyn
 */
public class MemberRegistrationData extends MemberLoginData {

    /**
     * Password confirmation.
     */
    @NotEmpty(message = "Password confirmation can not be empty")
    private String passwordConfirmation;

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

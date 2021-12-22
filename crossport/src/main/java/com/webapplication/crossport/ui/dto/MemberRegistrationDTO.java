package com.webapplication.crossport.ui.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;

/**
 * Implements an object to receive/send user registration data
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
@Getter
@Setter
public class MemberRegistrationDTO {

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
}

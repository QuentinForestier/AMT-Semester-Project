package com.webapplication.crossport.service.exception;

import java.util.LinkedList;
import java.util.List;

/**
 * Exception to encapsulate many errors due to registration mistakes
 * @author Herzig Melvyn
 */
public class RegistrationException extends Exception{

    /**
     * Errors list.
     */
    private List<String> errors = new LinkedList<>();

    /**
     * Constructor
     */
    public RegistrationException() {
        super();
    }


    /**
     * Constructor
     * @param message Exception message to add to inner errors list.
     */
    public RegistrationException(String message) {
        super();
        addError(message);
    }

    /**
     * Adds an error to inner errors list.
     * @param error Error to add.
     */
    public void addError(String error) {
        errors.add(error);
    }

    /**
     * Gets the list of all stores errors.
     * @return The copy of list of errors.
     */
    public List<String> getErrors() {
        return List.copyOf(errors);
    }

}

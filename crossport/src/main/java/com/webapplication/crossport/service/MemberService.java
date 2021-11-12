package com.webapplication.crossport.service;

import com.webapplication.crossport.service.exception.RegistrationException;

/**
 * Defines the methodes to be used to manipulation members
 * @author Herzig Melvyn
 */
public interface MemberService {

    /**
     * Tries to registers a member
     * @param member Members data to register
     * @throws RegistrationException When the authentification service returns error(s)
     */
    void register(final MemberRegistrationData member) throws RegistrationException;
}

package com.webapplication.crossport.models.services;

import com.webapplication.crossport.service.MemberRegistrationData;
import com.webapplication.crossport.service.exception.RegistrationException;

/**
 * Defines the methods to be used to manipulation members
 * @author Herzig Melvyn
 */
public interface MemberService {

    /**
     * Tries to register a member
     * @param member Members data to register
     * @throws RegistrationException When the authentication service returns error(s)
     */
    void register(final MemberRegistrationData member) throws RegistrationException;
}

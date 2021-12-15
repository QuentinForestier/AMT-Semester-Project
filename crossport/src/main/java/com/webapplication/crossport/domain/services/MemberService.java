package com.webapplication.crossport.domain.services;

import com.webapplication.crossport.ui.dto.MemberRegistrationDTO;
import com.webapplication.crossport.config.security.exception.RegistrationException;

/**
 * Defines the methods to be used to manipulation members
 *
 * @author Berney Alec
 * @author Forestier Quentin
 * @author Gazetta Florian
 * @author Herzig Melvyn
 * @author Lamrani Soulaymane
 */
public interface MemberService {

    /**
     * Tries to register a member
     * @param member Members data to register
     * @throws RegistrationException When the authentication service returns error(s)
     */
    void register(final MemberRegistrationDTO member) throws RegistrationException;
}

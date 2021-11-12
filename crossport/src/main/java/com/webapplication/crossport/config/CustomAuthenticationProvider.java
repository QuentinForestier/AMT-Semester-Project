package com.webapplication.crossport.config;

import com.webapplication.crossport.service.AuthService;
import com.webapplication.crossport.service.RequestType;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom authentication profider to dialogate with AuthService
 * @author Herzig Melvyn
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * Contacts external auth service and tries to login user
     * @param authentication Autentication credentials (username and password)
     * @return The autentication informations if the user authenticate successfully.
     * @throws AuthenticationException If authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // Gets login infos
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Stores user role
        final List<GrantedAuthority> grantedAuths = new ArrayList<>();

        // Gets user from AuthService
        String response =  AuthService.getInstance().makeRequest(RequestType.LOGIN, username, password);
        JSONObject jsonObject = new JSONObject(response);

        // Checking errors
        if( jsonObject.has("error") ) {
            throw new BadCredentialsException(jsonObject.getString("error"));
        }

        grantedAuths.add(new SimpleGrantedAuthority(jsonObject.getJSONObject("account").getString("role")));
        UserDetails principal = new User(username, password, grantedAuths);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);

        return auth;
    }

    /**
     * Checks the if authentication object is supported
     * @param authentication Authentication object
     * @return Return true if authentication is supported
     */
    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

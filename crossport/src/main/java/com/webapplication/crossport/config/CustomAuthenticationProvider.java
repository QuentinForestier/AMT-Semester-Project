package com.webapplication.crossport.config;

import com.webapplication.crossport.service.AuthService;
import com.webapplication.crossport.service.RequestType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom authentication profider to dialogate with AuthService
 *
 * @author Herzig Melvyn
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private HttpSession session;

	/**
	 * Contacts external auth service and tries to login user
	 *
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
		String response = AuthService.getInstance().makeRequest(RequestType.LOGIN, username, password);
		JSONObject jsonObject = new JSONObject(response);

		// Checking errors
		if (jsonObject.has("error")) {
			throw new BadCredentialsException(jsonObject.getString("error"));
		}

		// This if is for testing purpose since we fail to mock session
		if(session != null ) {
			session.setAttribute("memberId", jsonObject.getJSONObject("account").getInt("id"));
		}

		grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + jsonObject.getJSONObject("account").getString("role").toUpperCase()));
		Authentication auth = new UsernamePasswordAuthenticationToken(username, password, grantedAuths);

		return auth;
	}

	/**
	 * Checks the if authentication object is supported
	 *
	 * @param authentication Authentication object
	 * @return Return true if authentication is supported
	 */
	@Override
	public boolean supports(Class<?> authentication) {

		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}


}

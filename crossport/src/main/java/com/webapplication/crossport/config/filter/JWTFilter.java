package com.webapplication.crossport.config.filter;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to filter incoming request based on JWT token get from cookie.
 * @author Herzig Melvyn
 */
public class JWTFilter extends OncePerRequestFilter {

	public static String secret;

	@Override
	protected void doFilterInternal(HttpServletRequest req,
									HttpServletResponse res,
									FilterChain chain) throws IOException, ServletException {

		String jwt = getCookieValue(req, "jwt");

		try {
			if(jwt != null) {
				if(JWTUtility.validateToken(jwt, secret)){

					final List<GrantedAuthority> grantedAuths = new ArrayList<>();
					grantedAuths.add(new SimpleGrantedAuthority("ROLE_" + JWTUtility.getRole(jwt, secret).toUpperCase()));
					Authentication auth = new UsernamePasswordAuthenticationToken(JWTUtility.getUsername(jwt, secret), jwt, grantedAuths);

					SecurityContextHolder.getContext().setAuthentication(auth);

				}
			}

		} catch (ExpiredJwtException e) {
			// Temporary solution, if token is expired log out user
			res.sendRedirect("/logout");
		}

		chain.doFilter(req, res);
	}

	/**
	 * Gets a cookie with a given name from a request
	 * @param req Request
	 * @param cookieName Cookie name
	 * @return Cookie content else null
	 */
	private String getCookieValue(HttpServletRequest req, String cookieName) {

		if( req.getCookies() == null) {
			return null;
		}

		return Arrays.stream(req.getCookies())
				.filter(c -> c.getName().equals(cookieName))
				.findFirst()
				.map(Cookie::getValue)
				.orElse(null);
	}
}

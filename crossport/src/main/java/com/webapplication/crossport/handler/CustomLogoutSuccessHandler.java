package com.webapplication.crossport.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class to handle logout
 * @author Herzig Melvyn
 */
@Component
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request,
								HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		Cookie cookie = new Cookie("jwt", "");
		cookie.setMaxAge(0);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		super.onLogoutSuccess(request, response, authentication);
	}
}

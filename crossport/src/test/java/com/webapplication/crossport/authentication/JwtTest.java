package com.webapplication.crossport.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing jwtFilter
 * @author Herzig Melvyn
 */
@SpringBootTest
@AutoConfigureMockMvc
public class JwtTest {

	@Autowired
	private MockMvc mvc;

	@Value("${com.webapplication.crossport.config.jwt.secret}")
	private String secret;

	@Test
	@WithMockUser(roles = {"ADMIN"})
	public void AsAdmin_getReservedPageWithExpiredToken_Fail() throws Exception {

		String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjcm9zc3BvcnQiLCJyb2xlIjoiYWRtaW4iLCJpc3MiOiJJSUNUIiwiZXhwIjoxNjM3NDIxNTkxLCJpYXQiOjE2MzczMzUxOTF9.oLTiY43UmRMV_bYGr3mxRGeb3tN0w-9d4oKLU1KCV1s";
		Cookie cookie = new Cookie("jwt", expiredToken);

		mvc.perform(MockMvcRequestBuilders.get("/articles/manage").cookie(cookie))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/logout"));

	}
}

package com.webapplication.crossport.authentication;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;

import java.security.Key;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testing jwtFilter
 * @author Herzig Melvyn
 */
@SpringBootTest
@AutoConfigureMockMvc
public class JwtFilterTest {

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

	@Test
	@WithAnonymousUser
	public void AsAnonymous_logInIfValidToken_Succes() throws Exception {
		// signature algorithm
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// secret
		byte[] apiKeySecretBytes = secret.getBytes();
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		// set the JWT Claims
		JwtBuilder builder = Jwts.builder()
				.setIssuedAt(now)
				.setSubject("crossport")
				.setIssuer("crossport")
				.claim("role", "admin")
				.signWith(signatureAlgorithm, signingKey)
				.setHeaderParam("typ", "JWT");

		// expiration
		long expMillis = nowMillis + 86400000 ;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp);


		// Build
		String token = builder.compact();

		Cookie cookie = new Cookie("jwt", token);

		mvc.perform(MockMvcRequestBuilders.get("/articles/manage").cookie(cookie))
				.andExpect(status().isOk())
				.andExpect(view().name("manageArticles"));
	}

}

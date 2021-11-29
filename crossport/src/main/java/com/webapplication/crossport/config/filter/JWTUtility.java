package com.webapplication.crossport.config.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

/**
 * Utility class to manipulate jwt.
 * @author Herzig Melvyn
 */
public class JWTUtility {

	/**
	 * Get username of token
	 * @param token Token
	 * @param secret Secret to check authenticity
	 * @return Username
	 */
	static String getUsername(String token, String secret) {
		return getClaimFromToken(token, secret, Claims::getSubject);
	}

	/**
	 * Get role of token
	 * @param token Token
	 * @param secret Secret to check authenticity
	 * @return Role
	 */
	static String getRole(String token, String secret) {
		Claims claims = getAllClaimsFromToken(token, secret);
		return claims.get("role", String.class);
	}

	/**
	 * Get expiration of token
	 * @param token Token
	 * @param secret Secret to check authenticity
	 * @return Expiration date
	 */
	static Date getExpirationDate(String token, String secret) {
		return getClaimFromToken(token, secret, Claims::getExpiration);
	}


	/**
	 * Getting claims
	 * @param token Token
	 * @param secret Secret to check authenticity
	 * @param claimsResolver Rsolver
	 * @param <T> Type of claim
	 * @return Claim
	 */
	static <T> T getClaimFromToken(String token, String secret, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token,secret);
		return claimsResolver.apply(claims);
	}


	/**
	 * Get all claims from token
	 * @param token Token
	 * @param secret Secret to check authenticity
	 * @return
	 */
	static Claims getAllClaimsFromToken(String token, String secret) {

		return Jwts.parser().setSigningKey( Base64.getEncoder().encodeToString(secret.getBytes()) ).parseClaimsJws(token).getBody();
	}


	/**
	 * Check if token is expired
	 * @param token Token
	 * @param secret Secret to check authenticity
	 * @return True if validity date is not reached
	 */
	static Boolean isTokenExpired(String token, String secret) {
		final Date expiration = getExpirationDate(token, secret);
		return expiration.before(new Date());
	}


	/**
	 * Validate a token
	 * @param token Token
	 * @param secret Secret to get authenticity
	 * @return True if valid else false
	 */
	static Boolean validateToken(String token, String secret) {
		return !isTokenExpired(token, secret);
	}
}

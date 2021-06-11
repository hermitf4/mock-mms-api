package com.nttdata.mock.mms.api.utils;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.nttdata.mock.mms.api.exceptions.MockMmmsException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;
	private static final String SECRET = "mysecretkey";
	
	private String token = null;
	
	@Value("classpath:jwt-secret-key")
	private transient Resource resource;
	
	public JwtTokenUtil(String jwtToken) {
		this.token = jwtToken;
	}
	
	
	public JwtTokenUtil() {
		super();
	}


	public String getToken() {
	    return token;
	}
	
	
	public String getUsernameFromToken() {
		return getClaimFromToken(Claims::getSubject);
	}

	
	public Date getExpirationDateFromToken() {
		return getClaimFromToken(Claims::getExpiration);
	}

	
	public <T> T getClaimFromToken(Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken();
		return claimsResolver.apply(claims);
	}
	
	
	public String getClaimFromTokenByName(String name) {
		final Claims claims = getAllClaimsFromToken();
		return (String)claims.get(name);
	}
	
	
	public Claims getAllClaimsFromToken() {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	
	private Boolean isTokenExpired() {
		final Date expiration = getExpirationDateFromToken();
		return expiration.before(new Date());
	}

	
	public static JwtTokenUtil generateToken(String username) throws IllegalArgumentException, JWTCreationException {
		Map<String, Object> claims = new HashMap<String, Object>(); 
		
		String subJect = "mock-mms-api";
		
		claims.put("username", username);
		
		return doGenerateToken(claims, subJect);
	}

	
	private static JwtTokenUtil doGenerateToken(Map<String, Object> claims, String subject) throws IllegalArgumentException, JWTCreationException {
		Instant issuedAt = Instant.now();
		
		Builder builder = JWT.create().withSubject(subject).withIssuedAt(Date.from(issuedAt)).withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY));

		claims.forEach((claimName, value) -> builder.withClaim(claimName, StringUtils.join(value)));

		return new JwtTokenUtil(builder.sign(Algorithm.HMAC256(SECRET)));
	}

	
	public DecodedJWT validateToken(String token) throws MockMmmsException {
		JWTVerifier verifiefToken = JWT.require(Algorithm.HMAC256(SECRET)).build();
		
		try {
			return verifiefToken.verify(token);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}
	}
	
	
	public Boolean validateToken() {
		return (!isTokenExpired());
	}
	
	public DecodedJWT decodeJwtToken(String token) throws MockMmmsException{
		
		JWTVerifier verifiefToken = JWT.require(Algorithm.HMAC256(readJwtSecretKeyFromFile())).build();
		
		try {
			return verifiefToken.verify(token);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}
		
	}
	
	public String readJwtSecretKeyFromFile () throws MockMmmsException{
		
		String secretKey = null;
		
		try {
			File file = new File("src/main/resources/jwt-secret-key");
			secretKey = new String(FileCopyUtils.copyToByteArray(file));
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}
		
		return secretKey;
	}
}
 
package com.nttdata.mock.mms.api.utils;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.nttdata.mock.mms.api.exceptions.MockMmmsException;

public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;
	
	private String token = null;
	
	public JwtTokenUtil(String jwtToken) {
		this.token = jwtToken;
	}
	
	
	public JwtTokenUtil() {
		super();
	}


	public String getToken() {
	    return token;
	}
	
	public JwtTokenUtil generateToken(String tokenFEDERA) throws IllegalArgumentException, JWTCreationException, MockMmmsException {
		Map<String, Object> claims = new HashMap<String, Object>(); 
		
		String subJect = "mock-mms-api";
		
		claims.put("CODICEFISCALE", tokenFEDERA);
		
		return doGenerateToken(claims, subJect);
	}

	
	private static JwtTokenUtil doGenerateToken(Map<String, Object> claims, String subject) throws IllegalArgumentException, JWTCreationException, MockMmmsException {
		Instant issuedAt = Instant.now();
		
		Builder builder = JWT.create().withSubject(subject).withIssuedAt(Date.from(issuedAt)).withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY));

		claims.forEach((claimName, value) -> builder.withClaim(claimName, StringUtils.join(value)));

		return new JwtTokenUtil(builder.sign(Algorithm.HMAC256(readJwtSecretKeyFromFile())));
	}

	
	public DecodedJWT validateToken(String token) throws MockMmmsException {
		JWTVerifier verifiefToken = JWT.require(Algorithm.HMAC256(readJwtSecretKeyFromFile())).build();
		
		try {
			return verifiefToken.verify(token);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}
	}
	
	public static String readJwtSecretKeyFromFile () throws MockMmmsException{
		
		String secretKey = null;
		
		try {
			File file = new File("src/main/resources/private_key");
			secretKey = new String(FileCopyUtils.copyToByteArray(file));
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}
		
		return secretKey;
	}
	
	public DecodedJWT decodeJwtTokenFedera(String token) throws MockMmmsException{
		
		String publicKey = null;
		
		try {
			File file = new File("src/main/resources/jwt-federa-key");
			publicKey = new String(FileCopyUtils.copyToByteArray(file));
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}
		
		JWTVerifier verifiefToken = JWT.require(Algorithm.HMAC256(publicKey)).build();
		
		try {
			return verifiefToken.verify(token);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}
		
	}
}
 
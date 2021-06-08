package com.nttdata.mock.mms.api.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtil {

	private static final long JWT_TOKEN_VALIDITY = 7200;
	public static final String SECRET = "mysecret";
	
	private String token = null;
	
	
	public JwtTokenUtil(String jwtToken) {
		this.token = jwtToken;
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
	
	
	private Claims getAllClaimsFromToken() {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	
	private Boolean isTokenExpired() {
		final Date expiration = getExpirationDateFromToken();
		return expiration.before(new Date());
	}

	
	public static JwtTokenUtil generateToken(String username) {
		Map<String, Object> claims = new HashMap<String, Object>(); 
		
		claims.put("sub", username);
		
		return doGenerateToken(claims, username);
	}

	
	private static JwtTokenUtil doGenerateToken(Map<String, Object> claims, String subject) {
		String token= Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		JwtTokenUtil jwtUtil = new JwtTokenUtil(token);
		return jwtUtil;
	}

	
	public Boolean validateToken(String subject) {
		final String username = getUsernameFromToken();
		return (username.equals(subject));
	}
	
	
	public Boolean validateToken() {
		return (!isTokenExpired());
	}
}
 
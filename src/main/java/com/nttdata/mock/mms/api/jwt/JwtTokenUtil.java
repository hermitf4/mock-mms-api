package com.nttdata.mock.mms.api.jwt;

import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.nttdata.mock.mms.api.exceptions.MockMmmsException;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;
	
	@Value("classpath:jwt-federa-key")
	private transient Resource publicFederaKey;
	
	@Value("classpath:private_key")
	private transient Resource privateKey;
	
	@Value("classpath:public_key")
	private transient Resource publicKey;

	public String generateToken(String token, String authType)
			throws IllegalArgumentException, JWTCreationException, MockMmmsException {
		Map<String, Object> claims = new HashMap<String, Object>();

		String subJect = "mock-mms-api";

		claims.put("CODICEFISCALE", token);
		claims.put("AUTH_TYPE", authType);

		return doGenerateToken(claims, subJect);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) throws IllegalArgumentException, JWTCreationException, MockMmmsException {
		Instant issuedAt = Instant.now();

		Builder builder = JWT.create().withSubject(subject).withIssuedAt(Date.from(issuedAt)).withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY));

		claims.forEach((claimName, value) -> builder.withClaim(claimName, StringUtils.join(value)));

		return builder.sign(Algorithm.RSA256((RSAPublicKey) getPublicKey(), ((RSAPrivateKey) getPrivateKey())));
	}

	public DecodedJWT validateToken(String token) throws MockMmmsException {

		JWTVerifier verifiefToken = JWT.require(Algorithm.RSA256((RSAPublicKey) getPublicKey(), ((RSAPrivateKey) getPrivateKey()))).build();

		try {
			return verifiefToken.verify(token);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}
	}

	private PrivateKey getPrivateKey() throws MockMmmsException {
		PrivateKey key = null;

		try (InputStream fis = privateKey.getInputStream()) {
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getMimeDecoder().decode((StringUtils.replaceEachRepeatedly(new String(FileCopyUtils.copyToByteArray(fis)), new String[] { "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----" }, new String[] { "", "" })).trim()));

			KeyFactory kf = KeyFactory.getInstance("RSA");

			key = kf.generatePrivate(spec);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}

		return key;
	}

	private PublicKey getPublicKey() throws MockMmmsException {
		PublicKey key = null;

		try (InputStream fis = publicKey.getInputStream()) {

			X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode((StringUtils.replaceEachRepeatedly(new String(FileCopyUtils.copyToByteArray(fis)), new String[] { "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----" }, new String[] { "", "" })).trim()));

			KeyFactory kf = KeyFactory.getInstance("RSA");

			key = kf.generatePublic(spec);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}

		return key;
	}

	private PublicKey getPublicFederaKey() throws MockMmmsException {
		PublicKey key = null;

		try (InputStream fis = publicFederaKey.getInputStream();) {

			X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getMimeDecoder().decode((StringUtils.replaceEachRepeatedly(new String(FileCopyUtils.copyToByteArray(fis)), new String[] { "-----BEGIN PUBLIC KEY-----", "-----END PUBLIC KEY-----" }, new String[] { "", "" })).trim()));

			KeyFactory kf = KeyFactory.getInstance("RSA");

			key = kf.generatePublic(spec);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}

		return key;
	}

	public DecodedJWT decodeJwtTokenFedera(String token) throws MockMmmsException {

		JWTVerifier verifiefToken = JWT.require(Algorithm.RSA256((RSAPublicKey) getPublicFederaKey(), null)).build();

		try {
			return verifiefToken.verify(token);
		} catch (Exception e) {
			throw new MockMmmsException(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, e.getMessage());
		}

	}
	
}

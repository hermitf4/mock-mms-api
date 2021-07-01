package com.nttdata.mock.mms.api.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.mock.mms.api.enums.MockAuthExceptionEnum;
import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.jwt.JwtTokenUtil;
import com.nttdata.mock.mms.api.model.User;
import com.nttdata.mock.mms.api.services.IAuthenticationManagement;
import com.nttdata.mock.mms.api.swagger.dto.RequestUserLoginLDAPDTO;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;
import com.nttdata.mock.mms.api.swagger.models.ResponseBase;
import com.nttdata.mock.mms.api.swagger.models.UserAuthResponse;
import com.nttdata.mock.mms.api.utils.Constants;
import com.nttdata.mock.mms.api.utils.Loggable;


@Service
public class AuthenticationManagementImpl implements IAuthenticationManagement{
	
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationManagementImpl.class);
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	@Loggable
	public AuthenticationResponse getAuthentication(HttpServletRequest httpRequest) throws MockMmmsException {
		AuthenticationResponse result = new AuthenticationResponse();
		result.setType("AuthenticationResponse");
		
		List<Cookie> cookies = Optional.ofNullable(httpRequest.getCookies()).map(Arrays::stream).orElseGet(Stream::empty).collect(Collectors.toList());
		
		Cookie cookieFedera = cookies.stream().filter(cookie -> cookie.getName().equals(Constants.AUTHFEDERA)).findAny().orElseThrow(MockAuthExceptionEnum.TOKEN_FEDERA_EXCEPTION);
		
		String tokenFEDERA = cookieFedera.getValue();
		try {
			UserAuthResponse userAuth = jwtTokenUtil.decodeJwtTokenFedera(tokenFEDERA);
			
			result.setSuccess(true);
			result.setMessage("Successful Operation.");
			result.setResultCode(200);
			result.setSchema(userAuth);
		}catch (MockMmmsException e) {
			throw e;
		}
		
		return result;
	}

	@Override
	@Loggable
	public AuthenticationResponse loginLDAP(RequestUserLoginLDAPDTO request) throws MockMmmsException {
		AuthenticationResponse result = new AuthenticationResponse();
		result.setType("AuthenticationResponse");
		
		try {
			Map<String, User> map = new HashMap<>();
		    map.put("DANILO.FAZIO", new User("Danilo","Fazio", "danilo.fazio@nttdata.com", "password", "GTFRHR45RT6RDG56"));
		    map.put("CARMELO.MILORDO", new User("Carmelo","Milordo", "carmelo.milordo@nttdata.com", "password", "EG454GTG6HDG5G5H"));
		    map.put("VINCENZO.IANNINI", new User("Vincenzo","Iannini", "vincenzo.iannini@nttdata.com", "password", "NNNVCN96C19D005H"));
		    map.put("MARIO.ROMANELLI", new User("MARCO","ROMANELLI", "MARCO.ROMANELLI@LIBERO.IT", "password", "RMNMRC66H06A944T"));
			
		    User user = map.get(request.getUsername().toUpperCase());
		    
			if(user != null && user.getPassword().equals(request.getPassword())) {
				Map<String, Object> claims = new HashMap<>();
				
				claims.put(Constants.CODICEFISCALE_CLAIM, user.getCodiceFiscale().toUpperCase());
				claims.put(Constants.NOME_CLAIM, user.getFirstName().toUpperCase());
				claims.put(Constants.COGNOME_CLAIM, user.getLastName().toUpperCase());
				claims.put(Constants.AUTHTYPE, Constants.AUTHLDAP);

				UserAuthResponse userAuth = new UserAuthResponse();
				userAuth.setToken(jwtTokenUtil.generateToken(claims));
				userAuth.setCodiceFiscale(user.getCodiceFiscale().toUpperCase());
				userAuth.setNome(user.getFirstName().toUpperCase());
				userAuth.setCognome(user.getLastName().toUpperCase());
				
				result.setSuccess(true);
				result.setMessage("Successful Operation.");
				result.setResultCode(200);
				result.setSchema(userAuth);
			}else {
				result.setSuccess(false);
				result.setMessage("Utente non trovato");
				result.setResultCode(420);
				String message = request.getUsername().toUpperCase();
				LOG.error("Operation failed with CF: {}",  message);
			}
			
		}catch (MockMmmsException e) {
			throw e;
		}
		
		return result;
	}

	@Override
	@Loggable
	public ResponseBase logout(HttpServletRequest httpRequest) throws MockMmmsException {
		ResponseBase result = new ResponseBase();
		result.setType("ResponseBase");
		String jwtToken = null;
		
		try {
			String requestTokenHeader = httpRequest.getHeader("X-auth");
			
			if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				String authType = jwtTokenUtil.validateToken(jwtToken).getClaim(Constants.AUTHTYPE).asString();
				
				if(authType.equals(Constants.AUTHFEDERA)) {
					result.setSuccess(true);
					result.setMessage("Successful Operation Logout Federa.");
					result.setResultCode(204);
				}else if (authType.equals(Constants.AUTHLDAP)){
					result.setSuccess(true);
					result.setMessage("Successful Operation Logout LDAP.");
					result.setResultCode(200);
				}
			}
		}catch (MockMmmsException e) {
			throw e;
		} 
		
		return result;
	}

}

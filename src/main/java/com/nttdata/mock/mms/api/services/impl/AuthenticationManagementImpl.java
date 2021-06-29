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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.mock.mms.api.config.UsersConfig;
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
	
	@Autowired
	private UsersConfig usersConfig;
	
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
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(e.getMessage());
			result.setResultCode(e.getErrorCode());
		} catch (Exception e1) {
			LOG.error(e1.getMessage(), e1);
			result.setSuccess(false);
			result.setMessage("Operation Failed.");
			result.setResultCode(501);
		}
		
		return result;
	}

	@Override
	@Loggable
	public AuthenticationResponse loginLDAP(RequestUserLoginLDAPDTO request) throws MockMmmsException {
		AuthenticationResponse result = new AuthenticationResponse();
		result.setType("AuthenticationResponse");
		
		ImmutablePair<String, User> user = usersConfig.checkLogin(request.getUsername().toUpperCase(), request.getPassword());
		
		if(user == null) {
			LOG.error("Bad Credentials", MockAuthExceptionEnum.BAD_CREDENTIALS_LDAP_EXCEPTION.get());
			throw MockAuthExceptionEnum.BAD_CREDENTIALS_LDAP_EXCEPTION.get();
		}
		
		try {
			Map<String, Object> claims = new HashMap<String, Object>();
			
			claims.put(Constants.CODICEFISCALE_CLAIM, user.getValue().getCodiceFiscale().toUpperCase());
			claims.put(Constants.NOME_CLAIM, user.getValue().getFirstName().toUpperCase());
			claims.put(Constants.COGNOME_CLAIM, user.getValue().getLastName().toUpperCase());
			claims.put(Constants.AUTHTYPE, Constants.AUTHLDAP);
			
			String token = jwtTokenUtil.generateToken(claims);
			
			UserAuthResponse userAuth = new UserAuthResponse();
			userAuth.setToken(token);
			userAuth.setCodiceFiscale(user.getValue().getCodiceFiscale().toUpperCase());
			userAuth.setNome(user.getValue().getFirstName().toUpperCase());
			userAuth.setCognome(user.getValue().getLastName().toUpperCase());
			
			result.setSuccess(true);
			result.setMessage("Successful Operation.");
			result.setResultCode(200);
			result.setSchema(userAuth);
		}catch (MockMmmsException e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(e.getMessage());
			result.setResultCode(e.getErrorCode());
		} catch (Exception e1) {
			LOG.error(e1.getMessage(), e1);
			result.setSuccess(false);
			result.setMessage("Operation Failed.");
			result.setResultCode(501);
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
		} catch (MockMmmsException e) {
			LOG.error(e.getMessage(), e);
			result.setSuccess(false);
			result.setMessage(e.getMessage());
			result.setResultCode(e.getErrorCode());
		} 
		
		return result;
	}

}

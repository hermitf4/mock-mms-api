package com.nttdata.mock.mms.api.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.mock.mms.api.enums.MockAuthExceptionEnum;
import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.jwt.JwtTokenUtil;
import com.nttdata.mock.mms.api.services.IAuthenticationManagement;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;
import com.nttdata.mock.mms.api.utils.Constants;


@Service
public class AuthenticationManagementImpl implements IAuthenticationManagement{
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	public AuthenticationResponse getAuthenticationFedera(HttpServletRequest httpRequest) throws MockMmmsException {
		AuthenticationResponse result = new AuthenticationResponse();
		result.setType("AuthenticationResponse");
		
		List<Cookie> cookies = Optional.ofNullable(httpRequest.getCookies()).map(Arrays::stream).orElseGet(Stream::empty).collect(Collectors.toList());
		
		Cookie cookieFedera = cookies.stream().filter(cookie -> cookie.getName().equals(Constants.COOKIENAME)).findAny().orElseThrow(MockAuthExceptionEnum.TOKEN_FEDERA_EXCEPTIOM);
		
		String tokenFEDERA = cookieFedera.getValue();
		try {
			String codiceFiscale = jwtTokenUtil.decodeJwtTokenFedera(tokenFEDERA).getClaim("CDMCODICEFISCALE").asString();
			String token = jwtTokenUtil.generateToken(codiceFiscale);
			
			result.setSuccess(true);
			result.setMessage("Successful Operation.");
			result.setResultCode(200);
			result.setToken(token);
		}catch (MockMmmsException e) {
			result.setType("AuthenticationResponse");
			result.setSuccess(false);
			result.setMessage(e.getMessage());
			result.setResultCode(e.getErrorCode());
		} catch (Exception e1) {
			result.setType("AuthenticationResponse");
			result.setSuccess(false);
			result.setMessage("Operation Failed.");
			result.setResultCode(501);
		}
		
		return result;
	}

}

package com.nttdata.mock.mms.api.services.impl;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import com.nttdata.mock.mms.api.services.IAuthenticationManagement;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;
import com.nttdata.mock.mms.api.utils.JwtTokenUtil;


@Service
public class AuthenticationManagementImpl implements IAuthenticationManagement{

	@Override
	public AuthenticationResponse getAuthenticationFedera() {
		AuthenticationResponse result = new AuthenticationResponse();
		
		try {
			String username = "test";
			//--------CookieTest----------//
			Cookie cookie = new Cookie("authFEDERA", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1QkI2RjFCQ0QwMzYzQTJFMURFMkVGODMzNDI4NjgwNyIsIkNETUNPRElDRUZJU0NBTEUiOiJIU1NCTFI4MEwxN1ozMzZFIiwiZXhwIjoxNjIzNDE0NjEwLCJpYXQiOjE2MjMzMjgyMDN9.FQPDV_Sy2awf2rfmO-xY589YmWeoSbigtfMnWOBQxvQ");
			JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
			username = jwtTokenUtil.decodeJwtToken(cookie.getValue()).getClaim("CDMCODICEFISCALE").asString();
			
			//--------CookieTest----------//
			/*final JwtTokenUtil*/ jwtTokenUtil = JwtTokenUtil.generateToken(username);
			final String token = jwtTokenUtil.getToken();
			
			result.setType("AuthenticationResponse");
			result.setSuccess(true);
			result.setMessage("Successful Operation.");
			result.setResultCode(200);
			result.setToken(token);
		}catch (Exception e) {
			result.setType("AuthenticationResponse");
			result.setSuccess(false);
			result.setMessage("Operation Failed.");
			result.setResultCode(501);
		}
		
		return result;
	}

}

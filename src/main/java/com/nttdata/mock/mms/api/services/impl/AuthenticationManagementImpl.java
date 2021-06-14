package com.nttdata.mock.mms.api.services.impl;

import org.springframework.stereotype.Service;

import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.services.IAuthenticationManagement;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;
import com.nttdata.mock.mms.api.utils.JwtTokenUtil;


@Service
public class AuthenticationManagementImpl implements IAuthenticationManagement{

	@Override
	public AuthenticationResponse getAuthenticationFedera(String tokenFEDERA) {
		AuthenticationResponse result = new AuthenticationResponse();
		result.setType("AuthenticationResponse");
		try {
			JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
			String codiceFiscale = jwtTokenUtil.decodeJwtTokenFedera(tokenFEDERA).getClaim("CDMCODICEFISCALE").asString();
			String token = jwtTokenUtil.generateToken(codiceFiscale).getToken();
			
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

package com.nttdata.mock.mms.api.services.impl;

import org.springframework.stereotype.Service;

import com.nttdata.mock.mms.api.services.IAuthenticationManagement;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;


@Service
public class AuthenticationManagementImpl implements IAuthenticationManagement{

	@Override
	public AuthenticationResponse getAuthenticationFedera() {
		AuthenticationResponse result = new AuthenticationResponse();
		
		
		
		return result;
	}

}

package com.nttdata.mock.mms.api.services;

import javax.servlet.http.HttpServletRequest;

import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;

public interface IAuthenticationManagement {
	
	AuthenticationResponse getAuthenticationFedera(HttpServletRequest httpRequest) throws MockMmmsException;
	
}

package com.nttdata.mock.mms.api.services;

import javax.servlet.http.HttpServletRequest;

import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.swagger.dto.RequestUserLoginLDAPDTO;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;
import com.nttdata.mock.mms.api.swagger.models.ResponseBase;

public interface IAuthenticationManagement {
	
	AuthenticationResponse getAuthentication(HttpServletRequest httpRequest) throws MockMmmsException;

	AuthenticationResponse loginLDAP(RequestUserLoginLDAPDTO request) throws MockMmmsException;

	ResponseBase logout(HttpServletRequest httpRequest) throws MockMmmsException;
	
}

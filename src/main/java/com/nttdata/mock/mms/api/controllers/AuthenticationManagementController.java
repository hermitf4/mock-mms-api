package com.nttdata.mock.mms.api.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.services.IAuthenticationManagement;
import com.nttdata.mock.mms.api.swagger.api.AuthenticationApi;
import com.nttdata.mock.mms.api.swagger.dto.AuthenticationResponseDTO;
import com.nttdata.mock.mms.api.swagger.dto.RequestUserLoginLDAPDTO;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;

@RestController
public class AuthenticationManagementController extends AbstractController implements AuthenticationApi{
	
	@Autowired
	IAuthenticationManagement iAuthenticationManagement;
	
	@Override
	public ResponseEntity<AuthenticationResponseDTO> getAuthenticationFedera(){
		
		ResponseEntity<AuthenticationResponseDTO> result = null;
		
		Class<AuthenticationResponseDTO> authenticationResponseClass = AuthenticationResponseDTO.class;
		
		AuthenticationResponse model = null;
		
		try {
			model = iAuthenticationManagement.getAuthenticationFedera(httpRequest);
			
			AuthenticationResponseDTO modelDTO = mapper.map(model, AuthenticationResponseDTO.class);
			
			result = buildResponseEntitySuccess(modelDTO);
		} catch (MockMmmsException e1) {
			result = buildResponseEntityException(authenticationResponseClass, e1);
		}
		
		return result;
	}
	
	@Override
	public ResponseEntity<AuthenticationResponseDTO> getAuthenticationLDAP(@Valid @RequestBody RequestUserLoginLDAPDTO request) {
		
		ResponseEntity<AuthenticationResponseDTO> result = null;
		
		Class<AuthenticationResponseDTO> authenticationResponseClass = AuthenticationResponseDTO.class;
		
		AuthenticationResponse model = null;
		
		try {
			model = iAuthenticationManagement.getAuthenticationLDAP(request);
			
			AuthenticationResponseDTO modelDTO = mapper.map(model, AuthenticationResponseDTO.class);
			
			result = buildResponseEntitySuccess(modelDTO);
		} catch (MockMmmsException e1) {
			result = buildResponseEntityException(authenticationResponseClass, e1);
		}
		
		return result;
	}
}

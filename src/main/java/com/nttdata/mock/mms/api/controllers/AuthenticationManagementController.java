package com.nttdata.mock.mms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.mock.mms.api.services.IAuthenticationManagement;
import com.nttdata.mock.mms.api.swagger.api.AuthenticationApi;
import com.nttdata.mock.mms.api.swagger.dto.AuthenticationResponseDTO;
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
			model = iAuthenticationManagement.getAuthenticationFedera();
			
			AuthenticationResponseDTO modelDTO = mapper.map(model, AuthenticationResponseDTO.class);
			
			result = buildResponseEntitySuccess(modelDTO);
		} catch (Exception e) {
			result = buildResponseEntityException(authenticationResponseClass, e);
		}
		
		return result;
	}
}

package com.nttdata.mock.mms.api.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;

import com.nttdata.mock.mms.api.enums.MockAuthExceptionEnum;
import com.nttdata.mock.mms.api.exceptions.MockMmmsException;
import com.nttdata.mock.mms.api.services.IAuthenticationManagement;
import com.nttdata.mock.mms.api.swagger.api.AuthenticationApi;
import com.nttdata.mock.mms.api.swagger.dto.AuthenticationResponseDTO;
import com.nttdata.mock.mms.api.swagger.models.AuthenticationResponse;
import com.nttdata.mock.mms.api.utils.Constants;

@RestController
public class AuthenticationManagementController extends AbstractController implements AuthenticationApi{
	
	@Autowired
	IAuthenticationManagement iAuthenticationManagement;
	
	@Override
	public ResponseEntity<AuthenticationResponseDTO> getAuthenticationFedera(){
		
		ResponseEntity<AuthenticationResponseDTO> result = null;
		
		Class<AuthenticationResponseDTO> authenticationResponseClass = AuthenticationResponseDTO.class;
		
		AuthenticationResponse model = null;
		
		List<Cookie> cookies = Optional.ofNullable(httpRequest.getCookies()).map(Arrays::stream).orElseGet(Stream::empty).collect(Collectors.toList());
		
		try {
			Cookie cookieFedera = cookies.stream().filter(cookie -> cookie.getName().equals(Constants.COOKIENAME)).findAny().orElseThrow(MockAuthExceptionEnum.TOKEN_FEDERA_EXCEPTIOM);
			
			String tokenFEDERA = cookieFedera.getValue();
			
			model = iAuthenticationManagement.getAuthenticationFedera(tokenFEDERA);
			
			AuthenticationResponseDTO modelDTO = mapper.map(model, AuthenticationResponseDTO.class);
			
			result = buildResponseEntitySuccess(modelDTO);
		} catch (MockMmmsException e1) {
			result = buildResponseEntityException(authenticationResponseClass, e1);
		}
		
		return result;
	}
}

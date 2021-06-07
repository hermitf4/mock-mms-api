package com.nttdata.mock.mms.api.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.mock.mms.api.model.User;
import com.nttdata.mock.mms.api.services.IUserManagement;
import com.nttdata.mock.mms.api.swagger.api.UsersApi;
import com.nttdata.mock.mms.api.swagger.dto.UserResponseDTO;


@RestController
public class UserManagementController extends AbstractController implements UsersApi{
	
	@Autowired
	private IUserManagement iUserManagement;
	
	@Override
	public ResponseEntity<List<UserResponseDTO>> getUsers(){
		
		ResponseEntity<List<UserResponseDTO>> result = null;
		
		List<User> model = null;
		
		try {
			model = iUserManagement.getUsers();
			
			List<UserResponseDTO> modelDTO = model.stream().map(user -> mapper.map(user, UserResponseDTO.class)).collect(Collectors.toList());
			
			result = new ResponseEntity<List<UserResponseDTO>>(modelDTO, HttpStatus.OK);
		} catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return result;
	}
}

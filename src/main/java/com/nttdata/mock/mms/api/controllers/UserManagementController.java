package com.nttdata.mock.mms.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.mock.mms.api.services.IUserManagement;

@RestController
public class UserManagementController {
	
	@Autowired
	private IUserManagement iUserManagement;
	
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public ResponseEntity<Object> getUsers(){
		return new ResponseEntity<>(iUserManagement.getUsers(), HttpStatus.OK);
	}
}
